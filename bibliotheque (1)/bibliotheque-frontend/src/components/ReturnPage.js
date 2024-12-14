// ReturnPage.js
import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const ReturnPage = () => {
    const [bookTitle, setBookTitle] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        setBookTitle(e.target.value);
    };

    const handleReturnBook = async () => {
        try {
            const response = await axios.put("http://localhost:8080/api/books/return", {
                title: bookTitle,
            });

            if (response.status === 200) {
                alert("Le livre a été retourné avec succès !");
            } else {
                alert(`Erreur: ${response.data}`);
            }
        } catch (error) {
            console.error("Erreur lors du retour du livre", error);
            alert(`Une erreur est survenue: ${error.response ? error.response.data : error.message}`);
        }
    };


    return (
        <div className="return-page">
            <h2>Retourner un livre emprunté</h2>
            <div>
                <label>Titre du livre :</label>
                <input
                    type="text"
                    value={bookTitle}
                    onChange={handleChange}
                    placeholder="Entrez le titre du livre"
                    required
                />
            </div>
            <button onClick={handleReturnBook}>Retourner</button>

            {message && <p>{message}</p>}
        </div>
    );
};

export default ReturnPage;
