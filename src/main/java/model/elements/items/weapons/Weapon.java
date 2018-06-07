package model.elements.items.weapons;

import model.Dungeon;
import model.elements.entities.Hero;
import model.elements.items.Item;

import java.awt.*;

public abstract class Weapon extends Item {
    protected int damage;
    protected int range;
    protected int area;
    private String name;

    public Weapon(Point position, int damage, int range, int area) {
        super(position);
        this.damage = damage;
        this.range = range;
        this.area = area;
        name = getClass().getSimpleName() + "+" + damage;
    }

    public void pickup(Point position) {
        super.pickup(position);
        Hero hero = (Hero)Dungeon.getDungeon().getEntity(position);
        Weapon oldWeapon = hero.getWeapon();
        hero.setWeapon(this);
        oldWeapon.drop();
    }

    private void drop() {
        Dungeon.getDungeon().placeItem(this);
    }

    public String getName() {
        return name;
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

    public void setPosition(Point position) {
        this.position = position;
    }
}
