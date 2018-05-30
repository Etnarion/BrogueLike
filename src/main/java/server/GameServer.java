package server;

import model.elements.tiles.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void connect() throws IOException {
        clientSocket = serverSocket.accept();
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
        System.out.println("Connexion done");
    }

    public boolean sendMap(Tile[][] tiles) {
        try {
            out.writeObject(tiles);
            out.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
