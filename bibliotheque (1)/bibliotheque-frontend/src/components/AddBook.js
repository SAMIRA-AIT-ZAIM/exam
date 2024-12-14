import React, { useState } from 'react';

function AddBook() {
    const [bookData, setBookData] = useState({
        title: '',
        author: '',
        category: '',
    });

    const handleChange = (e) => {
        setBookData({ ...bookData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const response = await fetch('http://localhost:8080/api/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(bookData),
        });

        if (response.ok) {
            alert('Livre ajouté avec succès');
        } else {
            alert('Erreur lors de l\'ajout du livre');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                name="title"
                value={bookData.title}
                onChange={handleChange}
                placeholder="Titre"
            />
            <input
                type="text"
                name="author"
                value={bookData.author}
                onChange={handleChange}
                placeholder="Auteur"
            />
            <input
                type="text"
                name="category"
                value={bookData.category}
                onChange={handleChange}
                placeholder="Catégorie"
            />
            <button type="submit">Ajouter</button>
        </form>
    );
}

export default AddBook;
