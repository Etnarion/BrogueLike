package client.controller;

import model.Dungeon;
import model.entities.Entity;
import model.entities.Hero;
import model.entities.Spider;
import utils.JsonObjectMapper;
import client.view.DungeonView;
import protocol.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientController implements Runnable {
    BufferedReader in;
    PrintWriter out;

    public ClientController(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        Dungeon.getDungeon();
        DungeonView.getDungeonView();
    }

    private void listen() throws IOException, InterruptedException {
        String line;
        while (true) {
            line = in.readLine();
            switch (line) {
                case AttackProtocol.ATTACK_RESPONSE :
                    synchronized (this) {
                        AttackCommandResponse response = JsonObjectMapper.parseJson(in.readLine(), AttackCommandResponse.class);
                        Dungeon dungeon = Dungeon.getDungeon();
                        Entity entity = dungeon.getEntity(response.getId());
                        for (Integer id : response.getHurtEntities()) {
                            Dungeon.getDungeon().getEntity(id).hurt(response.getDamage());
                        }
                        DungeonView.getDungeonView().attack(entity, response.getDirection(), response.getRange());
                    }
                    break;
                case MoveProtocol.MOVE_RESPONSE :
                    synchronized (this) {
                        MoveCommandResponse response = JsonObjectMapper.parseJson(in.readLine(), MoveCommandResponse.class);
                        Dungeon dungeon = Dungeon.getDungeon();
                        Entity entity = dungeon.getEntity(response.getId());
                        Point prevPos = entity.position();
                        dungeon.moveEntity(entity, response.getDirection());
                        DungeonView.getDungeonView().move(entity, prevPos);
                    }
                    break;
                case EntityProtocol.INIT_ENTITY :
                    InitEntityResponse initResponse = JsonObjectMapper.parseJson(in.readLine(), InitEntityResponse.class);
                    Entity newEntity = null;
                    switch (initResponse.getEntity()) {
                        case HERO:
                            newEntity = new Hero(initResponse.getPosition(), initResponse.getId());
                            break;
                        case SPIDER:
                            newEntity = new Spider(initResponse.getPosition(), initResponse.getId());
                            break;
                        default:
                            break;
                    }
                    Dungeon.getDungeon().placeEntity(newEntity);
                    DungeonView.getDungeonView().displayEntity(newEntity);
            }
        }
    }

    public void run() {
        try {
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
