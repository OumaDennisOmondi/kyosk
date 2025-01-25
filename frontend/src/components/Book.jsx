import React from "react";

const Book = ({ book, onEdit, onDelete, isLoading, error }) => {
  if (isLoading) {
    return <div data-testid="loading-spinner">Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="book-card">
      <h2>{book.title}</h2>
      <p>
        <strong>Author:</strong> {book.author}
      </p>
      <p>
        <strong>ISBN:</strong> {book.isbn}
      </p>
      <p>
        <strong>Year:</strong> {book.year}
      </p>
      <div className="book-actions">
        <button onClick={() => onEdit(book)}>Edit</button>
        <button onClick={() => onDelete(book.id)}>Delete</button>
      </div>
      <style>{`
        .book-card {
          padding: 1.5rem;
          border: 1px solid #e1e4e8;
          border-radius: 8px;
          background: white;
          margin-bottom: 1rem;
        }
        .book-actions {
          display: flex;
          gap: 1rem;
          margin-top: 1rem;
        }
        button {
          padding: 0.5rem 1rem;
          border: none;
          border-radius: 4px;
          cursor: pointer;
          background-color: #0366d6;
          color: white;
        }
        button:hover {
          background-color: #0255b3;
        }
      `}</style>
    </div>
  );
};

export default Book;
