package model.elements.entities;

import model.Dungeon;
import model.elements.Element;
import model.elements.tiles.Tile;

import java.awt.*;

public abstract class Entity extends Element {
    protected boolean walkable;
    private int id;

    private int maxHealth;
    private int health;
    private int strength;
    protected int baseRange;
    private boolean alive;

    public Entity (Point position, int id) {
        super(position);
        this.id = id;
        maxHealth = 20;
        health = 20;
        strength = 1;
        baseRange = 1;
        alive = true;
        walkable = false;
    }

    public int getId() {
        return id;
    }



    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }


    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void move(Point nextPos) {
        position = nextPos;
    }

    public void hurt(int damage) {
        if (isAlive()) {
            health -= damage;
            if (health <= 0) {
                die();
            }
        }
    }

    public void die() {
        alive = false;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean isAlive() {
        return alive;
    }

    public Tile tile() {
        return Dungeon.getDungeon().getTile(position);
    }
}
