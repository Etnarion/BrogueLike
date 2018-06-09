package model;

import client.view.DungeonView;
import model.elements.Element;
import model.elements.entities.Enemy;
import model.elements.items.Item;
import model.elements.items.consumable.Gold;
import model.elements.items.consumable.Heart;
import model.elements.items.weapons.Sword;
import model.elements.mechanisms.Button;
import model.elements.mechanisms.DescendingStairs;
import model.elements.mechanisms.Door;
import model.elements.mechanisms.Mechanism;
import model.elements.entities.Entity;
import model.elements.entities.Hero;
import model.elements.entities.Spider;
import model.elements.tiles.*;
import utils.Direction;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

public class Dungeon {
    private static int idEntity = 100;
    private Tile[][] tiles;
    private Entity[][] entities;
    private Enemy[][] enemies;
    private Item[][] items;
    private Mechanism[][] mechanisms;
    private Element[][] elements;
    private Hero hero;
    private LinkedList<Hero> heroes;
    private Stack<String> dungeons;
    private int currentLevel;
    private boolean initialized = false;

    static private Dungeon dungeon = new Dungeon();

    final public static int DUNGEON_SIZE = 24;

    private Dungeon() {
        currentLevel = 1;
        dungeons = new Stack<>();
        dungeons.push("2.txt");
        dungeons.push("1.txt");
        heroes = new LinkedList<>();
        tiles = new Tile[DUNGEON_SIZE][DUNGEON_SIZE];
        entities = new Entity[DUNGEON_SIZE][DUNGEON_SIZE];
        enemies = new Enemy[DUNGEON_SIZE][DUNGEON_SIZE];
        items = new Item[DUNGEON_SIZE][DUNGEON_SIZE];
        mechanisms = new Mechanism[DUNGEON_SIZE][DUNGEON_SIZE];
        elements = new Element[DUNGEON_SIZE][DUNGEON_SIZE];
        generateDungeon();
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void initHero(Hero hero) throws IOException {
        heroes.add(hero);
        placeEntity(hero);
    }

    public void initHero(int id) throws IOException {
        hero = new Hero(new Point(2+id, 2), id);
        heroes.add(hero);
        placeEntity(hero);
        DungeonView.getDungeonView().displayEntity(hero);
    }

    public void initHero(Point position, int id) throws IOException {
        hero = new Hero(position, id);
        heroes.add(hero);
        placeEntity(hero);
        DungeonView.getDungeonView().displayEntity(hero);
    }

    public LinkedList<Hero> getHeroes() {
        return heroes;
    }

    public void loadNewMap() {
        currentLevel++;
        for (Entity[] ents : entities) {
            for (Entity he : ents) {
                if (he instanceof Hero)
                    he.setPosition(new Point(2+he.getId(), 2));
            }
        }
        for (int i = 0; i < DUNGEON_SIZE; i++) {
            for (int j = 0; j < DUNGEON_SIZE; j++) {
                tiles[i][j] = null;
                entities[i][j] = null;
                enemies[i][j] = null;
                items[i][j] = null;
                mechanisms[i][j] = null;
                elements[i][j] = null;
            }
        }
        generateDungeon();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void generateDungeon() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/" + dungeons.pop()));
            String line = br.readLine();

            //Generate tiles, mechanisms and entities
            for (int i = 0; i < DUNGEON_SIZE; i++) {
                for (int j = 0; j < line.length(); j++) {
                    switch(line.charAt(j)) {
                        case 'w':
                            tiles[i][j] = new Wall(new Point(j, i));
                            elements[i][j] = tiles[i][j];
                            break;
                        case 'g':
                            tiles[i][j] = new Ground(new Point(j, i));
                            elements[i][j] = tiles[i][j];
                            break;
                        case 's':
                            tiles[i][j] = new ShallowWater(new Point(j, i));
                            elements[i][j] = tiles[i][j];
                            break;
                        case 'd':
                            tiles[i][j] = new DeepWater(new Point(j, i));
                            elements[i][j] = tiles[i][j];
                            break;
                        case '-' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            mechanisms[i][j] = new Door(new Point(j, i));
                            elements[i][j] = mechanisms[i][j];
                            break;
                        case 'a' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            entities[i][j] = new Spider(new Point(j, i), idEntity++);
                            enemies[i][j] = (Enemy)entities[i][j];
                            elements[i][j] = entities[i][j];
                            break;
                        case 'O' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            mechanisms[i][j] = new Button(new Point(j, i));
                            elements[i][j] = mechanisms[i][j];
                            break;
                        case '<' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            mechanisms[i][j] = new DescendingStairs(new Point(j, i));
                            elements[i][j] = mechanisms[i][j];
                            break;
                        case 'X' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            elements[i][j] = tiles[i][j];
                            break;
                        case '$' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            items[i][j] = new Gold(new Point(j, i), 30);
                            elements[i][j] = items[i][j];
                            break;
                        case 'h' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            items[i][j] = new Heart(new Point(j, i), 5);
                            elements[i][j] = items[i][j];
                            break;
                        case '/' :
                            tiles[i][j] = new Ground(new Point(j, i));
                            items[i][j] = new Sword(new Point(j, i), 4, 1, 1);
                            elements[i][j] = items[i][j];
                            break;
                        default:
                            tiles[i][j] = new Ground(new Point(j, i));
                            elements[i][j] = tiles[i][j];
                            break;
                    }
                }
                line = br.readLine();
            }

            //Generate links between mechanisms
            while (line != null) {
                String data[] = line.split(" ");
                Button button = (Button)getElement(new Point(Integer.valueOf(data[1]), Integer.valueOf(data[0])));
                Mechanism toLink = getMechanism(new Point(Integer.valueOf(data[3]), Integer.valueOf(data[2])));
                button.linkElement(toLink);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Hero findClosestHero(Point position, int range) {
        for (int x = position.x-range; x <= position.x+range; x++) {
            for (int y = position.y-range; y <= position.y+range; y++) {
                if (x >= 0 && y >= 0 && x < DUNGEON_SIZE && y < DUNGEON_SIZE) {
                    if (entities[y][x] instanceof Hero) {
                        return (Hero)entities[y][x];
                    }
                }
            }
        }
        return null;
    }

    public Tile getTile(Point position) {
        return tiles[position.y][position.x];
    }

    public Entity getEntity(Point position) {
        return entities[position.y][position.x];
    }

    public Mechanism getMechanism(Point position) {
        return mechanisms[position.y][position.x];
    }

    public Mechanism getMechanism(int id) {
        for (Mechanism[] mechLine : mechanisms) {
            for (Mechanism mechanism : mechLine) {
                if (mechanism != null && mechanism.getId() == id) {
                    return mechanism;
                }
            }
        }
        return null;
    }

    public Item getItem(int id) {
        for (Item[] itemLine : items) {
            for (Item item : itemLine) {
                if (item != null && item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    public Item getItem(Point position) {
        return items[position.y][position.x];
    }

    public Element getElement(Point position) {
        return elements[position.y][position.x];
    }

    public Entity getEntity(int id) {
        for (Entity[] entLine : entities) {
            for (Entity entity : entLine) {
                if (entity != null && entity.getId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }

    public Hero getHero() {
        return hero;
    }

    public Hero getHero(int id) {
        for (Hero hero : heroes) {
            if (hero.getId() == id)
                return hero;
        }
        return null;
    }

    public boolean moveEntity(Entity entity, Point newPos) {
        if (!elements[newPos.y][newPos.x].isWalkable()) {
            return false;
        }
        entities[entity.position().y][entity.position().x] = null;
        entities[newPos.y][newPos.x] = entity;
        entity.move(new Point(newPos.x, newPos.y));
        return true;
    }

    public boolean moveEntity(Entity entity, Direction direction) {
        if (!elements[entity.position().y + direction.y()][entity.position().x + direction.x()].isWalkable()) {
            return false;
        }
        entities[entity.position().y][entity.position().x] = null;
        Mechanism mechanism = mechanisms[entity.position().y][entity.position().x];
        Item item = items[entity.position().y][entity.position().x];
        if (mechanism != null)
            elements[entity.position().y][entity.position().x] = mechanism;
        else if (item != null)
            elements[entity.position().y][entity.position().x] = item;
        else
            elements[entity.position().y][entity.position().x] = tiles[entity.position().y][entity.position().x];
        entities[entity.position().y + direction.y()][entity.position().x + direction.x()] = entity;
        elements[entity.position().y + direction.y()][entity.position().x + direction.x()] = entity;
        entity.move(new Point(entity.position().x + direction.x(), entity.position().y + direction.y()));
        return true;
    }

    public void placeEntity(Entity entity) {
        entities[entity.position().y][entity.position().x] = entity;
        elements[entity.position().y][entity.position().x] = entity;
    }

    public void removeEntity(Entity entity) {
        entities[entity.position().y][entity.position().x] = null;
        elements[entity.position().y][entity.position().x] = tiles[entity.position().y][entity.position().x];
    }

    public void placeItem(Item item) {
        items[item.position().y][item.position().x] = item;
        elements[item.position().y][item.position().x] = item;
    }

    public void removeItem(Item item) {
        items[item.position().y][item.position().x] = null;
        elements[item.position().y][item.position().x] = tiles[item.position().y][item.position().x];
    }

    public Tile[][] getTiles() { return tiles; }

    public Entity[][] getEntities() { return entities; }

    public Enemy[][] getEnemies() { return enemies; }

    public Mechanism[][] getMechanisms() { return mechanisms; }

    public Element[][] getElements() {
        return elements;
    }

    static public Dungeon getDungeon() {
        return dungeon;
    }
}