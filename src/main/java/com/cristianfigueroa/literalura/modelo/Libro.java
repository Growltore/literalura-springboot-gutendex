package com.cristianfigueroa.literalura.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    private String idioma;
    private Double numeroDescargas;

    public Libro() {
    }

    public Libro(LibroApi datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas().isEmpty()
                ? "desconocido"
                : datosLibro.idiomas().get(0).toLowerCase();
        this.numeroDescargas = datosLibro.numeroDescargas() == null
                ? 0.0
                : datosLibro.numeroDescargas();
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "----- LIBRO -----\n" +
                "Título: " + titulo + "\n" +
                "Autor: " + autor + "\n" +
                "Idioma: " + idioma + "\n" +
                "Descargas: " + numeroDescargas + "\n" +
                "-----------------";
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}