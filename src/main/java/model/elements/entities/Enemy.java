package model.elements.entities;

import java.awt.*;

abstract public class Enemy extends Entity {

	public Enemy (Point position, int id) {
	    super(position, id);
	    walkable = false;
	}
}
