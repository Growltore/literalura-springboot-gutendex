package com.cristianfigueroa.literalura;

import com.cristianfigueroa.literalura.principal.Principal;
import com.cristianfigueroa.literalura.repositorio.AutorRepository;
import com.cristianfigueroa.literalura.repositorio.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraNuevoApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraNuevoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Principal principal = new Principal(libroRepository, autorRepository);
        principal.muestraElMenu();
    }
}