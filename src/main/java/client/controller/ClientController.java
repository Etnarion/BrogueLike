package client.controller;

import client.view.HeroView;
import com.fasterxml.jackson.core.JsonParseException;
import jdk.nashorn.internal.codegen.DumpBytecode;
import model.Dungeon;
import model.elements.entities.Entity;
import model.elements.entities.Hero;
import model.elements.items.Item;
import model.elements.mechanisms.Button;
import model.elements.mechanisms.Mechanism;
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

    public ClientController(BufferedReader in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
        Dungeon.getDungeon();
        DungeonView.getDungeonView().showMap();
        HeroView.getHeroView().showStatus();
    }



    private void listen() throws IOException, InterruptedException {
        String line;
        while (true) {
            line = in.readLine();
            switch (line) {
                case AttackProtocol.BEGIN_ATTACK :
                    synchronized (this) {
                        AttackCommandResponse response;
                        try {
                            response = JsonObjectMapper.parseJson(in.readLine(), AttackCommandResponse.class);
                        } catch (JsonParseException e) {
                            System.out.println(e.getMessage());
                            break;
                        }

                        Dungeon dungeon = Dungeon.getDungeon();
                        Entity entity = dungeon.getEntity(response.getId());
                        DungeonView.getDungeonView().attack(entity, response.getDirection(), response.getRange());
                    }
                    break;

                case AttackProtocol.HURT_RESPONSE :
                    synchronized (this) {
                        HurtCommandResponse hurtResponse;
                        try {
                            hurtResponse = JsonObjectMapper.parseJson(in.readLine(), HurtCommandResponse.class);
                        } catch (JsonParseException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                        if (hurtResponse.getEntityId() != -1) {
                            Dungeon.getDungeon().getEntity(hurtResponse.getEntityId()).hurt(hurtResponse.getDamage());
                            if (hurtResponse.getEntityId() == Dungeon.getDungeon().getHero().getId()) {
                                HeroView.getHeroView().showHealth();
                            }
                        }
                    }
                    break;
                case MoveProtocol.MOVE_RESPONSE :
                    synchronized (this) {
                        MoveCommandResponse response;
                        try {
                            response = JsonObjectMapper.parseJson(in.readLine(), MoveCommandResponse.class);
                        } catch (JsonParseException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                        Dungeon dungeon = Dungeon.getDungeon();
                        Entity entity = dungeon.getEntity(response.getId());
                        Point prevPos = entity.position();
                        dungeon.moveEntity(entity, response.getDirection());
                        DungeonView.getDungeonView().move(entity, prevPos);
                    }
                    break;
                case HeroProtocol.INIT_HERO :
                    InitHeroResponse initResponse = JsonObjectMapper.parseJson(in.readLine(), InitHeroResponse.class);
                    if (initResponse.getId() != Dungeon.getDungeon().getHero().getId()) {
                        Dungeon.getDungeon().initHero(initResponse.getPosition(), initResponse.getId());
                    }
                    break;
                case GameProtocol.BUTTON :
                    Button button = (Button)Dungeon.getDungeon().getMechanism(Integer.valueOf(in.readLine()));
                    if (button != null) {
                        button.activate();
                        for (Mechanism mechanism : button.getLinkedMechanisms()) {
                            DungeonView.getDungeonView().displayElement(mechanism.position());
                        }
                    }
                    break;
                case GameProtocol.LOOT :
                    synchronized (this) {
                        LootResponse lootResponse = JsonObjectMapper.parseJson(in.readLine(), LootResponse.class);
                        Hero lootHero = Dungeon.getDungeon().getHero(lootResponse.getHeroId());
                        Item item = Dungeon.getDungeon().getItem(lootResponse.getItemId());
                        if (item != null) {
                            item.pickup(lootHero);
                            HeroView.getHeroView().showStatus();
                        }
                    }
                    break;
                case GameProtocol.NEW_LEVEL :
                    synchronized (Dungeon.getDungeon()) {
                        Dungeon dungeon = Dungeon.getDungeon();
                        dungeon.loadNewMap();
                        DungeonView dungeonView = DungeonView.getDungeonView();
                        dungeonView.showMap();
                        for (Hero hero : Dungeon.getDungeon().getHeroes()) {
                            hero.setPosition(new Point(2+hero.getId(), 2));
                            dungeon.placeEntity(hero);
                            dungeonView.displayEntity(hero);
                        }
                        HeroView.getHeroView().showStatus();
                    }
                    break;
                default:
                    break;

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
