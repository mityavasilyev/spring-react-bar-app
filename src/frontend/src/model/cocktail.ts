import Tag from "./tag";
import Ingredient from "./ingredient";
import Recipe from "./recipe";

export default class Cocktail {
  id: number;
  name: string;
  description: string;
  tags: Tag[];
  ingredients: Ingredient[];
  recipe: Recipe;
  note: string;

  constructor(
    id: number,
    name: string,
    description: string,
    tags: Tag[],
    ingredients: Ingredient[],
    recipe: Recipe,
    note: string
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.tags = tags;
    this.ingredients = ingredients;
    this.recipe = recipe;
    this.note = note;
  }
}

export interface CocktailDTO {
  id: string;
  name: string;
  category: string;
  subcategory: string;
}