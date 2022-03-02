import React, { useState } from "react";
import "./App.css";
import NavigationMenu from "./components/ui/NavigationMenu";
import { Box, createTheme, ThemeProvider, Typography } from "@mui/material";
import { useSnackbar } from "notistack";

function App() {
  // Themes for further use with MUI
  const darkTheme = createTheme({ palette: { mode: "dark" } });
  const lightTheme = createTheme({ palette: { mode: "light" } });

  // Snackbar utility
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();

  // State variables
  const [title, setTitle] = useState("Hello there");

  const notificationClickHandler = () => {
    closeSnackbar();
  };

  const selectCocktailsSectionHandler = () => {
    enqueueSnackbar("Selected cocktails section", {
      onClick: notificationClickHandler,
    });
  };

  const selectInventorySectionHandler = () => {
    enqueueSnackbar("Selected inventory section", {
      onClick: notificationClickHandler,
    });
  };

  const selectProfileSectionHandler = () => {
    enqueueSnackbar("Selected profile section", {
      variant: "warning",
      onClick: notificationClickHandler,
    });
  };

  return (
    <div className="App">
      <header className="App-header">
        <Box height={"100%"} justifyContent={"center"}>
          <Box height={"40%"} />
          <Typography variant={"h1"}>{title}</Typography>
        </Box>

        <ThemeProvider theme={darkTheme}>
          <NavigationMenu
            onSelectCocktails={selectCocktailsSectionHandler}
            onSelectInventory={selectInventorySectionHandler}
            onSelectProfile={selectProfileSectionHandler}
          />
        </ThemeProvider>
      </header>
    </div>
  );
}

export default App;
