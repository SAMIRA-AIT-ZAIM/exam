package com.example.bibliotheque.repository;

import com.example.bibliotheque.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    // Vous pouvez ajouter des méthodes de recherche personnalisées ici si nécessaire
}
