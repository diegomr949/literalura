package com.alura.literalura.service;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;

@Service
public class BookService {

    private static final String BASE_URL = "https://gutendex.com/books/";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void searchBookByTitle(String title) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "?search=" + title))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.has("results") && jsonNode.get("results").isArray() && jsonNode.get("results").size() > 0) {
                JsonNode bookNode = jsonNode.get("results").get(0);
                Book book = parseBook(bookNode);
                Author author = parseAuthor(bookNode.get("authors").get(0));

                authorRepository.save(author);
                book.setAuthor(author);
                bookRepository.save(book);

                System.out.println("Libro encontrado y guardado: " + book);
            } else {
                System.out.println("No se encontró ningún libro con ese título.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    public void listAllBooks() {
        List<Book> books = bookRepository.findAll();
        books.forEach(System.out::println);
    }

    public void listAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        authors.forEach(System.out::println);
    }

    public void listAuthorsAliveInYear(int year) {
        List<Author> authors = authorRepository.findAuthorsAliveInYear(year);
        authors.forEach(System.out::println);
    }

    public void showBooksCountByLanguage(String language) {
        long count = bookRepository.countByLanguage(language);
        System.out.println("Cantidad de libros en " + language + ": " + count);
    }

    private Book parseBook(JsonNode bookNode) {
        Book book = new Book();
        book.setTitle(bookNode.get("title").asText());
        book.setLanguage(bookNode.get("languages").get(0).asText());
        book.setDownloadCount(bookNode.get("download_count").asInt());
        return book;
    }

    private Author parseAuthor(JsonNode authorNode) {
        Author author = new Author();
        author.setName(authorNode.get("name").asText());
        author.setBirthYear(authorNode.has("birth_year") ? authorNode.get("birth_year").asInt() : null);
        author.setDeathYear(authorNode.has("death_year") ? authorNode.get("death_year").asInt() : null);
        return author;
    }
}
