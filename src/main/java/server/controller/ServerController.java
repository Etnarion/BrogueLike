/*
package server.controller;
import server.GameServer;
import server.model.Dungeon;
import server.model.DungeonGraph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

public class ServerController {
    private Dungeon dungeon;
    private Hero hero;
    private LinkedList<Enemy> enemies;
    private GameServer srv;

    public ServerController() throws IOException {
        srv = new GameServer(3030);
        srv.connect();
        dungeon = new Dungeon();
        hero = new Hero(new Point(20,4), dungeon);
        initEnemies();
    }

    public void initGame() throws IOException {
        dungeon.placeEntity(hero);
        initEnemies();
        for(Enemy enemy : enemies) {
            dungeon.placeEntity(enemy);
        }
        moveEnemies();
    }

    public void initEnemies() {
        enemies = new LinkedList<>();
        //TODO Initialize enemies to random positions
        enemies.add(new Spider(new Point(13,15), dungeon));
        enemies.add(new Spider(new Point(4,8), dungeon));
        enemies.add(new Spider(new Point(18,18), dungeon));
    }

    public void moveEnemies() {
        new Timer(500, w -> {
            for(Enemy e : enemies) {
                PathFinder pathFinder = new PathFinder(new DungeonGraph(dungeon.getTiles()), e.position());
                Stack<Integer> path = pathFinder.pathTo(hero.position().y*Dungeon.DUNGEON_SIZE+hero.position().x);

                if (!path.empty()) {
                    path.pop();
                    if (!path.empty()) {
                        int nextMove = path.pop();
                        e.move(new Point(nextMove % Dungeon.DUNGEON_SIZE, nextMove / Dungeon.DUNGEON_SIZE));
                    }

                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        ServerController controller = new ServerController();

        controller.initGame();
    }
}
*/
