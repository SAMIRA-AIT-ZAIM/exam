package com.example.bibliotheque.controller;

import com.example.bibliotheque.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public String getAllUsers() {
        // Retourner une liste d'utilisateurs (exemple simple)
        return "Liste des utilisateurs";
    }

    @PostMapping
    public String registerUser(@RequestBody User user) {
        // Traiter l'enregistrement de l'utilisateur
        return "Utilisateur enregistré avec succès : " + user.getUsername();
    }
}
