package client.controller;

import model.Dungeon;
import protocol.GameProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerLink {
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    int id;

    private final static Logger LOGGER = Logger.getLogger(ServerLink.class.getName());

    public synchronized void connect(String server, int port) throws IOException {
        clientSocket = new Socket(server, port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
        out.println(GameProtocol.HELLO);
        out.flush();
        id = Integer.valueOf(in.readLine());
        Dungeon.getDungeon().initHero(id);
        LOGGER.log(Level.INFO, "Init hero, hero's id: " + Dungeon.getDungeon().getHero().getId());
        InputController input = new InputController(in, out);
        ClientController clientController = new ClientController(in, out);
        (new Thread(input)).start();
        (new Thread(clientController)).start();
    }
}
