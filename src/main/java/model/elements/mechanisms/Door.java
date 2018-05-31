package model.elements.mechanisms;

import client.view.ViewUtils;
import com.googlecode.lanterna.TextColor;

import java.awt.*;

public class Door extends Mechanism {
    public Door(Point position) {
        super(position);
        symbol = ' ';
        walkable = false;
        color = new TextColor.RGB(112, 75, 28);
    }

    @Override
    public boolean activate() {
        color = ViewUtils.DEFAULT_COLOR;
        walkable = !walkable;
        return true;
    }
}
