package model;

import client.view.DungeonView;
import model.elements.Element;
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

public class Dungeon {
    private static int idEntity = 50;
    private Tile[][] tiles;
    private Entity[][] entities;
    private Mechanism[][] mechanisms;
    private Element[][] elements;
    private Hero hero;

    static private Dungeon dungeon = new Dungeon();

    final public static int DUNGEON_SIZE = 24;

    private Dungeon() {
        tiles = new Tile[DUNGEON_SIZE][DUNGEON_SIZE];
        entities = new Entity[DUNGEON_SIZE][DUNGEON_SIZE];
        mechanisms = new Mechanism[DUNGEON_SIZE][DUNGEON_SIZE];
        elements = new Element[DUNGEON_SIZE][DUNGEON_SIZE];
        generateDungeon();

    }

    public void initHero(Hero hero) throws IOException {
        placeEntity(hero);
    }

    public void initHero(int id) throws IOException {
        hero = new Hero(new Point(4+id, 4), id);
        placeEntity(hero);
        DungeonView.getDungeonView().displayEntity(hero);
    }

    public void generateDungeon() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/dungeon.txt"));
            String line = br.readLine();

            //Generate tiles, mechanisms and entities
            for (int i = 0; i < DUNGEON_SIZE; i++) {
                for (int j = 0; j < line.length(); j++) {
                    switch(line.charAt(j)) {
                        case 'w':
                            tiles[i][j] = new Wall(new Point(i, j));
                            elements[i][j] = tiles[i][j];
                            break;
                        case 'g':
                            tiles[i][j] = new Ground(new Point(i, j));
                            elements[i][j] = tiles[i][j];
                            break;
                        case 's':
                            tiles[i][j] = new ShallowWater(new Point(i, j));
                            elements[i][j] = tiles[i][j];
                            break;
                        case 'd':
                            tiles[i][j] = new DeepWater(new Point(i, j));
                            elements[i][j] = tiles[i][j];
                            break;
                        case '-' :
                            tiles[i][j] = new Ground(new Point(i, j));
                            mechanisms[i][j] = new Door(new Point(i, j));
                            elements[i][j] = mechanisms[i][j];
                            break;
                        case 'a' :
                            tiles[i][j] = new Ground(new Point(i, j));
                            entities[i][j] = new Spider(new Point(i, j), idEntity++);
                            elements[i][j] = entities[i][j];
                            break;
                        case 'O' :
                            tiles[i][j] = new Ground(new Point(i, j));
                            mechanisms[i][j] = new Button(new Point(i, j));
                            elements[i][j] = mechanisms[i][j];
                            break;
                        case '<' :
                            tiles[i][j] = new Ground(new Point(i, j));
                            mechanisms[i][j] = new DescendingStairs(new Point(i, j));
                            elements[i][j] = mechanisms[i][j];
                            break;
                        default:
                            tiles[i][j] = new Ground(new Point(i, j));
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

    public Element getElement(Point position) {
        return mechanisms[position.y][position.x];
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
        if (mechanism != null)
            elements[entity.position().y][entity.position().x] = mechanism;
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

    public Tile[][] getTiles() { return tiles; }

    public Entity[][] getEntities() { return entities; }

    public Mechanism[][] getMechanisms() { return mechanisms; }

    public Element[][] getElements() {
        return elements;
    }

    static public Dungeon getDungeon() {
        return dungeon;
    }
}