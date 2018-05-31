package model.elements.mechanisms;

import com.googlecode.lanterna.TextColor;

import java.awt.*;

public class DescendingStairs extends Mechanism {

    public DescendingStairs(Point position) {
        super(position);
        symbol = '<';
        walkable = true;
        color = TextColor.ANSI.BLACK;
    }

    @Override
    public boolean activate() {
        return true;
    }
}
