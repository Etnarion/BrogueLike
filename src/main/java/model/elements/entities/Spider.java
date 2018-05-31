package model.elements.entities;

import client.view.ViewUtils;
import com.googlecode.lanterna.TextColor;

import java.awt.*;

public class Spider extends Enemy {
    public Spider(Point position, int id) {
        super(position, id);
        symbol = 'S';
        fontColor = new TextColor.RGB(79, 141, 142);
        color = ViewUtils.DEFAULT_COLOR;
    }

    @Override
    public void die() {
        super.die();
        symbol = '$';
    }
}
