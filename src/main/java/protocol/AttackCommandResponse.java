package protocol;

import utils.Direction;

import java.util.ArrayList;
import java.util.List;

public class AttackCommandResponse {
    private  int id;
    Direction direction;
    int range;
    int damage;
    List<Integer> hurtEntities = new ArrayList<>();


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public List<Integer> getHurtEntities() {
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(hurtEntities);
        return list;
    }

    public void setHurtEntities(List<Integer> hurtEntities) {
        hurtEntities.clear();
        this.hurtEntities.addAll(hurtEntities);
    }
}
