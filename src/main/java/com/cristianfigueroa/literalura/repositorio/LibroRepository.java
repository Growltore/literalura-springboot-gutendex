package com.cristianfigueroa.literalura.repositorio;

import com.cristianfigueroa.literalura.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloIgnoreCase(String titulo);

    List<Libro> findByIdioma(String idioma);

    Long countByIdioma(String idioma);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();
}