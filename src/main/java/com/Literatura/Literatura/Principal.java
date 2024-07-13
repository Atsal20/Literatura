package com.Literatura.Literatura;

import com.Literatura.Literatura.Model.*;
import com.Literatura.Literatura.repository.AutorRepository;
import com.Literatura.Literatura.repository.LibroRepository;
import com.Literatura.Literatura.service.ConsumoAPI;
import com.Literatura.Literatura.service.ConvierteDatos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoApi;
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final ConvierteDatos conversor;
    private final LibroRepository repositoryLibro;
    private final AutorRepository repositoryAutor;
    private List<Autor> autores;
    private List<Libro> libros;

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            System.out.println("*********************************\n");
            var menu = """
                    1 - Busqueda de libros por título
                    2 - Mostrar libros registrados
                    3 - Mostrar autores registrados
                    4 - Autores vivos en determinado año
                    5 - Buscar libros por idioma
                    6 - Top 10 libros más descargados
                    7 - Libro más descargado y menos descargado
                    0 - Salir

                    """;
            System.out.println(menu);
            while (!teclado.hasNextInt()) {
                System.out.println("Formato inválido, favor de ingresar un número que esté disponible en el menú!");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    autoresVivosPorAnio();
                    break;
                case 5:
                    buscarPorIdioma();
                    break;
                case 6:
                    top10();
                    break;
                case 7:
                    rankingLibro();
                    break;
                case 0:
                    System.out.println("Finalizando la aplicación, gracias por su visita");
                    break;
                default:
                    System.out.printf("Opción inválida\n");
            }
        }
    }

    private DatosBusqueda getBusqueda() {
        System.out.println("Escribe el nombre del libro que desee buscar: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        DatosBusqueda datos = conversor.obtenerDatos(json, DatosBusqueda.class);
        return datos;
    }

    private void buscarLibro() {
        DatosBusqueda datosBusqueda = getBusqueda();
        if (datosBusqueda != null && !datosBusqueda.resultado().isEmpty()) {
            DatosLibros primerLibro = datosBusqueda.resultado().get(0);
            Libro libro = new Libro(primerLibro);
            System.out.println("***** Libro *****");
            System.out.println(libro);
            System.out.println("*****************");

            Optional<Libro> libroExiste = repositoryLibro.findByTitulo(libro.getTitulo());
            if (libroExiste.isPresent()) {
                System.out.println("\nEl libro ya está registrado\n");
            } else {
                if (!primerLibro.autor().isEmpty()) {
                    DatosAutor autor = primerLibro.autor().get(0);
                    Autor autor1 = new Autor(autor);
                    Optional<Autor> autorOptional = repositoryAutor.findByNombre(autor1.getNombre());
                    Autor autorEntity;
                    if (autorOptional.isPresent()) {
                        autorEntity = autorOptional.get();
                    } else {
                        autorEntity = repositoryAutor.save(autor1);
                    }
                    libro.setAutor(autorEntity);
                    repositoryLibro.save(libro);
                    System.out.println("********** Libro **********");
                    System.out.printf("Título: %s%nAutor: %s%nIdioma: %s%nNúmero de Descargas: %s%n",
                            libro.getTitulo(), autorEntity.getNombre(), libro.getLenguaje(), libro.getNumero_descargas());
                    System.out.println("***************************\n");
                } else {
                    System.out.println("Sin autor");
                }
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void mostrarLibros() {
        libros = repositoryLibro.findAll();
        libros.forEach(System.out::println);
    }

    private void mostrarAutores() {
        autores = repositoryAutor.findAll();
        autores.forEach(System.out::println);
    }

    private void autoresVivosPorAnio() {
        System.out.println("Digite el año a consultar: ");
        var anio = teclado.nextInt();
        teclado.nextLine();
        autores = repositoryAutor.listaAutoresVivosPorAnio(Year.of(anio));
        autores.forEach(System.out::println);
    }

    private void buscarPorIdioma() {
        System.out.println("Digite el idioma que quiere consultar: ");
        System.out.println("Opciones: EN, FR, DE, ES");
        String idioma = teclado.nextLine();
        try {
            libros = repositoryLibro.findByLenguaje(Idioma.valueOf(idioma));
            libros.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Formato inválido");
        }
    }

    private void top10() {
        libros = repositoryLibro.top10LibrosMasDescargados();
        libros.forEach(System.out::println);
    }

    private void rankingLibro() {
        IntSummaryStatistics estadisticas = new IntSummaryStatistics();
        for (Libro libro1 : repositoryLibro.findAll()) {
            Long numeroDescargas = libro1.getNumero_descargas();
            estadisticas.accept(Math.toIntExact(numeroDescargas));
        }

        Optional<Libro> libroMasDescargado = libros.stream()
                .filter(l -> l.getNumero_descargas() == estadisticas.getMax())
                .findFirst();

        Optional<Libro> libroMenosDescargado = libros.stream()
                .filter(l -> l.getNumero_descargas() == estadisticas.getMin())
                .findFirst();
        libroMasDescargado.ifPresent(libro -> System.out.println("El libro más descargado es: " + libro.getTitulo()));
        libroMenosDescargado.ifPresent(libro -> System.out.println("El libro menos descargado es: " + libro.getTitulo()));

    }
}
