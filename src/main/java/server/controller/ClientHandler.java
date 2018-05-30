package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.elements.Element;
import model.elements.mechanisms.Button;
import model.elements.mechanisms.Mechanism;
import protocol.*;
import model.elements.entities.Entities;
import model.elements.entities.Entity;
import utils.JsonObjectMapper;
import model.Dungeon;
import model.elements.entities.Hero;
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
                    ArrayList<Hero> heroes = GameServer.getServer().getHeroes();
                    for (Hero hero : heroes) {
                        if (hero != this.hero) {
                            InitHeroResponse heroResponse = new InitHeroResponse();
                            heroResponse.setId(hero.getId());
                            heroResponse.setPosition(hero.position());
                            writer.println(HeroProtocol.INIT_HERO + '\n' + JsonObjectMapper.toJson(heroResponse));
                            writer.flush();
                        }
                    }
                    InitHeroResponse initResponse = new InitHeroResponse();
                    initResponse.setId(hero.getId());
                    initResponse.setPosition(hero.position());
                    GameServer.getServer().notifyClients(this, HeroProtocol.INIT_HERO + "\n" + JsonObjectMapper.toJson(initResponse));
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
        GameServer.getServer().notifyClients(AttackProtocol.BEGIN_ATTACK + "\n" + JsonObjectMapper.toJson(attackMessage));
    }

    private void hurt(Direction direction) {
        HurtCommandResponse hurtResponse;
        hurtResponse = new HurtCommandResponse();
        hurtResponse.setDamage(hero.getDamage());
        hurtEntities(hurtResponse, direction);
    }

    private void move(Direction direction) throws JsonProcessingException {
        MoveCommandResponse moveMessage;
        Element nextElement = Dungeon.getDungeon().getElement(new Point(hero.position().x + direction.x(), hero.position().y + direction.y()));
        if (nextElement instanceof Button) {
            ((Button) nextElement).activate();
            GameServer.getServer().notifyClients(GameProtocol.BUTTON + '\n' + ((Button) nextElement).getId());
        }
        if (Dungeon.getDungeon().moveEntity(hero, direction)) {
            moveMessage = new MoveCommandResponse();
            moveMessage.setId(hero.getId());
            moveMessage.setDirection(direction);
            GameServer.getServer().notifyClients(MoveProtocol.MOVE_RESPONSE + "\n" + JsonObjectMapper.toJson(moveMessage));
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
                    try {
                        GameServer.getServer().notifyClients(AttackProtocol.HURT_RESPONSE + "\n" + JsonObjectMapper.toJson(hurtMessage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
            }
        }).start();
    }

    public Hero getHero() {
        return hero;
    }
}
