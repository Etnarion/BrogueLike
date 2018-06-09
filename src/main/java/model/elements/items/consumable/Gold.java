package model.elements.items.consumable;

import com.googlecode.lanterna.TextColor;
import model.Dungeon;
import model.elements.entities.Hero;

import java.awt.*;

public class Gold extends Consumable {
    int amount;
    public Gold(Point position, int amount) {
        super(position);
        symbol = '$';
        fontColor = new TextColor.RGB(255, 178, 0);
        this.amount = amount;
    }

    public void pickup(Hero hero) {
        super.pickup(hero);
        hero.setGold(hero.getGold() + amount);
        Dungeon.getDungeon().removeItem(this);
    }
}
