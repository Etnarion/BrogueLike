package client.controller;

import model.Dungeon;
import model.elements.entities.Hero;
import utils.Direction;
import client.view.DungeonView;
import com.googlecode.lanterna.input.KeyStroke;
import protocol.AttackProtocol;
import protocol.MoveProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class InputController implements Runnable {
    BufferedReader in;
    PrintWriter out;

    public InputController(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void input() throws IOException {
        Hero hero = Dungeon.getDungeon().getHero();
        while (hero.isAlive()) {
            KeyStroke key = DungeonView.getDungeonView().getInput();
            switch (key.getKeyType()) {
                case ArrowDown:
                    move(Direction.DOWN);
                    break;
                case ArrowLeft:
                    move(Direction.LEFT);
                    break;
                case ArrowRight:
                    move(Direction.RIGHT);
                    break;
                case ArrowUp:
                    move(Direction.UP);
                    break;
                case Character:
                    switch (key.getCharacter()) {
                        case 'a' :
                            attack(Direction.LEFT);
                            break;
                        case 's' :
                            attack(Direction.DOWN);
                            break;
                        case 'd' :
                            attack(Direction.RIGHT);
                            break;
                        case 'w' :
                            attack(Direction.UP);
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }
        }
    }

    public void move(Direction direction) throws IOException {
        switch (direction) {
            case UP:
                out.println(MoveProtocol.UP);
                out.flush();
                break;
            case DOWN:
                out.println(MoveProtocol.DOWN);
                out.flush();
                break;
            case LEFT:
                out.println(MoveProtocol.LEFT);
                out.flush();
                break;
            case RIGHT:
                out.println(MoveProtocol.RIGHT);
                out.flush();
                break;
            default:
                break;
        }
    }

    public void attack(Direction direction) throws IOException {
        switch (direction) {
            case UP:
                out.println(AttackProtocol.ATTACK_UP);
                out.flush();
                break;
            case DOWN:
                out.println(AttackProtocol.ATTACK_DOWN);
                out.flush();
                break;
            case LEFT:
                out.println(AttackProtocol.ATTACK_LEFT);
                out.flush();
                break;
            case RIGHT:
                out.println(AttackProtocol.ATTACK_RIGHT);
                out.flush();
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        try {
            input();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
