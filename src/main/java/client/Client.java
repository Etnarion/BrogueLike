package client;

import client.controller.MenuController;
import client.controller.ServerLink;
import client.view.DungeonView;

import java.io.IOException;

public class Client {
    public static void main(String ... args) throws IOException {
        MenuController menuController = new MenuController();
        menuController.input();

        ServerLink client = new ServerLink();
        client.connect(args[0], 3030);

        DungeonView dungeonView = DungeonView.getDungeonView();
        dungeonView.getInput();
    }
}
