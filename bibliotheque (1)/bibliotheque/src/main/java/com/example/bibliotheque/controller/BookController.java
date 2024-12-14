package com.example.bibliotheque.controller;

import com.example.bibliotheque.model.Book;
import com.example.bibliotheque.model.BookRequest;
import com.example.bibliotheque.repository.BookRepository;
import com.example.bibliotheque.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;  // Injection du repository

    // Retourner un livre par titre (avec la méthode correcte)
    @PutMapping("/return")
    public ResponseEntity<String> returnBook(@RequestBody BookRequest bookRequest) {
        if (bookRequest == null || bookRequest.getTitle() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Titre du livre manquant");
        }

        List<Book> books = bookRepository.findByTitleContainingOrAuthorContainingOrCategoryContaining(bookRequest.getTitle(), "", "");

        if (!books.isEmpty() && books.get(0).isBorrowed()) {
            Book book = books.get(0);
            book.setBorrowed(false);  // Marquer le livre comme retourné
            bookRepository.save(book);  // Sauvegarder le livre mis à jour
            return ResponseEntity.ok("Livre retourné avec succès");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Le livre n'a pas été trouvé ou a déjà été retourné");
        }
    }



    // Ajouter un livre
    @Validated
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody @Valid Book book) {
        Book savedBook = bookService.addBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }


    // Modifier un livre
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book updated = bookService.updateBook(id, updatedBook);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Supprimer un livre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    // Consulter tous les livres
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Emprunter un livre
    @PostMapping("/{id}/borrow")
    public ResponseEntity<Book> borrowBook(@PathVariable Long id, @RequestParam String borrowDate, @RequestParam String returnDate) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            // Vérifier si le livre est déjà emprunté
            if (book.isBorrowed()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);  // Le livre est déjà emprunté
            }

            // Mettre à jour les informations d'emprunt
            book.setBorrowed(true);  // Marquer le livre comme emprunté
            try {
                book.setBorrowDate(new SimpleDateFormat("yyyy-MM-dd").parse(borrowDate));  // Définir la date d'emprunt
                book.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse(returnDate));  // Définir la date de retour
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Erreur de format de date
            }

            // Sauvegarder l'état mis à jour du livre
            bookRepository.save(book);  // Sauvegarder le livre avec l'état mis à jour

            // Retourner la réponse avec l'état mis à jour du livre
            return ResponseEntity.status(HttpStatus.OK).body(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Le livre n'existe pas
        }
    }




    // Retourner un livre
    @PostMapping("/{id}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (!book.isBorrowed()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Le livre est déjà marqué comme disponible.");
            }

            // Remettre le livre à l'état disponible
            book.setBorrowed(false);
            book.setBorrowDate(null);
            book.setReturnDate(null);
            bookRepository.save(book);

            return ResponseEntity.ok("Le livre a été retourné avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Livre introuvable avec ID : " + id);
        }
    }

    // Rechercher par titre


    // Rechercher par auteur et catégorie
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam(required = false) String query,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) String category) {
        if (query != null) {
            return bookService.searchBooks(query);  // Recherche avec le terme de recherche global
        } else {
            return bookService.searchBooks(title, author, category);  // Recherche avec des critères spécifiques
        }
    }






}
