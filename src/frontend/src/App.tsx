import React from "react";
import "./App.css";
import NavigationMenu from "./components/ui/NavigationMenu";
import { createTheme, ThemeProvider, Typography } from "@mui/material";

function App() {
  const darkTheme = createTheme({ palette: { mode: "dark" } });
  const lightTheme = createTheme({ palette: { mode: "light" } });

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

        <ThemeProvider theme={darkTheme}>
          <NavigationMenu
            onSelectCocktails={() => {
              console.log("Loading cocktails");
            }}
            onSelectInventory={() => {
              console.log("Loading inventory");
            }}
            onSelectProfile={() => {
              console.log("Loading profile");
            }}
          />
        </ThemeProvider>
      </header>
    </div>
  );
}

export default App;
