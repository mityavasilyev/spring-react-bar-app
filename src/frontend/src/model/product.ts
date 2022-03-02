import Unit from "./unit";

export default class Product {
  id: number;
  name: string;
  description: string;
  amountLeft: number;
  unit: Unit;

  constructor(
    id: number,
    name: string,
    description: string,
    amountLeft: number,
    unit: Unit
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.amountLeft = amountLeft;
    this.unit = unit;
  }
}

export interface ProductDTO {
  id: number;
  name: string;
  description: string;
  amountLeft: number;
  unit: Unit;
}