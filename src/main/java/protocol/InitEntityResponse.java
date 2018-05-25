package protocol;

import model.entities.Entities;

import java.awt.*;

public class InitEntityResponse {
    Point position;
    int id;
    Entities entity;


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

    public Entities getEntity() {
        return entity;
    }

    public void setEntity(Entities entity) {
        this.entity = entity;
    }
}
