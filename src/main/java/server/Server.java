package server;

import server.controller.GameServer;

import java.io.IOException;

public class Server {
    public static void main(String ... args) throws IOException {
        GameServer server = GameServer.getServer();
        server.startServer();
        server.moveEnemies();
    }
}
