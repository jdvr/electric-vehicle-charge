import "./index.css";

import React from "react";
import ReactDOM from "react-dom/client";
import { HashRouter } from "react-router-dom";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";

import App from "./App";

ReactDOM.createRoot(document.getElementById("root")).render(
  <LocalizationProvider dateAdapter={AdapterDateFns}>
    <HashRouter>
      <React.StrictMode>
        <App />
      </React.StrictMode>
    </HashRouter>
  </LocalizationProvider>
);
