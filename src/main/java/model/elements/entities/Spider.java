package model.elements.entities;

import java.awt.*;

public class Spider extends Enemy {
    public Spider(Point position, int id) {
        super(position, id);
        symbol = 'S';
        color = Color.DARK_GRAY;
    }

    @Override
    public void die() {
        super.die();
        symbol = '$';
    }
}
