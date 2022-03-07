import React, { useState } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  Modal,
  Stack,
  Switch,
  TextField,
  Theme,
} from "@mui/material";
import Cocktail from "../../model/cocktail";
import IngredientItems from "./IngredientItems";

interface CocktailModalProps {
  cocktail: Cocktail;
  open: boolean;
  onClose: () => void;
}

const CocktailModal: React.FC<CocktailModalProps> = (props) => {
  // Editor mode
  const [isEditing, setIsEditing] = useState<boolean>(false);

  const [cocktailName, setCocktailName] = useState(props.cocktail.name);
  const [cocktailTags, setCocktailTags] = useState(props.cocktail.tags);
  const [cocktailDescription, setCocktailDescription] = useState(props.cocktail.description);
  const [cocktailRecipe, setCocktailRecipe] = useState(props.cocktail.recipe);
  const [cocktailNote, setCocktailNote] = useState(props.cocktail.note);
  const [cocktailIngredients, setCocktailIngredients] = useState(props.cocktail.ingredients);

  const handleSaveCocktail = () => {
    console.log(
      new Cocktail(
        props.cocktail.id,
        cocktailName,
        cocktailDescription,
        cocktailTags,
        cocktailIngredients,
        cocktailRecipe,
        cocktailNote
      )
    );
  };

  // Error messages
  const errors = {
    name: "",
    description: "",
    tags: "",
    ingredients: "",
    recipe: "",
    note: "",
  };
  const [errorMessages, setErrorMessages] = useState(errors);

  const closeModalHandler = () => {
    props.onClose();
  };

  const editModeToggleHandler = () => {
    setIsEditing((prevState) => {
      !prevState &&
        setErrorMessages({ ...errors, note: "Note is in editing mode" });
      prevState && setErrorMessages(errors);
      return !prevState;
    });
  };

  return (
    <Modal
      open={props.open}
      onClose={closeModalHandler}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={{ minWidth: "300px", maxWidth: "600px", mx: "auto" }}>
        <Card sx={{ mx: 3, my: 15, px: 2, py: 2 }}>
          {/*Title*/}
          <CardHeader
            titleTypographyProps={{ align: "center" }}
            title={cocktailName}
          />
          <Switch checked={isEditing} onChange={editModeToggleHandler} />

          {/*Tags*/}
          <Stack justifyContent={"center"} direction={"row"} spacing={1}>
            {cocktailTags.map((tag) => (
              <Chip key={tag.id} label={tag.name} />
            ))}
          </Stack>

          {/*Name editing*/}
          {isEditing && (
            <CardContent>
              <Button onClick={handleSaveCocktail}>Save</Button>
              <TextField
                label="Cocktail Name"
                defaultValue={cocktailName}
                helperText={errorMessages.name && errorMessages.name}
                variant="standard"
                fullWidth={true}
                InputProps={{
                  readOnly: !isEditing,
                  disableUnderline: !isEditing,
                }}
                sx={{
                  borderStyle: "none",
                }}
              />
            </CardContent>
          )}

          {/*Metadata*/}
          <CardContent>
            <Stack direction={"column"} spacing={4} justifyContent={"center"}>
              {/*Description*/}
              <TextField
                label="Description"
                defaultValue={cocktailDescription}
                helperText={
                  errorMessages.description && errorMessages.description
                }
                variant="standard"
                fullWidth={true}
                multiline
                InputProps={{
                  readOnly: !isEditing,
                  disableUnderline: !isEditing,
                }}
                sx={{
                  borderStyle: "none",
                }}
              />

              {/*Recipe*/}
              <Card
                sx={{
                  mx: 4,
                  my: 15,
                  px: 4,
                  py: 2,
                  backgroundColor: (theme: Theme) =>
                    theme.palette.secondary.main,
                }}
              >
                <TextField
                  label="Recipe"
                  defaultValue={cocktailRecipe.steps}
                  helperText={errorMessages.recipe && errorMessages.recipe}
                  variant="standard"
                  fullWidth={true}
                  multiline
                  InputProps={{
                    readOnly: !isEditing,
                    disableUnderline: !isEditing,
                  }}
                  sx={{
                    borderStyle: "none",
                  }}
                />
              </Card>

              {/*Note*/}
              <TextField
                label="Note"
                defaultValue={cocktailNote}
                helperText={errorMessages.note && errorMessages.note}
                variant="standard"
                fullWidth={true}
                multiline
                error={errorMessages.note.length !== 0}
                InputProps={{
                  readOnly: !isEditing,
                  disableUnderline: !isEditing,
                }}
                sx={{
                  borderStyle: "none",
                }}
              />
            </Stack>
          </CardContent>

          {/*Ingredients*/}
          <CardContent>
            <IngredientItems
              isEditing={isEditing}
              ingredients={cocktailIngredients}
              setIngredients={setCocktailIngredients}
            />
          </CardContent>
        </Card>
      </Box>
    </Modal>
  );
};

export default CocktailModal;