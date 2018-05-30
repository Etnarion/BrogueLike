package model.elements.mechanisms;

import model.elements.Element;

import java.awt.*;

public abstract class Mechanism extends Element {
    private static int nbMechanism = 0;

    private int id;

    public Mechanism(Point position) {
        super(position);
        id = nbMechanism++;
    }

    abstract public boolean activate();

    public boolean isWalkable() {
        return walkable;
    }

    public int getId() {
        return id;
    }
}
