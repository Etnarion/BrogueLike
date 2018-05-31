package model.elements.tiles;

import client.view.ViewUtils;

import java.awt.*;

public class Ground extends Tile {
    public Ground (Point position) {
        super(position);
        symbol = '.';
        color = ViewUtils.DEFAULT_COLOR;
        walkable = true;
    }
}
