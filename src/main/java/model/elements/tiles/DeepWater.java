package model.elements.tiles;

import com.googlecode.lanterna.TextColor;

import java.awt.*;

public class DeepWater extends Tile {
    public DeepWater (Point position) {
        super(position);
        symbol = '~';
        color = new TextColor.RGB(0,13,146);
        walkable = false;
    }
}