package server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientWorker implements Runnable {
    private boolean connected;

    private IClientHandler handler;
    private Socket clientSocket;
    private InputStream is ;
    private OutputStream os;
    private GameServer server;

    public ClientWorker(Socket clientSocket, IClientHandler handler, GameServer server) throws IOException {
        this.clientSocket = clientSocket;
        this.handler = handler;
        this.server = server;
        is = clientSocket.getInputStream();
        os = clientSocket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            handler.handleClientConnection(is, os);

        } catch (IOException e) {

        }
    }

    public OutputStream getOs() {
        return os;
    }
}
