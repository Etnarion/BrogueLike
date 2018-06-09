package client.controller;

import client.view.MenuView;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

public class MenuController {
    private MenuView menuView;

    public MenuController() throws IOException {
        menuView = new MenuView();
    }

    public void input() throws IOException {
        boolean finished = false;
        while (!finished) {
            int choice = menuView.getCurrentChoice();
            KeyStroke key = menuView.getInput();
            switch (key.getKeyType()) {
                case ArrowUp:
                    choice = menuView.previousChoice();
                    break;
                case ArrowDown:
                    choice = menuView.nextChoice();
                    break;
                case Enter:
                    switch (menuView.getCurrentChoice()) {
                        case 0:
                            menuView.showWaitMessage();
                            finished = true;
                            break;
                        case 1:
                            menuView.close();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            switch (choice) {
                case 0:
                    menuView.setNewGame();
                    break;
                case 1:
                    menuView.setQuit();
                    break;
                default:
                    break;
            }
        }
    }
}
