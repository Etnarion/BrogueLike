package model.elements.entities;

import com.googlecode.lanterna.TextColor;
import model.elements.items.weapons.Gun;
import model.elements.items.weapons.Sword;
import model.elements.items.weapons.Weapon;
import utils.ExtendedAscii;

import java.awt.*;


public class Hero extends Entity {
    private int gold;

    private Weapon weapon;

    public Hero(Point position, int id) {
        super(position, id);
        symbol = 'X';
        gold = 0;
        weapon = new Sword(position, 1, 1, 1);
        fontColor = new TextColor.RGB(255, 178, 0);
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

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public void move(Point nextPos) {
        super.move(nextPos);
        weapon.setPosition(nextPos);
    }

    @Override
    public void die() {
        super.die();
        symbol = ExtendedAscii.getAscii(157);
    }
}
