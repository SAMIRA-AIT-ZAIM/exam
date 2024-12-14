import React, { useState, useEffect } from "react";
import axios from "axios";

const ActionPage = () => {
    const [books, setBooks] = useState([]);
    const [newBook, setNewBook] = useState({ title: "", author: "", category: "" });
    const [editBook, setEditBook] = useState(null);

    // Charger les livres
    const fetchBooks = async () => {
        try {
            const response = await axios.get("http://localhost:8080/api/books");
            setBooks(response.data);
        } catch (error) {
            console.error("Erreur lors de la récupération des livres", error);
        }
    };

    useEffect(() => {
        fetchBooks();
    }, []);

    // Ajouter un livre
    const handleAddBook = async (e) => {
        e.preventDefault();
        try {
            await axios.post("http://localhost:8080/api/books", newBook);
            setNewBook({ title: "", author: "", category: "" });
            fetchBooks();
        } catch (error) {
            console.error("Erreur lors de l'ajout du livre", error);
        }
    };

    // Modifier un livre
    const handleEditBook = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:8080/api/books/${editBook.id}`, editBook);
            setEditBook(null);
            fetchBooks();
        } catch (error) {
            console.error("Erreur lors de la modification du livre", error);
        }
    };

    // Supprimer un livre
    const handleDeleteClick = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/books/${id}`);
            fetchBooks();
        } catch (error) {
            console.error("Erreur lors de la suppression du livre", error);
        }
    };

    return (
        <div>
            <h2>Gestion des Livres</h2>

            {/* Liste des livres */}
            <ul>
                {books.map((book) => (
                    <li key={book.id}>
                        {book.title} - {book.author} - {book.category}
                        <button onClick={() => setEditBook(book)}>Modifier</button>
                        <button onClick={() => handleDeleteClick(book.id)}>Supprimer</button>
                    </li>
                ))}
            </ul>

            {/* Ajouter un livre */}
            <form onSubmit={handleAddBook}>
                <input
                    type="text"
                    placeholder="Titre"
                    value={newBook.title}
                    onChange={(e) => setNewBook({ ...newBook, title: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Auteur"
                    value={newBook.author}
                    onChange={(e) => setNewBook({ ...newBook, author: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Catégorie"
                    value={newBook.category}
                    onChange={(e) => setNewBook({ ...newBook, category: e.target.value })}
                    required
                />
                <button type="submit">Ajouter</button>
            </form>

            {/* Modifier un livre */}
            {editBook && (
                <form onSubmit={handleEditBook}>
                    <input
                        type="text"
                        placeholder="Titre"
                        value={editBook.title}
                        onChange={(e) => setEditBook({ ...editBook, title: e.target.value })}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Auteur"
                        value={editBook.author}
                        onChange={(e) => setEditBook({ ...editBook, author: e.target.value })}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Catégorie"
                        value={editBook.category}
                        onChange={(e) => setEditBook({ ...editBook, category: e.target.value })}
                        required
                    />
                    <button type="submit">Modifier</button>
                </form>
            )}
        </div>
    );
};

export default ActionPage;
