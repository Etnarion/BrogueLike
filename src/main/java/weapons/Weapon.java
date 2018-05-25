package weapons;

public abstract class Weapon {
    protected int damage;
    protected int range;
    protected int area;

    public Weapon(int damage, int range, int area) {
        this.damage = damage;
        this.range = range;
        this.area = area;
    }

    public int getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public int getArea() {
        return area;
    }
}
