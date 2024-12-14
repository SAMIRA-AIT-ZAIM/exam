package com.example.bibliotheque.service;

import com.example.bibliotheque.model.Book;
import com.example.bibliotheque.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Ajouter un livre
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    // Modifier un livre
    // Modifier un livre
    public Book updateBook(Long id, Book updatedBook) {
        if (bookRepository.existsById(id)) {
            Book existingBook = bookRepository.findById(id).orElse(null);
            if (existingBook != null) {
                // Mettre à jour les champs du livre existant sans toucher à l'ID
                existingBook.setTitle(updatedBook.getTitle());
                existingBook.setAuthor(updatedBook.getAuthor());
                existingBook.setCategory(updatedBook.getCategory());
                existingBook.setBorrowed(updatedBook.isBorrowed());
                existingBook.setBorrowDate(updatedBook.getBorrowDate());
                existingBook.setReturnDate(updatedBook.getReturnDate());
                return bookRepository.save(existingBook);
            }
        }
        return null;
    }


    // Supprimer un livre
    public boolean deleteBook(Long id) {
        // Vérifie si le livre existe
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            // Supprimer le livre si trouvé
            bookRepository.delete(book);
            return true;  // Retourne vrai si la suppression a réussi
        }
        return false;  // Retourne faux si le livre n'a pas été trouvé
    }

    // Rechercher un livre par titre, auteur ou catégorie
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingOrAuthorContainingOrCategoryContaining(query, query, query);
    }

    // Consulter tous les livres
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Emprunter un livre
    public Book borrowBook(Long id, Date borrowDate, Date returnDate) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null && !book.isBorrowed()) {
            book.setBorrowed(true);
            book.setBorrowDate(borrowDate);
            book.setReturnDate(returnDate);
            return bookRepository.save(book);
        }
        return null;
    }

    // Retourner un livre
    public String returnBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (!book.isBorrowed()) {
                return "Le livre est déjà disponible.";
            }
            book.setBorrowed(false);
            book.setBorrowDate(null);
            book.setReturnDate(null);
            bookRepository.save(book);
            return "success";
        }
        return "Livre introuvable.";
    }

    public List<Book> searchBooks(String title, String author, String category) {
        return bookRepository.findAll().stream()
                .filter(book -> (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                        (category == null || book.getCategory().toLowerCase().contains(category.toLowerCase())))
                .collect(Collectors.toList());
    }

}
