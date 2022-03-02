export default class Recipe {
  id: number;
  steps: string;

  constructor(id: number, steps: string) {
    this.id = id;
    this.steps = steps;
  }
}

export interface RecipeDTO {
  id: number;
  steps: string;
}