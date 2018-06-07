package model.elements.entities;

import client.view.ViewUtils;
import com.googlecode.lanterna.TextColor;
import model.elements.items.consumable.Gold;
import model.elements.items.weapons.Sword;

import java.awt.*;

public class Spider extends Enemy {
    public Spider(Point position, int id) {
        super(position, id);
        symbol = 'S';
        fontColor = new TextColor.RGB(79, 141, 142);
        color = ViewUtils.DEFAULT_COLOR;
        loot.add(new Gold(position, 30));
        loot.add(new Sword(position, 2, 1, 1));
        vision = 6;
    }

    @Override
    public void die() {
        super.die();
        symbol = '$';
    }
}
