import React, { useState } from "react";
import {
  Card,
  CardContent,
  CardHeader,
  Chip,
  Stack,
  Typography,
} from "@mui/material";
import Cocktail from "../../model/cocktail";
import exampleCocktails from "../../utilsShitPost";

const CocktailsSection: React.FC = (props) => {
  const [cocktails, setCocktails] = useState<Cocktail[]>(exampleCocktails);
  console.log("Loading cocktails data");

  return (
    <>
      <Card>
        <Typography variant={"h1"}>Cocktails</Typography>
      </Card>

      <Stack justifyContent={"center"} direction={"row"} spacing={3} mt={10}>
        {cocktails
          .sort((a, b) => a.name.localeCompare(b.name))
          .map((cocktail) => (
            <CocktailCard key={cocktail.id} cocktail={cocktail} />
          ))}
      </Stack>
    </>
  );
};

export default CocktailsSection;

const CocktailCard: React.FC<{ cocktail: Cocktail }> = (props) => {
  const { cocktail } = props;

  return (
    <Card key={cocktail.id} sx={{ px: 2}}>
      <CardHeader title={cocktail.name} />
      <Stack justifyContent={"center"} direction={"row"} spacing={1}>
        {cocktail.tags.map((tag) => (
          <Chip key={tag.id} label={tag.name} />
        ))}
      </Stack>
      <CardContent>
        <Typography variant="body2" color="text.secondary">
          {cocktail.description}
        </Typography>
      </CardContent>
      <CardContent>
        <Typography paragraph>HowTo:</Typography>
        <Typography paragraph>{cocktail.recipe.steps}</Typography>
      </CardContent>
    </Card>
  );
};
