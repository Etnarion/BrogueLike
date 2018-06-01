package model.elements.items.weapons;

import model.elements.items.Item;

import java.awt.*;

public abstract class Weapon extends Item {
    protected int damage;
    protected int range;
    protected int area;

    public Weapon(Point position, int damage, int range, int area) {
        super(position);
        this.damage = damage;
        this.range = range;
        this.area = area;
    }

    public int getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public int getArea() {
        return area;
    }
}
