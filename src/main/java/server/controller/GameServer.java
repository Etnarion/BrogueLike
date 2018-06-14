package server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Dungeon;
import model.DungeonGraph;
import model.elements.entities.Enemy;
import model.elements.entities.Hero;
import model.elements.entities.PathFinder;
import protocol.MoveCommandResponse;
import protocol.MoveProtocol;
import utils.Direction;
import utils.JsonObjectMapper;

import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    ServerSocket serverSocket;
    public static int nbClients = 0;
    public static final int MAX_CONNEXIONS = 2;
    private int port = 3030;
    private static GameServer server = new GameServer();
    private boolean running;
    private List<ClientWorker> clientWorkers = new CopyOnWriteArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(GameServer.class.getName());

    private GameServer(int port) {
        this.port = port;
    }

    private GameServer() {
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));

        Thread serverThread = new Thread(() -> {
            while (nbClients < MAX_CONNEXIONS) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    nbClients++;
                    ClientWorker clientWorker = new ClientWorker(clientSocket, getClientHandler(), GameServer.this);
                    clientWorkers.add(clientWorker);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (ClientWorker clientWorker : clientWorkers) {
                Thread clientThread = new Thread(clientWorker);
                clientThread.start();
            }

        });
        serverThread.start();
    }

    private synchronized IClientHandler getClientHandler() {
        return new ClientHandler(nbClients);
    }

    public static GameServer getServer() {
        return server;
    }

    public void notifyClients(String message) {
        PrintWriter writer;
        for (ClientWorker client : clientWorkers) {
            writer = new PrintWriter(new OutputStreamWriter(client.getOs()));
            writer.println(message);
            writer.flush();
        }
    }

    public void notifyClients(ClientHandler sender, String message) {
        PrintWriter writer;
        for (ClientWorker client : clientWorkers) {
            if (client.getHandler().getHero() != sender.getHero()) {
                writer = new PrintWriter(new OutputStreamWriter(client.getOs()));
                writer.println(message);
                writer.flush();
            }
        }
    }

    public ArrayList<Hero> getHeroes() {
        ArrayList<Hero> heroes = new ArrayList<>();
        for (ClientWorker clientWorker : clientWorkers) {
            heroes.add(clientWorker.getHandler().getHero());
        }
        return heroes;
    }

    public void moveEnemies() {
        for(Enemy[] es : Dungeon.getDungeon().getEnemies()) {
            for (Enemy e : es) {
                if (e != null) {
                    new Thread(() -> {
                        int currentLevel = Dungeon.getDungeon().getCurrentLevel();
                        while (currentLevel == Dungeon.getDungeon().getCurrentLevel() && e.isAlive()) {
                            synchronized (Dungeon.getDungeon()) {
                                Hero closestHero = Dungeon.getDungeon().findClosestHero(e.position(), e.getVision());
                                if (closestHero != null) {

                                    LOGGER.log(Level.INFO, "spider at " + e.position() + " detected a hero at " + closestHero.position());

                                    PathFinder pathFinder = new PathFinder(new DungeonGraph(Dungeon.getDungeon().getTiles()), e.position());
                                    LinkedList<Integer> path = pathFinder.pathTo(closestHero.position().y * Dungeon.DUNGEON_SIZE + closestHero.position().x);

                                    if (!path.isEmpty()) {
                                        int nextMove = path.poll();
                                        int x = nextMove % Dungeon.DUNGEON_SIZE;
                                        int y = nextMove / Dungeon.DUNGEON_SIZE;
                                        Point newPos = new Point(x, y);
                                        LOGGER.log(Level.INFO, "spider's at " + e.position() + " next position: " + newPos);
                                        Point prevPos = e.position();
                                        e.move(newPos);
                                        try {
                                            moveEntity(e.getId(), Direction.getDirection(prevPos, newPos));
                                        } catch (JsonProcessingException e1) {
                                            e1.printStackTrace();
                                        }

                                    }
                                }
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ie) {
                            }
                        }
                    }).start();
                }
            }
        }
    }

    private void moveEntity(int idEntity, Direction direction) throws JsonProcessingException {
        MoveCommandResponse moveResponse = new MoveCommandResponse();
        moveResponse.setId(idEntity);
        moveResponse.setDirection(direction);
        GameServer.getServer().notifyClients(MoveProtocol.MOVE_RESPONSE + "\n" + JsonObjectMapper.toJson(moveResponse));
    }
}
