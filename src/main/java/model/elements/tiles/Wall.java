package model.elements.tiles;

import utils.ExtendedAscii;

import java.awt.*;

public class Wall extends Tile {
    public Wall (Point position) {
        super(position);
        symbol = ExtendedAscii.getAscii(177);
        color = new Color(41, 56, 61);
        walkable = false;
    }

}
