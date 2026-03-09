package com.aluracursos.literalura.main;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LibroRepository libroRepository;
    private List<Libro> libros;
    private List<Autor> autores;
    private Optional<Libro> libroBuscado;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Elige la acción que deseas realizar:
                    
                    1 - Agregar libros a la Colección
                    2 - Buscar libro en la Colección por título
                    3 - Mostrar todos los libros en la Colección
                    4 - Listar autores de libros en Colección
                    5 - Listar autores vivos en un año determinado
                    6 - Listar libros por idioma
                    7 - Top 10 libros en Colección con más descargas
                    
                    0 - salir
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        agregarLibroAColeccion();
                        break;
                    case 2:
                        buscarLibroPorTitulo();
                        break;
                    case 3:
                        mostrarLibrosBuscados();
                        break;
                    case 4:
                        mostrarAutoresBuscados();
                        break;
                    case 5:
                        mostrarAutoresVivosPorFecha();
                        break;
                    case 6:
                        mostrarLibrosPorIdioma();
                        break;
                    case 7:
                        mostrarTop10LibrosPorDescargas();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida, por favor elige un número disponible");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ups parece que no has ingresado un número, por favor ingresa un número válido.");
                teclado.nextLine();
            }
        }
    }

    private DatosLibro obtenerDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        Resultado resultado = conversor.obtenerDatos(json, Resultado.class);
        if (resultado.libros() == null || resultado.libros().isEmpty()) {
            System.out.println("No se encontró ningún libro con ese nombre en la base de datos de Gutendex.");
            return null;
        }
        return resultado.libros().getFirst();
    }

    private void mostrarLibro(Libro libro) {
        String datosLibro = """
                **************** %s ******************
                Temas: %s
                Autor: %s
                Idiomas disponibles: %s
                Cantidad de descargas: %d
                **************************************
                """.formatted(
                libro.getTitulo(),
                libro.getTemas(),
                libro.getAutor(),
                libro.getIdioma(),
                libro.getDescargas());
        System.out.println(datosLibro);
    }

    private void mostrarAutor(Autor autor) {
        String datosAutor = """
                Nombre: %s
                Fecha de nacimiento: %d
                Fecha de fallecimiento: %d 
                Libros escritos: < %s >
                """.formatted(
                autor.getNombreAutor(),
                autor.getFechaDeNacimiento(),
                autor.getFechaDeFallecimiento(),
                autor.getLibros().stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", ")));
        System.out.println(datosAutor);
    }

    private void agregarLibroAColeccion() {
        DatosLibro datos = obtenerDatosLibro();
        if (datos == null) return;
        List<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datos.titulo());
        if (!libroExistente.isEmpty()) {
            System.out.println("El libro '" + datos.titulo() + "' ya está registrado en la colección.");
            return;
        }
        Libro libro = new Libro(datos);
        // Aquí guardamos el autor primero si existe
        if (libro.getAutor() != null) {
            Autor autor = libro.getAutor();
            // Ve si el autor ya existía en la base de datos
            Optional<Autor> autorExistente = autorRepository
                    .findByNombreAutorContainsIgnoreCase(autor.getNombreAutor());
            if (autorExistente.isPresent()) {
                libro.setAutor(autorExistente.get()); // Utilizamos el autor que ya existe
            } else {
                autorRepository.save(autor); // O guarda el nuevo autor si no existe
            }
        }

        libroRepository.save(libro);
        System.out.println("Libro guardado: " + libro.getTitulo());
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que quieres buscar");
        var nombreLibro = teclado.nextLine();
        List<Libro> libroBuscado = libroRepository.findByTituloContainsIgnoreCase(nombreLibro);

        if (libroBuscado.isEmpty()) {
            System.out.println("No se encontró ningún libro con título '" + nombreLibro + "'");
        } else if (libroBuscado.size() == 1) {
            mostrarLibro(libroBuscado.getFirst());
        } else {
            System.out.println("Se encontraron " + libroBuscado.size() + " libros:");
            libroBuscado.forEach(this::mostrarLibro);
        }
    }

    private void mostrarLibrosBuscados() {
        libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("La lista de Libros está vacía... por ahora");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(this::mostrarLibro);
        }
    }

    private void mostrarAutoresBuscados() {
        autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("La lista de Autores está vacía por ahora, ¡busca alguno para empezar los registros!");
        } else {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombreAutor))
                    .forEach(this::mostrarAutor);
        }
    }

    private void mostrarAutoresVivosPorFecha() {
        System.out.println("Por favor digita el año que deseas consultar: ");
        try {
            Integer fecha = teclado.nextInt();
            teclado.nextLine();
            List<Autor> autores = autorRepository.buscarAutoresVivosEnDeterminadaFecha(fecha);
            System.out.println();
            if (autores.isEmpty()) {
                System.out.println("No hay autores registrados que cumplan los requisitos");
            } else {
                System.out.println("Los autores vivos en el año " + fecha + " son:");
                autores.stream()
                        .sorted(Comparator.comparing(Autor::getFechaDeNacimiento))
                        .forEach(this::mostrarAutor);
            }
        } catch (InputMismatchException e) {
            System.out.println("Se ha ingresado un año inválido, por favor trata de nuevo");
            teclado.next();
        }
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("""
                Escribe el idioma que te gustaría consultar:
                - español
                - inglés
                - portugués
                - ruso
                - chino
                """);
        var buscarIdioma = teclado.nextLine();

        try {
            Idioma idioma = Idioma.fromEspanol(buscarIdioma);
            List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
            if (idioma == null) {
                System.out.println("Idioma no reconocido, por favor escribe uno de los idiomas disponibles.");
                return;
            }
            if (librosPorIdioma.isEmpty()) {
                System.out.println("No hay libros registrados en " + buscarIdioma);
            } else {
                System.out.println("Los libros en " + buscarIdioma + " son:");
                librosPorIdioma.forEach(this::mostrarLibro);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no reconocido, por favor escribe uno de los idiomas disponibles.");
        }
    }

    private void mostrarTop10LibrosPorDescargas() {
        List<Libro> top10LibrosMasDescargados = libroRepository.findTop10LibrosPorDescargas();

        if (top10LibrosMasDescargados.isEmpty()) {
            System.out.println("No hay libros registrados aún.");
            return;
        }

        System.out.println("Estos son los 10 libros más descargados:");
        for (int i = 0; i < top10LibrosMasDescargados.size(); i++) {
            Libro libro = top10LibrosMasDescargados.get(i);
            System.out.println((i + 1) + ". " + libro.getTitulo() +
                    " - Descargas: " + libro.getDescargas());
        }
        System.out.println();
    }
}