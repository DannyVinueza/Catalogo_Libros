package com.literatura.catalogo.literatura.repository;

import com.literatura.catalogo.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.nombre ILIKE %:texto%")
    Optional<Autor> findByNombre(String texto);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento < :anio AND a.fechaDeFallecimiento > :anio")
    List<Autor> buscarAutoresPorAnio(int anio);
}
