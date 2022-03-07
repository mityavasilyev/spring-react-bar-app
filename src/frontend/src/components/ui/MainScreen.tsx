import React, { useState } from "react";
import NavigationMenu from "./NavigationMenu";
import { useSnackbar } from "notistack";
import ProfileSection from "../profile/ProfileSection";
import CocktailsSection from "../Cocktails/CocktailsSection";
import InventorySection from "../Products/InventorySection";

const MainScreen: React.FC = (props) => {
  // Snackbar utility
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();

  // State variables
  enum Section {
    cocktails,
    inventory,
    profile,
  }
  const [section, setSection] = useState<Section>(Section.cocktails);

  const notificationClickHandler = () => {
    closeSnackbar();
  };

  const selectCocktailsSectionHandler = () => {
    setSection(Section.cocktails);
    enqueueSnackbar("Selected cocktails section", {
      onClick: notificationClickHandler,
    });
  };

  const selectInventorySectionHandler = () => {
    setSection(Section.inventory);
    enqueueSnackbar("Selected inventory section", {
      onClick: notificationClickHandler,
    });
  };

  const selectProfileSectionHandler = () => {
    setSection(Section.profile);
    enqueueSnackbar("Selected profile section", {
      variant: "warning",
      onClick: notificationClickHandler,
    });
  };

  return (
    <>
      {section === Section.cocktails && <CocktailsSection />}
      {section === Section.inventory && <InventorySection />}
      {section === Section.profile && <ProfileSection />}

      {/*Rendered in the end for proper components overlap*/}
      <NavigationMenu // Navigation
        onSelectCocktails={selectCocktailsSectionHandler}
        onSelectInventory={selectInventorySectionHandler}
        onSelectProfile={selectProfileSectionHandler}
      />
    </>
  );
};

export default MainScreen;
