package model.tiles;

import java.awt.*;

public class DeepWater extends Tile implements Unwalkable {
    public DeepWater () {
        symbol = '~';
        color = new Color(0,13,146);
    }
}