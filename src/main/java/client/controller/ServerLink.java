package client.controller;

import model.Dungeon;
import protocol.GameProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerLink {
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    int id;

    public synchronized void connect(String server, int port) throws IOException {
        clientSocket = new Socket(server, port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
        out.println(GameProtocol.HELLO);
        out.flush();
        id = Integer.valueOf(in.readLine());
        Dungeon.getDungeon().initHero(id);
        InputController input = new InputController(in, out);
        ClientController clientController = new ClientController(in, out);
        (new Thread(input)).start();
        (new Thread(clientController)).start();
    }
}
