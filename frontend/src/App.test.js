import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import App from "./App";

// Mock the fetch function
global.fetch = jest.fn();

describe("App Component", () => {
  const mockBooks = [
    {
      id: "1",
      title: "Test Book 1",
      author: "Author 1",
      isbn: "ISBN-1",
      year: 2021,
    },
    {
      id: "2",
      title: "Test Book 2",
      author: "Author 2",
      isbn: "ISBN-2",
      year: 2022,
    },
  ];

  beforeEach(() => {
    fetch.mockClear();
  });

  test("renders loading state initially", () => {
    fetch.mockImplementationOnce(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockBooks),
      })
    );

    render(<App />);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });

  test("renders books after successful API call", async () => {
    fetch.mockImplementationOnce(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockBooks),
      })
    );

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(mockBooks[0].title)).toBeInTheDocument();
    });
  });
});
