import React, { useEffect, useState } from "react";
import axios from "axios";

const BooksListPage = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);

    // Fonction pour récupérer la liste des livres
    const fetchBooks = async () => {
        try {
            const response = await axios.get("http://localhost:8080/api/books");
            setBooks(response.data); // Assurez-vous que l'API retourne la liste des livres
            setLoading(false);
        } catch (error) {
            console.error("Erreur lors de la récupération des livres", error);
            setLoading(false);
        }
    };

    // Fonction pour emprunter un livre
    const borrowBook = async (id) => {
        try {
            const borrowDate = new Date().toISOString();
            const returnDate = new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000).toISOString(); // Exemple : 7 jours après
            await axios.post(`http://localhost:8080/api/books/${id}/borrow`, null, {
                params: { borrowDate, returnDate }
            });
            fetchBooks();  // Récupérer les livres après l'emprunt
        } catch (error) {
            console.error("Erreur lors de l'emprunt du livre", error);
        }
    };

    // Fonction pour retourner un livre
    const returnBook = async (id) => {
        try {
            await axios.post(`http://localhost:8080/api/books/${id}/return`);
            fetchBooks();  // Récupérer les livres après le retour
        } catch (error) {
            console.error("Erreur lors du retour du livre", error);
        }
    };

    useEffect(() => {
        fetchBooks(); // Récupérer les livres lors du chargement du composant
    }, []);

    if (loading) {
        return <p>Chargement des livres...</p>;
    }

    return (
        <div className="books-list-page">
            <h2>Liste des livres</h2>
            <table>
                <thead>
                <tr>
                    <th>Titre</th>
                    <th>Auteur</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {books.map((book) => (
                    <tr key={book.id}>
                        <td>{book.title}</td>
                        <td>{book.author}</td>
                        <td>{book.isBorrowed ? "Emprunté" : "Disponible"}</td>
                        <td>
                            {!book.isBorrowed && (
                                <button onClick={() => borrowBook(book.id)}>Emprunter</button>
                            )}
                            {book.isBorrowed && (
                                <button onClick={() => returnBook(book.id)}>Retourner</button>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>

            </table>
        </div>
    );
};

export default BooksListPage;
