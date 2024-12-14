package com.example.bibliotheque.controller;

import com.example.bibliotheque.model.Borrow;
import com.example.bibliotheque.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    public ResponseEntity<String> createBorrow(@RequestBody Borrow borrow) {
        // Vérifier si le livre est déjà emprunté
        String result = borrowService.saveBorrow(borrow);

        // Si le livre est déjà emprunté, retourner un message d'erreur
        if (result.equals("Le livre est déjà emprunté.")) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Emprunt enregistré avec succès !", HttpStatus.CREATED);
    }

    @GetMapping
    public List<Borrow> getAllBorrows() {
        return borrowService.getAllBorrows();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(@PathVariable Long id) {
        Borrow borrow = borrowService.getBorrowById(id);
        return ResponseEntity.ok(borrow);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorrow(@PathVariable Long id) {
        borrowService.deleteBorrow(id);
        return ResponseEntity.noContent().build();
    }
}
