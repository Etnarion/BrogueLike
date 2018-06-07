package model.elements.mechanisms;

import com.googlecode.lanterna.TextColor;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Mechanism {
    private ArrayList<Mechanism> linkedMechanisms;
    private boolean activated;

    public Button(Point position) {
        super(position);
        linkedMechanisms = new ArrayList<>();
        symbol = 'o';
        walkable = true;
        activated = false;
        color = new TextColor.RGB(48, 48, 48);
    }

    public void linkElement(Mechanism mechanism) {
        linkedMechanisms.add(mechanism);
    }

    @Override
    public boolean activate() {
        if (!activated) {
            activated = true;
            symbol = '0';
            for (Mechanism mechanism : linkedMechanisms) {
                mechanism.activate();
            }
            return true;
        }
        return false;
    }

    public ArrayList<Mechanism> getLinkedMechanisms() {
        return linkedMechanisms;
    }
}
