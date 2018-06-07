package model.elements.items;

import model.Dungeon;
import model.elements.Element;

import java.awt.*;

public abstract class Item extends Element {
    private static int nbItems = 0;

    private int id;

    public Item(Point position) {
        super(position);
        id = nbItems++;
        walkable = true;
    }

    public void pickup(Point position) {
        Dungeon.getDungeon().removeItem(this);
    }

    public int getId() {
        return id;
    }
}
