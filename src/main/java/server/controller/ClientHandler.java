package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import protocol.*;
import model.entities.Entities;
import model.entities.Entity;
import model.entities.Spider;
import utils.JsonObjectMapper;
import model.Dungeon;
import model.entities.Hero;
import utils.Direction;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ClientHandler implements IClientHandler {
    private Hero hero;

    public ClientHandler(int id) {
        hero = new Hero(new Point(4+id, 4), id);
    }

    @Override
    public void handleClientConnection(InputStream is, OutputStream os) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));

        String command;
        while (true) {
            command = reader.readLine();
            switch (command.toUpperCase()) {
                case GameProtocol.HELLO :
                    Dungeon.getDungeon().initHero(hero);
                    writer.println(hero.getId());
                    writer.flush();
                    for (Entity entities[] : Dungeon.getDungeon().getEntities()) {
                        for (Entity ent : entities) {
                            if (ent != null) {
                                InitEntityResponse initResponse = new InitEntityResponse();
                                initResponse.setId(ent.getId());
                                initResponse.setPosition(ent.position());
                                if (ent instanceof Spider)
                                    initResponse.setEntity(Entities.SPIDER);
                                else if (ent instanceof Hero)
                                    initResponse.setEntity(Entities.HERO);
                                writer.println(EntityProtocol.INIT_ENTITY);
                                writer.println(JsonObjectMapper.toJson(initResponse));
                                writer.flush();
                            }
                        }
                    }
                    InitEntityResponse initResponse = new InitEntityResponse();
                    initResponse.setId(hero.getId());
                    initResponse.setPosition(hero.position());
                    initResponse.setEntity(Entities.HERO);
                    GameServer.getServer().notifyClients(EntityProtocol.INIT_ENTITY);
                    GameServer.getServer().notifyClients(JsonObjectMapper.toJson(initResponse));
                    break;
                case MoveProtocol.LEFT :
                    move(Direction.LEFT);
                    break;
                case MoveProtocol.DOWN :
                    move(Direction.DOWN);
                    break;
                case MoveProtocol.UP :
                    move(Direction.UP);
                    break;
                case MoveProtocol.RIGHT :
                    move(Direction.RIGHT);
                    break;
                case AttackProtocol.ATTACK_DOWN :
                    attack(Direction.DOWN);
                    hurt(Direction.DOWN);
                    break;
                case AttackProtocol.ATTACK_LEFT :
                    attack(Direction.LEFT);
                    hurt(Direction.LEFT);
                    break;
                case AttackProtocol.ATTACK_RIGHT :
                    attack(Direction.RIGHT);
                    hurt(Direction.RIGHT);
                    break;
                case AttackProtocol.ATTACK_UP :
                    attack(Direction.UP);
                    hurt(Direction.UP);
                    break;
                default:
                    break;
            }
        }
    }

    private void attack(Direction direction) throws JsonProcessingException {
        AttackCommandResponse attackMessage = new AttackCommandResponse();
        attackMessage.setId(hero.getId());
        attackMessage.setDirection(direction);
        attackMessage.setRange(hero.getRange());
        GameServer.getServer().notifyClients(AttackProtocol.BEGIN_ATTACK);
        GameServer.getServer().notifyClients(JsonObjectMapper.toJson(attackMessage));
    }

    private void hurt(Direction direction) {
        HurtCommandResponse hurtResponse;
        hurtResponse = new HurtCommandResponse();
        hurtResponse.setDamage(hero.getDamage());
        hurtEntities(hurtResponse, direction);
    }

    private void move(Direction direction) throws JsonProcessingException {
        MoveCommandResponse moveMessage;
        if (Dungeon.getDungeon().moveEntity(hero, direction)) {
            moveMessage = new MoveCommandResponse();
            moveMessage.setId(hero.getId());
            moveMessage.setDirection(direction);
            synchronized (this) {
                GameServer.getServer().notifyClients(MoveProtocol.MOVE_RESPONSE);
                GameServer.getServer().notifyClients(JsonObjectMapper.toJson(moveMessage));
            }
        }
    }

    private void hurtEntities(HurtCommandResponse hurtMessage, Direction direction) {
        new Thread(() -> {
            for (int i = 1; i <= hero.getRange(); i++) {
                Point position = new Point(hero.position().x + i * direction.x(), hero.position().y + direction.y() + i * direction.y());
                if (position.x < Dungeon.DUNGEON_SIZE && position.x >= 0 && position.y < Dungeon.DUNGEON_SIZE && position.y >= 0) {
                    Entity entityToHurt = Dungeon.getDungeon().getEntity(position);
                    if (entityToHurt != null) {
                        entityToHurt.hurt(hero.getDamage());
                        hurtMessage.setEntityId(entityToHurt.getId());
                    } else
                        hurtMessage.setEntityId(-1);
                    synchronized (this) {
                        GameServer.getServer().notifyClients(AttackProtocol.HURT_RESPONSE);
                        try {
                            GameServer.getServer().notifyClients(JsonObjectMapper.toJson(hurtMessage));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
            }
        }).start();
    }
}
