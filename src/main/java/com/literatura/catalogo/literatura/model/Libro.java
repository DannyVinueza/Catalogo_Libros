package com.literatura.catalogo.literatura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column (unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    private Double numeroDeDescargas;
    @ManyToOne
    private Autor autor;

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.autor = new Autor(datosLibro.autor().get(0));
        this.idioma = Idioma.fromString(datosLibro.idioma().get(0));
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    public Libro() {

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "-----------------------------------" + '\n' +
                "Título: " + titulo + '\n' +
                "Autor: " + (autor!= null ? autor.getNombre() : "Desconocido") + '\n' +
                "Idioma: " + (idioma == null ? "Desconocido" : idioma) + '\n' +
                "Número de descargas: " + (numeroDeDescargas != null ? numeroDeDescargas : 0)+ '\n';
    }
}
