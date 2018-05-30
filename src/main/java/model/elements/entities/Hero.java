package model.elements.entities;

import model.Dungeon;
import model.elements.mechanisms.Mechanism;
import model.elements.weapons.Gun;
import model.elements.weapons.Weapon;
import utils.ExtendedAscii;

import java.awt.*;


public class Hero extends Entity {
    private int gold;

    private Weapon weapon;

    public Hero(Point position, int id) {
        super(position, id);
        symbol = 'X';
        gold = 0;
        weapon = new Gun(4, 3, 1);
        color = Color.DARK_GRAY;
        fontColor = Color.ORANGE;
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

    @Override
    public void die() {
        super.die();
        symbol = ExtendedAscii.getAscii(157);
    }
}
