import React, { useState } from "react";
import { Box, IconButton, Paper, Stack, Toolbar, Tooltip } from "@mui/material";

import LocalBarRoundedIcon from "@mui/icons-material/LocalBarRounded";
import Inventory2Icon from "@mui/icons-material/Inventory2";
import PersonIcon from "@mui/icons-material/Person";
import componentsConfig from "../../config/themeConfig";

interface NavigationMenuInterface {
  onSelectCocktails: () => void;
  onSelectInventory: () => void;
  onSelectProfile: () => void;
}

const NavigationMenu: React.FC<NavigationMenuInterface> = (props) => {
  const iconStyle: object = { fontSize: "30px" };

  const menuStates = {
    cocktails: "cocktails",
    inventory: "inventory",
    profile: "profile",
  };
  const [selectedMenu, setSelectedMenu] = useState(menuStates.cocktails);

  const selectCocktailsHandler = () => {
    setSelectedMenu(menuStates.cocktails);
    console.log("Opening cocktails");
    selectedMenu !== menuStates.cocktails && props.onSelectCocktails();
  };

  const selectInventoryHandler = () => {
    setSelectedMenu(menuStates.inventory);
    console.log("Opening inventory");
    selectedMenu !== menuStates.inventory && props.onSelectInventory();
  };

  const selectProfileHandler = () => {
    setSelectedMenu(menuStates.profile);
    console.log("Opening profile");
    selectedMenu !== menuStates.profile && props.onSelectProfile();
  };

  return (
    <Box position={"fixed"} sx={{ top: "auto", bottom: 0 }}>
      <Box sx={{ p: 2 }}>
        <Paper
          variant={"elevation"}
          elevation={componentsConfig.paperElevation}
          sx={{ borderRadius: componentsConfig.borderRadius }}
        >
          <Toolbar>
            <Stack direction={"row"} spacing={4} justifyContent={"center"}>
              <MenuItem onClick={selectCocktailsHandler} tooltip={"Cocktails"}>
                <LocalBarRoundedIcon
                  color={
                    selectedMenu === menuStates.cocktails
                      ? "primary"
                      : "inherit"
                  }
                  sx={iconStyle}
                />
              </MenuItem>
              <MenuItem onClick={selectInventoryHandler} tooltip={"Inventory"}>
                <Inventory2Icon
                  color={
                    selectedMenu === menuStates.inventory
                      ? "primary"
                      : "inherit"
                  }
                  sx={iconStyle}
                />
              </MenuItem>
              <MenuItem onClick={selectProfileHandler} tooltip={"Profile"}>
                <PersonIcon
                  color={
                    selectedMenu === menuStates.profile ? "primary" : "inherit"
                  }
                  sx={iconStyle}
                />
              </MenuItem>
            </Stack>
          </Toolbar>
        </Paper>
      </Box>
    </Box>
  );
};

interface MenuItemProps {
  tooltip?: string;
  onClick: () => void;
}

const MenuItem: React.FC<MenuItemProps> = (props) => {
  const label: string = props.tooltip ? props.tooltip : "";
  const item = (
    <Tooltip title={label}>
      <IconButton onClick={props.onClick}>{props.children}</IconButton>
    </Tooltip>
  );

  return item;
};

export default NavigationMenu;