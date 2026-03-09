package com.cristianfigueroa.literalura.principal;

import com.cristianfigueroa.literalura.modelo.Autor;
import com.cristianfigueroa.literalura.modelo.AutorApi;
import com.cristianfigueroa.literalura.modelo.Libro;
import com.cristianfigueroa.literalura.modelo.LibroApi;
import com.cristianfigueroa.literalura.modelo.RespuestaLibros;
import com.cristianfigueroa.literalura.repositorio.AutorRepository;
import com.cristianfigueroa.literalura.repositorio.LibroRepository;
import com.cristianfigueroa.literalura.servicio.ConsumoAPI;
import com.cristianfigueroa.literalura.servicio.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final Scanner lectura = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        int opcion = -1;

        while (opcion != 0) {
            String menu = """
                    
                    **************************************************
                    Sea bienvenido/a a LiterAlura
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Mostrar estadísticas de descargas
                    7 - Top 10 libros más descargados
                    8 - Buscar autor por nombre
                    9 - Buscar autores por año de nacimiento
                    
                    0 - Salir
                    **************************************************
                    """;

            System.out.println(menu);
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(lectura.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Debe ingresar un número.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosEnAnio();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> mostrarEstadisticasDeDescargas();
                case 7 -> mostrarTop10Libros();
                case 8 -> buscarAutorPorNombre();
                case 9 -> buscarAutoresPorAnioNacimiento();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro que desea buscar:");
        String tituloIngresado = lectura.nextLine().trim();

        if (tituloIngresado.isBlank()) {
            System.out.println("Debe ingresar un título válido.");
            return;
        }

        Optional<Libro> libroEnBase = libroRepository.findByTituloIgnoreCase(tituloIngresado);

        if (libroEnBase.isPresent()) {
            System.out.println("\n[INFO] El libro ya está registrado en la base de datos:");
            System.out.println(libroEnBase.get());
            return;
        }

        RespuestaLibros datos = obtenerDatosDesdeApi(tituloIngresado);

        if (datos == null || datos.resultados() == null || datos.resultados().isEmpty()) {
            System.out.println("Libro no encontrado en Gutendex.");
            return;
        }

        LibroApi primerLibro = datos.resultados().get(0);

        if (primerLibro.autores() == null || primerLibro.autores().isEmpty()) {
            System.out.println("El libro fue encontrado, pero no tiene autor disponible.");
            return;
        }

        Optional<Libro> libroExacto = libroRepository.findByTituloIgnoreCase(primerLibro.titulo());

        if (libroExacto.isPresent()) {
            System.out.println("\n[INFO] El libro ya está registrado en la base de datos:");
            System.out.println(libroExacto.get());
            return;
        }

        AutorApi primerAutorApi = primerLibro.autores().get(0);

        Autor autor = autorRepository.findByNombreIgnoreCase(primerAutorApi.nombre())
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor(primerAutorApi);
                    return autorRepository.save(nuevoAutor);
                });

        Libro libro = new Libro(primerLibro, autor);
        libroRepository.save(libro);

        System.out.println("\nLibro registrado correctamente:");
        System.out.println(libro);
    }

    private RespuestaLibros obtenerDatosDesdeApi(String titulo) {
        try {
            String tituloCodificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String json = consumoApi.obtenerDatos(URL_BASE + tituloCodificado);
            return conversor.obtenerDatos(json, RespuestaLibros.class);
        } catch (Exception e) {
            System.out.println("No fue posible consultar la API: " + e.getMessage());
            return null;
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n========== LIBROS REGISTRADOS ==========");
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }

        System.out.println("\n========== AUTORES REGISTRADOS ==========");
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año que desea consultar:");
        try {
            Integer anio = Integer.parseInt(lectura.nextLine());

            List<Autor> autoresVivos = autorRepository.buscarAutoresVivosEnAnio(anio);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio + ".");
                return;
            }

            System.out.println("\n========== AUTORES VIVOS EN " + anio + " ==========");
            autoresVivos.forEach(System.out::println);

        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                ru - ruso
                """);

        String idioma = lectura.nextLine().trim().toLowerCase();

        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        Long total = libroRepository.countByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma [" + idioma + "].");
            return;
        }

        System.out.println("\n========== LIBROS POR IDIOMA ==========");
        System.out.println("Idioma consultado: " + idioma);
        System.out.println("Cantidad de libros: " + total);
        librosPorIdioma.forEach(System.out::println);
    }

    private void mostrarEstadisticasDeDescargas() {
        List<Libro> libros = libroRepository.findAll();

        DoubleSummaryStatistics estadisticas = libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null && libro.getNumeroDescargas() > 0)
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();

        if (estadisticas.getCount() == 0) {
            System.out.println("No hay datos suficientes para generar estadísticas.");
            return;
        }

        System.out.println("\n========== ESTADÍSTICAS ==========");
        System.out.println("Cantidad de libros analizados: " + estadisticas.getCount());
        System.out.println("Promedio de descargas: " + estadisticas.getAverage());
        System.out.println("Máximo de descargas: " + estadisticas.getMax());
        System.out.println("Mínimo de descargas: " + estadisticas.getMin());
    }

    private void mostrarTop10Libros() {
        List<Libro> topLibros = libroRepository.findTop10ByOrderByNumeroDescargasDesc();

        if (topLibros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n========== TOP 10 LIBROS MÁS DESCARGADOS ==========");
        topLibros.forEach(libro ->
                System.out.println("Libro: " + libro.getTitulo() +
                        " | Descargas: " + libro.getNumeroDescargas()));
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre o parte del nombre del autor:");
        String nombre = lectura.nextLine().trim();

        if (nombre.isBlank()) {
            System.out.println("Debe ingresar un nombre válido.");
            return;
        }

        List<Autor> autores = autorRepository.findByNombreContainsIgnoreCase(nombre);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores con ese criterio.");
            return;
        }

        System.out.println("\n========== RESULTADOS DE BÚSQUEDA ==========");
        autores.forEach(System.out::println);
    }

    private void buscarAutoresPorAnioNacimiento() {
        System.out.println("Ingrese el año de nacimiento:");
        try {
            Integer anio = Integer.parseInt(lectura.nextLine());

            List<Autor> autores = autorRepository.findByAnioNacimiento(anio);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores nacidos en el año " + anio + ".");
                return;
            }

            System.out.println("\n========== AUTORES NACIDOS EN " + anio + " ==========");
            autores.forEach(System.out::println);

        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un año válido.");
        }
    }
}