package model.elements.items.consumable;

import java.awt.*;

public class Gold extends Consumable {
    int amount;
    public Gold(Point position, int amount) {
        super(position);

        this.amount = amount;
    }
}
