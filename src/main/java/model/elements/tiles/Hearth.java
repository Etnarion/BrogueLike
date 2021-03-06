package model.elements.tiles;

import com.googlecode.lanterna.TextColor;
import utils.ExtendedAscii;

import java.awt.*;

public class Hearth extends Tile {
    public Hearth (Point position) {
        super(position);
        symbol = ExtendedAscii.getAscii(145);
        color = new TextColor.RGB(245, 0, 0);
        walkable = true;
    }

}