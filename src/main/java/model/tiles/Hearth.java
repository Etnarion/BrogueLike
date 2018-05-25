package model.tiles;

import utils.ExtendedAscii;

import java.awt.*;

public class Hearth extends Tile implements Unwalkable {
    public Hearth () {
        symbol = ExtendedAscii.getAscii(145);
        color = new Color(245, 0, 0);
    }

}