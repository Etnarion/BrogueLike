package protocol;

import java.awt.*;

public class InitHeroResponse {
    Point position;
    int id;


    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
