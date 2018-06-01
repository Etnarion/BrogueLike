package model.elements.items.consumable;

import utils.ExtendedAscii;

import java.awt.*;

public class Heart extends Consumable {
    public Heart(Point position) {
        super(position);
        symbol = ExtendedAscii.getAscii(230);
    }
}
