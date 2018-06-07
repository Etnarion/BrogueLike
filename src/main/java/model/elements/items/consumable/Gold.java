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

    public void pickup(Point position) {
        super.pickup(position);
        Hero hero = (Hero)Dungeon.getDungeon().getEntity(position);
        hero.setGold(hero.getGold() + amount);
        Dungeon.getDungeon().removeItem(this);
    }
}
