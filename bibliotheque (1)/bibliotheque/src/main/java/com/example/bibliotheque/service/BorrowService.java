package com.example.bibliotheque.service;

import com.example.bibliotheque.model.Book;
import com.example.bibliotheque.model.Borrow;
import com.example.bibliotheque.repository.BookRepository;
import com.example.bibliotheque.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BorrowService(BorrowRepository borrowRepository, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
    }

    // Méthode pour enregistrer un nouvel emprunt
    public String saveBorrow(Borrow borrow) {
        Book book = bookRepository.findById(borrow.getBookId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable avec ID : " + borrow.getBookId()));

        if (book.isBorrowed()) {
            return "Le livre est déjà emprunté."; // Retourner un message d'erreur si le livre est déjà emprunté
        }

        // Créer un nouvel emprunt
        borrow.setBookTitle(book.getTitle()); // Ajouter le titre du livre à l'emprunt
        borrowRepository.save(borrow);

        // Marquer le livre comme emprunté
        book.borrow();
        bookRepository.save(book);

        return "Emprunt enregistré avec succès!";
    }

    // Méthode pour récupérer tous les emprunts
    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    // Méthode pour récupérer un emprunt par son ID
    public Borrow getBorrowById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable avec ID : " + id));
    }

    // Méthode pour supprimer un emprunt par son ID
    public void deleteBorrow(Long id) {
        if (!borrowRepository.existsById(id)) {
            throw new RuntimeException("Emprunt introuvable avec ID : " + id);
        }
        borrowRepository.deleteById(id);
    }

    // Méthode pour retourner un livre emprunté
    public void returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable avec ID : " + borrowId));

        Book book = bookRepository.findById(borrow.getBookId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable avec ID : " + borrow.getBookId()));

        // Marquer le livre comme non emprunté
        book.returnBook();
        bookRepository.save(book);

        // Supprimer l'emprunt
        borrowRepository.deleteById(borrowId);
    }
}
