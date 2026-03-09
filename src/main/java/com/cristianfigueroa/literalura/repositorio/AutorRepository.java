package com.cristianfigueroa.literalura.repositorio;

import com.cristianfigueroa.literalura.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreIgnoreCase(String nombre);

    List<Autor> findByNombreContainsIgnoreCase(String nombre);

    List<Autor> findByAnioNacimiento(Integer anio);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento <= :anio AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)")
    List<Autor> buscarAutoresVivosEnAnio(Integer anio);
}