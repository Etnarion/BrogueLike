package model.elements.tiles;

import java.awt.*;

public class ShallowWater extends Tile {
    public ShallowWater (Point position) {
        super(position);
        symbol = '~';
        color = new Color(65,105,225);
        walkable = true;
    }
}