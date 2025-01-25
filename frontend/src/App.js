import { useState, useEffect } from "react";
import Book from "./components/Book";
import "./App.css";

function App() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const apiUrl = process.env.REACT_APP_API_URL || "http://kyosk.local";
        const response = await fetch(`${apiUrl}/api/books`);
        const data = await response.json();
        setBooks(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to fetch books");
        setLoading(false);
      }
    };

    fetchBooks();
  }, []);

  const handleEdit = async (book) => {
    // TODO: Implement edit functionality
    console.log("Editing book:", book);
  };

  const handleDelete = async (bookId) => {
    // TODO: Implement delete functionality
    console.log("Deleting book:", bookId);
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="container">
      <h1>Book List</h1>
      <div className="book-grid">
        {books.map((book) => (
          <Book
            key={book.id}
            book={book}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        ))}
      </div>
    </div>
  );
}

export default App;
