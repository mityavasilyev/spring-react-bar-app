import React from "react";
import "./App.css";
import { createTheme, ThemeProvider } from "@mui/material";
import MainScreen from "./components/ui/MainScreen";
import themeConfig from "./config/themeConfig";

function App() {
  // Themes for further use with MUI
  const darkTheme = createTheme({
    palette: {
      mode: "dark",
      primary: {
        main: "#4c44cf",
      },
      secondary: {
        main: "#e953da",
      },
      background: {
        default: "#2d2d2d",
      },
    },
    components: {
      MuiCard: {
        styleOverrides: {
          root: {
            borderRadius: themeConfig.borderRadius,
            backgroundColor: "#2a2a2a",
          },
        },
        defaultProps: {
          elevation: themeConfig.paperElevation,
        },
      },
    },
  });

  const lightTheme = createTheme({
    palette: {
      mode: "light",
      primary: {
        main: "#4c44cf",
      },
      secondary: {
        main: "#e953da",
      },
    },
    components: {
      MuiCard: {
        styleOverrides: {
          root: {
            borderRadius: themeConfig.borderRadius,
          },
        },
        defaultProps: {
          elevation: themeConfig.paperElevation,
        },
      },
    },
  });

  return (
    <div className="App">
      <ThemeProvider theme={darkTheme}>
        <MainScreen />
      </ThemeProvider>
    </div>
  );
}

export default App;
