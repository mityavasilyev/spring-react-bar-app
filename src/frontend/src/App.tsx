import React, { useState } from "react";
import "./App.css";
import NavigationMenu from "./components/ui/NavigationMenu";
import { createTheme, ThemeProvider, Typography } from "@mui/material";
import Notification from "./components/notifications/Notification";

function App() {
  // Themes for further use with MUI
  const darkTheme = createTheme({ palette: { mode: "dark" } });
  const lightTheme = createTheme({ palette: { mode: "light" } });

  const [message, setMessage] = useState("");

  const selectCocktailsSectionHandler = () => {
    console.log("Loading cocktails");
    console.log("Message is:" + message);
  };

  const selectInventorySectionHandler = () => {
    console.log("Loading inventory");
    setMessage("");
  };

  const selectProfileSectionHandler = () => {
    console.log("Loading profile");
    setMessage("Hello there!");
  };

  return (
    <div className="App">
      <header className="App-header">
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>
        <Typography variant={"h1"}>Hey</Typography>

        <Notification message={message} />

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
