package com.alura.literalura.controller;

import com.alura.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menú:");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar todos los libros");
            System.out.println("3. Listar autores");
            System.out.println("4. Listar autores vivos en un determinado año");
            System.out.println("5. Mostrar cantidad de libros por idioma");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    System.out.print("Ingrese el título del libro: ");
                    String title = scanner.nextLine();
                    bookService.searchBookByTitle(title);
                    break;
                case 2:
                    bookService.listAllBooks();
                    break;
                case 3:
                    bookService.listAllAuthors();
                    break;
                case 4:
                    System.out.print("Ingrese el año: ");
                    int year = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    bookService.listAuthorsAliveInYear(year);
                    break;
                case 5:
                    System.out.print("Ingrese el idioma: ");
                    String language = scanner.nextLine();
                    bookService.showBooksCountByLanguage(language);
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
}
