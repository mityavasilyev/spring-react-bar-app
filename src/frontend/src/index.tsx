import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { SnackbarProvider } from "notistack";
import themeConfig from "./config/themeConfig";

ReactDOM.render(
  <React.StrictMode>
    <SnackbarProvider
      maxSnack={3}
      anchorOrigin={{
        vertical: "top",
        horizontal: "center",
      }}
      transitionDuration={{ enter: 500, exit: 200 }}
      iconVariant={{
        success: "👍 ",
        error: "❌ ",
        warning: "⚠️ ",
        info: "🤌 ",
      }}
      style={{
        borderRadius: themeConfig.borderRadius,
        fontSize: 15,
      }}
    >
      <App />
    </SnackbarProvider>
  </React.StrictMode>,
  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
