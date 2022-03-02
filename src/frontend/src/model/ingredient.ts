import Product from "./product";
import Unit from "./unit";

export default class Ingredient {
  id: number;
  name: string;
  description: string;
  sourceProduct: Product;
  amount: number;
  unit: Unit;

  constructor(
    id: number,
    name: string,
    description: string,
    sourceProduct: Product,
    amount: number,
    unit: Unit
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.sourceProduct = sourceProduct;
    this.amount = amount;
    this.unit = unit;
  }
}

export interface IngredientDTO {
  id: number;
  name: string;
  description: string;
  sourceProduct: Product;
  amount: number;
  unit: Unit;
}