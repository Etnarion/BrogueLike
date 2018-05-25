package utils;

import java.awt.*;

public enum Direction {
    LEFT(new Point(-1,0)),
    UP(new Point(0,-1)),
    RIGHT(new Point(1,0)),
    DOWN(new Point(0,1));
    private Point direction;

    Direction(Point direction) {
        this.direction = direction;
    }

    Direction(int x, int y) {
        direction.x = x;
        direction.y = y;
    }

    public int x () {
        return direction.x;
    }

    public int y () {
        return direction.y;
    }
}