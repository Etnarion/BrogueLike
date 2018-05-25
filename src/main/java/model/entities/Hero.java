package model.entities;

import weapons.Gun;
import weapons.Weapon;

import java.awt.*;


public class Hero extends Entity {
    private int gold;

    private Weapon weapon;

    public Hero(Point position, int id) {
        super(position, id);
        symbol = 'X';
        gold = 0;
        weapon = new Gun(10, 7, 1);
    }

    public int getRange() {
        if (weapon != null)
            return weapon.getRange();
        else
            return baseRange;
    }

    public int getDamage() {
        return getStrength() + (weapon == null ? 0 : weapon.getDamage());
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
