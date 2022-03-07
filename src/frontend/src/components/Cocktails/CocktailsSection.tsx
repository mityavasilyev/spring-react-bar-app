import React, { useState } from "react";
import {
  Box,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Chip,
  Collapse,
  Grid,
  IconButton,
  IconButtonProps,
  Stack,
  styled,
  Tooltip,
  Typography,
} from "@mui/material";
import Cocktail from "../../model/cocktail";
import exampleCocktails from "../../utilsShitPost";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import NotesIcon from "@mui/icons-material/Notes";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";
import CocktailModal from "./CocktailModal";

const CocktailsSection: React.FC = (props) => {
  const [cocktails, setCocktails] = useState<Cocktail[]>(exampleCocktails);
  console.log("Loading cocktails data");

  return (
    <>
      <Box sx={{ height: "40vh" }}>
        <Typography variant={"h1"}>Cocktails</Typography>
      </Box>

      <Grid
        container
        justifyContent={"center"}
        direction={"row"}
        spacing={4}
        mt={10}
      >
        {cocktails
          .sort((a, b) => a.name.localeCompare(b.name))
          .map((cocktail) => (
            <CocktailCard key={cocktail.id} cocktail={cocktail} />
          ))}
      </Grid>
      <Box sx={{ height: "100px" }} />
    </>
  );
};

export default CocktailsSection;

const CocktailCard: React.FC<{ cocktail: Cocktail }> = (props) => {
  const { cocktail } = props;

  const [isExpanded, setIsExpanded] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const handleModalOpen = () => {
    console.log("Opening modal");
    setIsModalVisible(true);
  }

  const handleModalClose = () => {
    setIsModalVisible(false);
  }

  const toggleSection = () => {
    setIsExpanded((prevState) => !prevState);
  };

  // Interface for Expand section
  interface ExpandMoreProps extends IconButtonProps {
    expand: boolean;
  }

  const ExpandMore = styled((props: ExpandMoreProps) => {
    const { expand, ...other } = props;
    return <IconButton {...other} />;
  })(({ theme, expand }) => ({
    transform: !expand ? "rotate(0deg)" : "rotate(180deg)",
    marginLeft: "auto",
  }));

  return (
    <>
      <Grid item>
        <Card key={cocktail.id} sx={{ px: 2, width: "300px" }}>
          <CardHeader
            titleTypographyProps={{ align: "center" }}
            title={cocktail.name}
          />
          <Stack justifyContent={"center"} direction={"row"} spacing={1}>
            {cocktail.tags.map((tag) => (
              <Chip key={tag.id} label={tag.name} />
            ))}
          </Stack>
          <CardContent>
            <Typography variant={"caption"} align={"center"} color="text.secondary">
              {cocktail.description}
            </Typography>

            {/*Something here overlaps the menu*/}
            <CardActions  disableSpacing={true}>
              <Tooltip title={"Open in full page"}>
                <IconButton onClick={handleModalOpen} aria-label={"Open in full page"}>
                  <OpenInNewIcon />
                </IconButton>
              </Tooltip>

              <ExpandMore
                expand={isExpanded}
                onClick={toggleSection}
                aria-expanded={isExpanded}
                aria-label={"Show note"}
              >
                <Tooltip title={isExpanded? "Hide note" : "Show note"}>
                  {isExpanded ? <ExpandMoreIcon /> : <NotesIcon />}
                </Tooltip>
              </ExpandMore>
            </CardActions>

            <Collapse in={isExpanded} unmountOnExit={true}>
              <Typography variant={"caption"}>Bruh</Typography>
            </Collapse>
          </CardContent>
        </Card>
      </Grid>
      <CocktailModal cocktail={cocktail} open={isModalVisible} onClose={handleModalClose}/>
    </>

  );
};
