package model.elements;

import java.awt.*;

public abstract class Element {
    protected Point position;
    public char symbol;
    protected boolean walkable;
    public Color color;
    public Color fontColor;

    public Element(Point position) {
        this.position = position;
        fontColor = Color.WHITE;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public Point position() {
        return position;
    }
}
