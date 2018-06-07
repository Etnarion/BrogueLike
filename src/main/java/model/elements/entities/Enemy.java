package model.elements.entities;

import java.awt.*;

abstract public class Enemy extends Entity {
	protected int vision;

	public Enemy (Point position, int id) {
	    super(position, id);
	    walkable = false;
	}

    public int getVision() {
        return vision;
    }
}
