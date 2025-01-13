package com.literatura.catalogo.literatura;

import com.literatura.catalogo.literatura.repository.AutorRepository;
import com.literatura.catalogo.literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(libroRepository, autorRepository);
		main.mostrarElMenu();
	}
}
