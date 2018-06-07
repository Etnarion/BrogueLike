package model.elements.items.weapons;

import com.googlecode.lanterna.TextColor;

import java.awt.*;

public class Sword extends Weapon {

    public Sword(Point position, int damage, int range, int area) {
        super(position, damage, range, area);
        symbol = '/';
        fontColor = new TextColor.RGB(178, 238, 255);
    }
}
