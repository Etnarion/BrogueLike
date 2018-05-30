package model.elements.tiles;

import model.elements.Element;

import java.awt.*;

public abstract class Tile extends Element {
    public Tile(Point position) {
        super(position);
    }

    public boolean isWalkable() {
        return walkable;
    }
}
