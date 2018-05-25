package server.controller;

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
        boolean done = false;
        while (true) {
            MoveCommandResponse moveMessage;
            AttackCommandResponse attackMessage;
            ArrayList<Integer> hurtEntities;
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
                    if (Dungeon.getDungeon().moveEntity(hero, Direction.LEFT)) {
                        moveMessage = new MoveCommandResponse();
                        moveMessage.setId(hero.getId());
                        moveMessage.setDirection(Direction.LEFT);
                        synchronized (this) {
                            GameServer.getServer().notifyClients(MoveProtocol.MOVE_RESPONSE);
                            GameServer.getServer().notifyClients(JsonObjectMapper.toJson(moveMessage));
                        }
                    }
                    break;
                case MoveProtocol.DOWN :
                    if (Dungeon.getDungeon().moveEntity(hero, Direction.DOWN)) {
                        moveMessage = new MoveCommandResponse();
                        moveMessage.setId(hero.getId());
                        moveMessage.setDirection(Direction.DOWN);
                        GameServer.getServer().notifyClients(MoveProtocol.MOVE_RESPONSE);
                        GameServer.getServer().notifyClients(JsonObjectMapper.toJson(moveMessage));
                    }
                    break;
                case MoveProtocol.UP :
                    if (Dungeon.getDungeon().moveEntity(hero, Direction.UP)) {
                        moveMessage = new MoveCommandResponse();
                        moveMessage.setId(hero.getId());
                        moveMessage.setDirection(Direction.UP);
                        GameServer.getServer().notifyClients(MoveProtocol.MOVE_RESPONSE);
                        GameServer.getServer().notifyClients(JsonObjectMapper.toJson(moveMessage));
                    }
                    break;
                case MoveProtocol.RIGHT :
                    if (Dungeon.getDungeon().moveEntity(hero, Direction.RIGHT)) {
                        moveMessage = new MoveCommandResponse();
                        moveMessage.setId(hero.getId());
                        moveMessage.setDirection(Direction.RIGHT);
                        GameServer.getServer().notifyClients(MoveProtocol.MOVE_RESPONSE);
                        GameServer.getServer().notifyClients(JsonObjectMapper.toJson(moveMessage));
                    }
                    break;
                case AttackProtocol.ATTACK_DOWN :
                    attackMessage = new AttackCommandResponse();
                    attackMessage.setDirection(Direction.DOWN);
                    attackMessage.setId(hero.getId());
                    attackMessage.setDamage(hero.getStrength());
                    attackMessage.setRange(3);
                    hurtEntities = hurtEntities(attackMessage);
                    attackMessage.setHurtEntities(hurtEntities);
                    GameServer.getServer().notifyClients(AttackProtocol.ATTACK_RESPONSE);
                    GameServer.getServer().notifyClients(JsonObjectMapper.toJson(attackMessage));
                    break;
                case AttackProtocol.ATTACK_LEFT :
                    attackMessage = new AttackCommandResponse();
                    attackMessage.setDirection(Direction.LEFT);
                    attackMessage.setId(hero.getId());
                    attackMessage.setDamage(hero.getStrength());
                    attackMessage.setRange(3);
                    hurtEntities = hurtEntities(attackMessage);
                    attackMessage.setHurtEntities(hurtEntities);
                    GameServer.getServer().notifyClients(AttackProtocol.ATTACK_RESPONSE);
                    GameServer.getServer().notifyClients(JsonObjectMapper.toJson(attackMessage));
                    break;
                case AttackProtocol.ATTACK_RIGHT :
                    attackMessage = new AttackCommandResponse();
                    attackMessage.setDirection(Direction.RIGHT);
                    attackMessage.setId(hero.getId());
                    attackMessage.setDamage(hero.getStrength());
                    attackMessage.setRange(3);
                    hurtEntities = hurtEntities(attackMessage);
                    attackMessage.setHurtEntities(hurtEntities);
                    GameServer.getServer().notifyClients(AttackProtocol.ATTACK_RESPONSE);
                    GameServer.getServer().notifyClients(JsonObjectMapper.toJson(attackMessage));
                    break;
                case AttackProtocol.ATTACK_UP :
                    attackMessage = new AttackCommandResponse();
                    attackMessage.setDirection(Direction.UP);
                    attackMessage.setId(hero.getId());
                    attackMessage.setDamage(hero.getStrength());
                    attackMessage.setRange(3);
                    hurtEntities = hurtEntities(attackMessage);
                    attackMessage.setHurtEntities(hurtEntities);
                    GameServer.getServer().notifyClients(AttackProtocol.ATTACK_RESPONSE);
                    GameServer.getServer().notifyClients(JsonObjectMapper.toJson(attackMessage));
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<Integer> hurtEntities(AttackCommandResponse attackMessage) {
        ArrayList hurtEntities = new ArrayList<>();
        for (int i = 0; i < attackMessage.getRange(); i++) {
            Point position = new Point(hero.position().x +i*attackMessage.getDirection().x(), hero.position().y + attackMessage.getDirection().y() +i*attackMessage.getDirection().y());
            if (position.x < Dungeon.DUNGEON_SIZE && position.x >= 0 && position.y < Dungeon.DUNGEON_SIZE && position.y >= 0) {
                Entity entityToHurt = Dungeon.getDungeon().getEntity(position);
                if (entityToHurt != null) {
                    entityToHurt.hurt(attackMessage.getDamage());
                    hurtEntities.add(entityToHurt.getId());
                }
            }
        }
        return hurtEntities;
    }
}
