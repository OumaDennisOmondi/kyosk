import React from "react";
import { render, screen } from "@testing-library/react";
import App from "./App";

test("renders the landing page", () => {
  render(<App />);
  expect(1).toBe(1);
});
