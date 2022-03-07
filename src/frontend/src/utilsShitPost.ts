import Cocktail from "./model/cocktail";
import Tag from "./model/tag";
import Product from "./model/product";

const exampleCocktails: Cocktail[] = JSON.parse(
  "[{\n" +
    '        "id": 1,\n' +
    '        "name": "Cuba Libre",\n' +
    '        "description": "Taste some of that rum",\n' +
    '        "tags": [\n' +
    "            {\n" +
    '                "id": 1,\n' +
    '                "name": "Rum"\n' +
    "            },\n" +
    "            {\n" +
    '                "id": 3,\n' +
    '                "name": "Sweet"\n' +
    "            },\n" +
    "            {\n" +
    '                "id": 2,\n' +
    '                "name": "Cold"\n' +
    "            }\n" +
    "        ],\n" +
    '        "ingredients": [\n' +
    "            {\n" +
    '                "id": 1,\n' +
    '                "name": "Rum",\n' +
    '                "description": null,\n' +
    '                "sourceProduct": {\n' +
    '                    "id": 2,\n' +
    '                    "name": "Bacardi Carta Blanca",\n' +
    '                    "description": null,\n' +
    '                    "amountLeft": 700.0,\n' +
    '                    "unit": "MILLILITER"\n' +
    "                },\n" +
    '                "amount": 1.0,\n' +
    '                "unit": "OUNCE"\n' +
    "            },\n" +
    "            {\n" +
    '                "id": 2,\n' +
    '                "name": "Coca Cola",\n' +
    '                "description": null,\n' +
    '                "sourceProduct": {\n' +
    '                    "id": 1,\n' +
    '                    "name": "Coca Cola",\n' +
    '                    "description": null,\n' +
    '                    "amountLeft": 2000.0,\n' +
    '                    "unit": "MILLILITER"\n' +
    "                },\n" +
    '                "amount": 1.0,\n' +
    '                "unit": "OUNCE"\n' +
    "            }\n" +
    "        ],\n" +
    '        "recipe": {\n' +
    '            "id": 1,\n' +
    '            "steps": "Mix em together"\n' +
    "        },\n" +
    '        "note": "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium deserunt dolore illo necessitatibus voluptatibus? Adipisci dicta quam quisquam recusandae rem velit voluptatibus. Accusamus amet aut debitis minima, recusandae repellat repudiandae."\n' +
    "    }, {\n" +
    '        "id": 2,\n' +
    '        "name": "Mojito",\n' +
    '        "description": "Summertime",\n' +
    '        "tags": [\n' +
    "            {\n" +
    '                "id": 1,\n' +
    '                "name": "Rum"\n' +
    "            },\n" +
    "            {\n" +
    '                "id": 2,\n' +
    '                "name": "Cold"\n' +
    "            }\n" +
    "        ],\n" +
    '        "ingredients": [\n' +
    "            {\n" +
    '                "id": 4,\n' +
    '                "name": "Lemon",\n' +
    '                "description": null,\n' +
    '                "sourceProduct": {\n' +
    '                    "id": 3,\n' +
    '                    "name": "Lemon",\n' +
    '                    "description": "Supa fresh",\n' +
    '                    "amountLeft": 7.0,\n' +
    '                    "unit": "PIECE"\n' +
    "                },\n" +
    '                "amount": 2.0,\n' +
    '                "unit": "PIECE"\n' +
    "            },\n" +
    "            {\n" +
    '                "id": 5,\n' +
    '                "name": "White Rum",\n' +
    '                "description": null,\n' +
    '                "sourceProduct": {\n' +
    '                    "id": 2,\n' +
    '                    "name": "Bacardi Carta Blanca",\n' +
    '                    "description": null,\n' +
    '                    "amountLeft": 700.0,\n' +
    '                    "unit": "MILLILITER"\n' +
    "                },\n" +
    '                "amount": 1.0,\n' +
    '                "unit": "OUNCE"\n' +
    "            },\n" +
    "            {\n" +
    '                "id": 3,\n' +
    '                "name": "Soda",\n' +
    '                "description": null,\n' +
    '                "sourceProduct": null,\n' +
    '                "amount": 4.0,\n' +
    '                "unit": "OUNCE"\n' +
    "            }\n" +
    "        ],\n" +
    '        "recipe": {\n' +
    '            "id": 2,\n' +
    '            "steps": "Ya know the drill. Mix this stuff"\n' +
    "        },\n" +
    '        "note": "with alco Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium deserunt dolore illo necessitatibus voluptatibus? Adipisci dicta quam quisquam recusandae rem velit voluptatibus. Accusamus amet aut debitis minima, recusandae repellat repudiandae."\n' +
    "    }]"
);

const exampleTags: Tag[] = JSON.parse("[\n" +
    "    {\n" +
    "        \"id\": 1,\n" +
    "        \"name\": \"Rum\"\n" +
    "    },\n" +
    "    {\n" +
    "        \"id\": 2,\n" +
    "        \"name\": \"Cold\"\n" +
    "    },\n" +
    "    {\n" +
    "        \"id\": 3,\n" +
    "        \"name\": \"Sweet\"\n" +
    "    },\n" +
    "    {\n" +
    "        \"id\": 4,\n" +
    "        \"name\": \"Bitter\"\n" +
    "    }\n" +
    "]");

const exampleProducts: Product[] = JSON.parse("[\n" +
    "    {\n" +
    "        \"id\": 1,\n" +
    "        \"name\": \"Coca Cola\",\n" +
    "        \"description\": null,\n" +
    "        \"amountLeft\": 2000.0,\n" +
    "        \"unit\": \"MILLILITER\"\n" +
    "    },\n" +
    "    {\n" +
    "        \"id\": 2,\n" +
    "        \"name\": \"Bacardi Carta Blanca\",\n" +
    "        \"description\": null,\n" +
    "        \"amountLeft\": 700.0,\n" +
    "        \"unit\": \"MILLILITER\"\n" +
    "    },\n" +
    "    {\n" +
    "        \"id\": 3,\n" +
    "        \"name\": \"Lemon\",\n" +
    "        \"description\": \"Supa fresh\",\n" +
    "        \"amountLeft\": 7.0,\n" +
    "        \"unit\": \"PIECE\"\n" +
    "    }\n" +
    "]");

export default exampleCocktails;