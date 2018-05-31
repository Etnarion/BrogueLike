package model.elements.tiles;

import com.googlecode.lanterna.TextColor;

import java.awt.*;

public class ShallowWater extends Tile {
    public ShallowWater (Point position) {
        super(position);
        symbol = '~';
        color = new TextColor.RGB(65,105,225);
        walkable = true;
    }
}