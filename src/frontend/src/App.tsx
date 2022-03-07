import React from "react";
import "./App.css";
import { createTheme, CssBaseline, ThemeProvider } from "@mui/material";
import MainScreen from "./components/ui/MainScreen";
import themeConfig from "./config/themeConfig";

function App() {
  // Themes for further use with MUI
  const darkTheme = createTheme({
    palette: {
      mode: "dark",
      primary: {
        main: "#e0c01a",
      },
      secondary: {
        main: "#5247e5",
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
            backgroundColor: "#2f2f2f",
          },
        },
        defaultProps: {
          elevation: themeConfig.paperElevation,
        },
      },
      MuiModal: {
        styleOverrides: {
          root: {
            overflow: "scroll",
            backgroundColor: "rgba(38,38,38,0.84)",
          },
        },
      },
    },
  });

  const lightTheme = createTheme({
    palette: {
      background: {
        default: "#f9f4ff",
      },
      mode: "light",
      primary: {
        main: "#cbc8ff",
      },
      secondary: {
        main: "#d7f7ff",
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
      MuiModal: {
        styleOverrides: {
          root: {
            overflow: "scroll",
            backgroundColor: "rgba(249,244,255,0.87)",
          },
        },
      },
    },
  });

  return (
    <>
      <ThemeProvider theme={darkTheme}>
        <CssBaseline />
        <MainScreen />
      </ThemeProvider>
    </>
  );
}

export default App;
