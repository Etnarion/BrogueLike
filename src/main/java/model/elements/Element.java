package model.elements;

import client.view.ViewUtils;
import com.googlecode.lanterna.TextColor;

import java.awt.*;

public abstract class Element {
    protected Point position;
    public char symbol;
    protected boolean walkable;
    public TextColor color;
    public TextColor fontColor;

    public Element(Point position) {
        this.position = position;
        color = ViewUtils.DEFAULT_COLOR;
        fontColor = TextColor.ANSI.WHITE;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public Point position() {
        return position;
    }
}
