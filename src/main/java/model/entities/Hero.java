package model.entities;

import java.awt.*;


public class Hero extends Entity {
    private int gold;

    public Hero(Point position, int id) {
        super(position, id);
        symbol = 'X';
        gold = 0;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
