package model.elements.mechanisms;

import java.awt.*;

public class Door extends Mechanism {
    public Door(Point position) {
        super(position);
        symbol = ' ';
        walkable = false;
        color = new Color(112, 75, 28);
    }

    @Override
    public boolean activate() {
        color = Color.DARK_GRAY;
        walkable = !walkable;
        return true;
    }
}
