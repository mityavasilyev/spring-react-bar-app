export default class Tag {
  id: number;
  name: string;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}

export interface TagDTO {
  id: number;
  name: string;
}
