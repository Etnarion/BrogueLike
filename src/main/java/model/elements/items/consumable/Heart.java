package model.elements.items.consumable;

import com.googlecode.lanterna.TextColor;
import model.Dungeon;
import model.elements.entities.Hero;
import utils.ExtendedAscii;

import java.awt.*;

public class Heart extends Consumable {
    int amount;
    public Heart(Point position, int amount) {
        super(position);
        this.amount = amount;
        fontColor = TextColor.ANSI.RED;
        symbol = '@';
    }

    public void pickup(Point position) {
        super.pickup(position);
        Hero hero = (Hero)Dungeon.getDungeon().getEntity(position);
        hero.setHealth(hero.getHealth() + amount);
    }
}
