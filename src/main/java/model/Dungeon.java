package model;

import client.view.DungeonView;
import model.entities.Entity;
import model.entities.Hero;
import model.entities.Spider;
import model.tiles.*;
import utils.Direction;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dungeon {
    private Tile[][] tiles;
    private Entity[][] entities;
    private Hero hero;

    static private Dungeon dungeon;

    static {
        try {
            dungeon = new Dungeon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final public static int DUNGEON_SIZE = 24;

    private Dungeon() throws IOException {
        tiles = new Tile[DUNGEON_SIZE][DUNGEON_SIZE];
        generateDungeon();
        entities = new Entity[DUNGEON_SIZE][DUNGEON_SIZE];
    }

    public synchronized void initHero(Hero hero) throws IOException {
        placeEntity(hero);
    }

    public synchronized void initHero(int id) throws IOException {
        hero = new Hero(new Point(4+id, 4), id);
        placeEntity(hero);
        initEnemies();
        DungeonView.getDungeonView().displayEntity(hero);
    }

    public void generateDungeon() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/dungeon.txt"));
            String line = br.readLine();

            int i = 0;
            while (line != null) {
                for (int j = 0; j < line.length(); j++) {
                    switch(line.charAt(j)) {
                        case 'w':
                            tiles[i][j] = new Wall();
                            break;
                        case 'g':
                            tiles[i][j] = new Ground();
                            break;
                        case 's':
                            tiles[i][j] = new ShallowWater();
                            break;
                        case 'd':
                            tiles[i][j] = new DeepWater();
                            break;
                        default:
                            tiles[i][j] = new Ground();
                            break;
                    }
                }
                line = br.readLine();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initEnemies() {
        Spider spider = new Spider(new Point(20, 20), 5);
        placeEntity(spider);
    }

    public Tile getTile(Point position) {
        return tiles[position.y][position.x];
    }

    public Entity getEntity(Point position) {
        return entities[position.y][position.x];
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
        if (entities[newPos.y][newPos.x] instanceof Unwalkable && tiles[newPos.y][newPos.x] instanceof Unwalkable) {
            return false;
        }
        entities[entity.position().y][entity.position().x] = null;
        entities[newPos.y][newPos.x] = entity;
        entity.move(new Point(newPos.x, newPos.y));
        return true;
    }

    public boolean moveEntity(Entity entity, Direction direction) {
        if (entities[entity.position().y + direction.y()][entity.position().x + direction.x()] instanceof Unwalkable || tiles[entity.position().y + direction.y()][entity.position().x + direction.x()] instanceof Unwalkable) {
            return false;
        }
        entities[entity.position().y][entity.position().x] = null;
        entities[entity.position().y + direction.y()][entity.position().x + direction.x()] = entity;
        entity.move(new Point(entity.position().x + direction.x(), entity.position().y + direction.y()));
        return true;
    }

    public void placeEntity(Entity entity) {
        entities[entity.position().y][entity.position().x] = entity;
    }

    public Tile[][] getTiles() { return tiles; }

    public Entity[][] getEntities() { return entities; }

    static public Dungeon getDungeon() {
        return dungeon;
    }
}