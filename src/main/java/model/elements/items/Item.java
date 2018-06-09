package model.elements.items;

import model.Dungeon;
import model.elements.Element;
import model.elements.entities.Hero;

import java.awt.*;

public abstract class Item extends Element {
    private static int nbItems = 5000;

    private int id;

    public Item(Point position) {
        super(position);
        id = nbItems++;
        walkable = true;
    }

    public void pickup(Hero hero) {
        Dungeon.getDungeon().removeItem(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
