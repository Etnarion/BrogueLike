package model.entities;

import model.Dungeon;
import model.tiles.Tile;
import model.tiles.Unwalkable;

import java.awt.*;

public abstract class Entity implements Unwalkable {
    private int id;
    public char symbol;
    private int maxHealth;
    private int health;
    private int strength;
    protected int baseRange;
    protected Point position;
    private Tile tile;

    public Entity (Point position, int id) {
        this.id = id;
        this.position = position;
        this.tile = Dungeon.getDungeon().getTile(position);
        maxHealth = 20;
        health = 20;
        strength = 1;
        baseRange = 1;
    }

    public int getId() {
        return id;
    }

    public Point position() {
        return position;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void move(Point nextPos) {
        position = nextPos;
        tile = Dungeon.getDungeon().getTile(position);
    }

    public void hurt(int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void die() {

    }

    public Tile tile() {
        return tile;
    }
}
