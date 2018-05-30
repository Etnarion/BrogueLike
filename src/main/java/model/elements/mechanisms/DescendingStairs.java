package model.elements.mechanisms;

import java.awt.*;

public class DescendingStairs extends Mechanism {

    public DescendingStairs(Point position) {
        super(position);
        symbol = '<';
        walkable = true;
        color = Color.BLACK;
    }

    @Override
    public boolean activate() {
        return true;
    }
}
