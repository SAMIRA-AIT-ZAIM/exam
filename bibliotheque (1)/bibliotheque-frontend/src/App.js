import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import HomePage from "./components/HomePage" ;
import SearchPage from "./components/SearchPage";
import ConsultPage from "./components/ConsultPage";
import ReturnPage from "./components/ReturnPage";
import BorrowPage from "./components/BorrowPage"; // Import de BorrowPage
import ActionPage from "./components/ActionPage";
import BooksListPage from "./components/BooksListPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/action" element={<ActionPage />} />
                <Route path="/search" element={<SearchPage />} />
                <Route path="/consult" element={<BooksListPage />} />
                <Route path="/return" element={<ReturnPage />} />
                <Route path="/emprunte" element={<BorrowPage />} /> {/* Nouvelle route */}
                <Route path="/books" element={<BooksListPage />} />
            </Routes>
        </Router>
    );
}

export default App;
