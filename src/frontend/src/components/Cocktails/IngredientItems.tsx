import Ingredient from "../../model/ingredient";
import React, { useState } from "react";
import {
  Button,
  Grid,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  MenuItem,
  Stack,
  TextField,
  Theme,
} from "@mui/material";
import LiquorIcon from "@mui/icons-material/Liquor";
import Unit from "../../model/unit";
import { $enum } from "ts-enum-util";
import themeConfig from "../../config/themeConfig";
import Product from "../../model/product";

interface IngredientItemsProps {
  ingredients: Ingredient[];
  isEditing: boolean;
  setIngredients: (newIngredients: Ingredient[]) => void;
}

const IngredientItems: React.FC<IngredientItemsProps> = (props) => {
  const { isEditing } = props;
  const [ingredients, setIngredients] = useState(props.ingredients);

  const handleAddNewIngredient = () => {
    setIngredients((prevState) => {
      const newIngredients = prevState.concat(
        new Ingredient(
          prevState.length + 1,
          "New Ingredient",
          "",
          {} as Product,
          1,
          Unit.ounce
        )
      );
      props.setIngredients(newIngredients);
      return newIngredients;
    });
  };

  const handleIngredientUpdate = (newIngredient: Ingredient) => {
    const prevIngredient = ingredients.find(ing => ing.id === newIngredient.id);
    if (prevIngredient) {
      const idx = ingredients.indexOf(prevIngredient);
      setIngredients((prevState) => {
        prevState[idx] = newIngredient;
        props.setIngredients(prevState);
        console.log(prevState);
        return prevState;
      });
    }
  }

  return (
    <>
      <List>
        {ingredients.map((ingredient) => (
          <IngredientItem
            key={ingredient.id}
            ingredient={ingredient}
            isEditing={isEditing}
            updateIngredient={handleIngredientUpdate}
          />
        ))}
      </List>
      {isEditing && (
        <Stack justifyContent={"center"}>
          <Button
            sx={{
              borderRadius: themeConfig.borderRadius,
            }}
            onClick={handleAddNewIngredient}
            variant={"outlined"}
          >
            Add new ingredient
          </Button>
        </Stack>
      )}
    </>
  );
};

export default IngredientItems;

interface IngredientItemProps {
  ingredient: Ingredient;
  isEditing: boolean;
  updateIngredient: (ingredient: Ingredient) => void;
}

const IngredientItem: React.FC<IngredientItemProps> = (props) => {
  const { ingredient, isEditing } = props;

  const [ingName, setIngName] = useState(ingredient.name);
  const [ingAmount, setIngAmount] = useState(ingredient.amount);
  const [ingUnit, setIngUnit] = useState<Unit>(ingredient.unit);

  const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.target.value && setIngName(event.target.value);
    props.updateIngredient({...ingredient, name: event.target.value, amount: ingAmount, unit: ingUnit});
  };

  const handleAmountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.target.value && setIngAmount(+event.target.value);
    props.updateIngredient({...ingredient, amount: +event.target.value, name: ingName, unit: ingUnit});
  };

  const handleUnitChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.target.value && setIngUnit(event.target.value as Unit);
    props.updateIngredient({...ingredient, unit: event.target.value as Unit, amount: ingAmount, name: ingName});
  };

  return (
    <ListItem
      key={ingredient.id}
      sx={{
        borderWidth: "1px",
        borderStyle: "solid",
        borderColor: (theme: Theme) => theme.palette.primary.dark,
        borderRadius: themeConfig.borderRadius,
        my: 1,
      }}
    >
      <ListItemAvatar>{<LiquorIcon />}</ListItemAvatar>
      <Stack direction={"row"} justifyContent={"center"}>
        <Grid container alignItems={"center"}>
          <Grid item>
            <ListItemText>
              <TextField
                placeholder={isEditing ? "Ingredient" : ""}
                defaultValue={ingName}
                variant="standard"
                onChange={handleNameChange}
                InputProps={{
                  readOnly: !isEditing,
                  disableUnderline: !isEditing,
                }}
              />
            </ListItemText>
          </Grid>
          <Stack direction={"row"} alignItems={"center"}>
            <TextField
              placeholder={isEditing ? "Amount" : ""}
              defaultValue={ingAmount}
              variant="standard"
              onChange={handleAmountChange}
              InputProps={{
                readOnly: !isEditing,
                disableUnderline: !isEditing,
              }}
              sx={{ maxWidth: "75px" }}
            />
            <TextField
              select
              placeholder={isEditing ? "Unit" : ""}
              variant={"standard"}
              value={ingUnit}
              fullWidth={true}
              onChange={handleUnitChange}
              InputProps={{
                readOnly: !isEditing,
                disableUnderline: !isEditing,
              }}
              sx={{ maxWidth: "100px" }}
              SelectProps={isEditing ? {} : { IconComponent: "a" }}
            >
              {$enum(Unit).map((unit) => (
                <MenuItem key={unit} value={unit}>
                  {unit.toLowerCase()}
                </MenuItem>
              ))}
            </TextField>
          </Stack>
        </Grid>
      </Stack>
    </ListItem>
  );
};
