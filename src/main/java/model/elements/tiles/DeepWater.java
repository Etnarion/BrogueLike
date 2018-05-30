package model.elements.tiles;

import java.awt.*;

public class DeepWater extends Tile {
    public DeepWater (Point position) {
        super(position);
        symbol = '~';
        color = new Color(0,13,146);
        walkable = false;
    }
}