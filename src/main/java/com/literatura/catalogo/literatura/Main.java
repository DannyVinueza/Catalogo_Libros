package com.literatura.catalogo.literatura;

import com.literatura.catalogo.literatura.model.Autor;
import com.literatura.catalogo.literatura.model.DatosLibro;
import com.literatura.catalogo.literatura.model.Idioma;
import com.literatura.catalogo.literatura.model.Libro;
import com.literatura.catalogo.literatura.repository.AutorRepository;
import com.literatura.catalogo.literatura.repository.LibroRepository;
import com.literatura.catalogo.literatura.service.ConsumoApi;
import com.literatura.catalogo.literatura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Main {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> libros;
    private List<Autor> autores;


    public Main(LibroRepository libroRepository, AutorRepository autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Escriba el número de la opción que desea ejecutar:
                    1 .- Buscar libro por título
                    2 .- Listar todos los libros registrados
                    3 .- Listar autores registrados
                    4 .- Listar autores vivos en un determinado año
                    5 .- Listar libros por idioma
                    6 .- Top 10 de los libros más descargados
                    7 .- Estadísticas de los libros registrados

                    0 .- Salir
                    """;
            System.out.println(menu);
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    guardarLibro();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    topLibrosMasDescargados();
                    break;
                case 7:
                    mostrarEstadisticas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción ingresada inválida. Intente nuevamente ingresando un valor de acuerdo al menú.");
            }
        }
    }

    public DatosLibro getDatosLibro() {
        System.out.println("Escribe el título del libro que desea buscar: ");
        var tituloBuscado = sc.nextLine();
        try {
            var json = consumoApi.obtenerDatos(tituloBuscado.replace(" ", "+"));
            var datos = conversor.obtenerDatos(json, DatosLibro.class);
            return datos;
        } catch (Exception e) {
            System.out.println("Opción ingresada inválida. Intente nuevamente ingresando un valor de acuerdo al menú.");
        }
        return null;
    }

    private void guardarLibro() {
        DatosLibro datosLibro = getDatosLibro();
        if (datosLibro != null) {
            Optional<Autor> nombreAutor = autorRepository.findByNombre(datosLibro.autor().get(0).nombre());

            Libro libro = new Libro();
            libro.setTitulo(datosLibro.titulo());
            libro.setIdioma(Idioma.fromString(datosLibro.idioma().get(0)));
            libro.setNumeroDeDescargas(datosLibro.numeroDeDescargas());

            if (nombreAutor.isPresent()) {
                Autor autorExistente = nombreAutor.get();
                libro.setAutor(autorExistente);
            } else {
                Autor nuevoAutor = new Autor(datosLibro.autor().get(0));
                nuevoAutor = autorRepository.save(nuevoAutor);
                libro.setAutor(nuevoAutor);
            }
            try {
                Optional<Libro> libroExistente = libroRepository.findByTituloContainingIgnoreCase(datosLibro.titulo());
                if (libroExistente.isPresent()) {
                    System.out.println("El libro ya está registrado.");
                    return;
                }
                libroRepository.save(libro);
                System.out.println("Libro registrado exitosamente");
            } catch (Exception e) {
                System.out.println("Libro registrado anteriormente");
            }
        } else {
            System.out.println("Opción ingresada inválida. Intente nuevamente ingresando un valor de acuerdo al menú.");
        }
    }

    private void listarLibrosRegistrados(){
        System.out.println("---Lista de libros registrados---");
        libros =libroRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("Aún no hay libros registrados en la base de datos");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }

    }

    private void listarAutoresRegistrados(){
        System.out.println("Autores Registrados");
        autores = autorRepository.findAll();
        if (autores.isEmpty()){
            System.out.println("Todavía no hay autores registrados");
        } else {
            for (int i = 0; i < autores.size(); i++){
                List<Libro> librosPorAutor = libroRepository.findLibrosByAutorId(autores.get(i).getId());
                System.out.println("\n" + autores.get(i).toString());
                System.out.println("Libros registrados: ");
                librosPorAutor.forEach(l -> System.out.println("- " + l.getTitulo()));
            }
        }
    }

    private void listarAutoresVivosPorAnio(){
        System.out.println("Ingrese un año para buscar a los autores que seguían con vida");
        var anio = sc.nextInt();
        List<Autor> autoresVivos = autorRepository.buscarAutoresPorAnio(anio);
        System.out.println("---Autores registrados vivos en " + anio + "---");
        autoresVivos.forEach(a -> System.out.println("\n" + a.toString()));
    }

    private void listarLibrosPorIdioma() {
        System.out.println(
                """
                        Idiomas disponibles:
                        es - Español
                        en - Inglés
                        pt - Portugués
                        fr - Francés
                        it - Italiano
                        de - Alemán
                        """
        );
        System.out.println("Ingrese el idioma que desea buscar:");
        var idiomaBuscado = sc.nextLine();

        try {

            List<Libro> librosPorIdioma = libroRepository.findByIdioma(Idioma.fromString(idiomaBuscado));
            if (!librosPorIdioma.isEmpty()) {
                System.out.println("---Libros registrados publicados en " + Idioma.fromString(idiomaBuscado) + "---");
                librosPorIdioma.forEach(l -> System.out.println("\n" + l.toString()));
            } else {
                System.out.println("No se han encontrado libros en ese idioma.");
            }
        } catch (Exception e) {
            System.out.println("Opción ingresada inválida. Intente nuevamente ingresando un valor de acuerdo al menú.");
        }
    }

    private void topLibrosMasDescargados(){
        System.out.println("---Top 10 de los libros más descargados---");
        List<Libro> top10 = libroRepository.findTop10();
        top10.forEach(l -> System.out.println(l.toString()));
    }

    private void mostrarEstadisticas() {
        System.out.println("---Estadísticas de los libros registrados---");
        libros = libroRepository.findAll();
        DoubleSummaryStatistics estadisticas = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() > 0.0)
                .collect(Collectors.summarizingDouble(Libro::getNumeroDeDescargas));
        System.out.println("Máximo de descargas: " + estadisticas.getMax());
        System.out.println("Mínimo de descargas: " + estadisticas.getMin());
        System.out.println("Cantidad de libros registrados: " + estadisticas.getCount());
        System.out.println("Total de descargas de todos los libros registrados: " + estadisticas.getSum());
        System.out.println("Promedio de descargas: " + Math.round(estadisticas.getAverage()));
    }
}
