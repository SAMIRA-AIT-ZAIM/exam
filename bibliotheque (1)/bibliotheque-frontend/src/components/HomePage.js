import React from "react";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
    const navigate = useNavigate();

    return (
        <div className="home-page">
            <h2>Bienvenue sur la page d'accueil</h2>
            <button onClick={() => navigate("/action")}>Action</button>
            <button onClick={() => navigate("/search")}>Rechercher</button>
            <button onClick={() => navigate("/consult")}>Consulter</button>
            <button onClick={() => navigate("/return")}>Retourner emprunté</button>
            <button onClick={() => navigate("/emprunte")}>emprunté</button>

        </div>
    );
};

export default HomePage;
