package model.elements.tiles;

import java.awt.*;

public class Ground extends Tile {
    public Ground (Point position) {
        super(position);
        symbol = '.';
        color = Color.DARK_GRAY;
        walkable = true;
    }
}
