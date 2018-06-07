package client.view;
import java.awt.Point;
import java.io.IOException;

import model.Dungeon;
import model.elements.Element;
import model.elements.mechanisms.Door;
import model.elements.tiles.Wall;
import utils.Direction;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.*;
import model.elements.entities.Entity;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

public class DungeonView implements TerminalResizeListener {
    private Terminal terminal;
    private Element[][] map;
    private Element[][] tileMap;


    private static DungeonView dungeonView;

    static {
        try {
            dungeonView = new DungeonView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DungeonView() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        terminal.setCursorVisible(false);
        terminal.addResizeListener(this);
        map = Dungeon.getDungeon().getElements();
        tileMap = Dungeon.getDungeon().getTiles();
    }

    public void showMap() throws IOException  {
        terminal.clearScreen();
        terminal.flush();
        terminal.setCursorPosition(0, 0);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                terminal.setForegroundColor(map[i][j].fontColor);
                terminal.setBackgroundColor(tileMap[i][j].color);
                terminal.putCharacter(map[i][j].symbol);
                if (map[i][j] instanceof Wall || map[i][j] instanceof Door) {
                    terminal.putCharacter(map[i][j].symbol);
                } else
                    terminal.putCharacter(' ');
            }
            terminal.setCursorPosition(0, i+1);
        }
        terminal.flush();
    }

    @Override
    public void onResized(Terminal terminal, TerminalSize terminalSize) {
        try {
            if (map != null) {
                showMap();
            }
        } catch (IOException ignored) {
        }
    }

    public void displayEntity(Entity entity) throws IOException {
        terminal.setCursorPosition(entity.position().x*2, entity.position().y);
        terminal.setForegroundColor(entity.fontColor);
        terminal.setBackgroundColor(Dungeon.getDungeon().getTiles()[entity.position().y][entity.position().x].color);
        terminal.putCharacter(entity.symbol);
        terminal.flush();
    }

    public void displayElement(Point pos) throws IOException {
        Element element = map[pos.y][pos.x];
        terminal.setCursorPosition(pos.x * 2, pos.y);
        terminal.setForegroundColor(element.fontColor);
        terminal.setBackgroundColor(element.color);
        terminal.putCharacter(element.symbol);
        terminal.flush();
    }

    public void move(Entity entity, Point previousPos) throws IOException {
        displayElement(new Point(previousPos.x, previousPos.y));
        displayEntity(entity);
    }

    public void attack(Entity entity, Direction direction, int range) {
        new Thread(() -> {
            try {

                Point position = entity.position();
                boolean wallFound = false;
                int i = 1;
                while (i <= range && !wallFound) {
                    Point curPos = new Point(position.x + i * direction.x(), position.y + i * direction.y());
                    if (curPos.x < Dungeon.DUNGEON_SIZE && curPos.x >= 0 && curPos.y < Dungeon.DUNGEON_SIZE && curPos.y >= 0) {
                        Element element = map[curPos.y][curPos.x];
                        Entity curEntity = Dungeon.getDungeon().getEntity(new Point(curPos.x, curPos.y));
                        synchronized (DungeonView.getDungeonView()) {
                            terminal.setCursorPosition(curPos.x * 2, curPos.y);
                            terminal.setBackgroundColor(new TextColor.RGB(229, 99, 0));
                            if (curEntity == null)
                                terminal.putCharacter(element.symbol);
                            else
                                terminal.putCharacter(curEntity.symbol);
                            terminal.flush();
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                        synchronized (DungeonView.getDungeonView()) {
                            terminal.setCursorPosition(curPos.x * 2, curPos.y);
                            terminal.setForegroundColor(element.fontColor);
                            terminal.setBackgroundColor(element.color);
                            if (curEntity == null)
                                terminal.putCharacter(element.symbol);
                            else
                                terminal.putCharacter(curEntity.symbol);
                            terminal.flush();
                        }
                        wallFound = !Dungeon.getDungeon().getElement(curPos).isWalkable();
                        i++;
                    }
                    if (Dungeon.getDungeon().isStopping())
                        return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public KeyStroke getInput() throws IOException {
        KeyStroke key = terminal.readInput();
        while(key == null) {
            key = terminal.readInput();
        }
        return key;
    }

    public void close() throws IOException {
        terminal.close();
    }

    static public DungeonView getDungeonView() {
        return dungeonView;
    }

    Terminal getTerminal() {
        return terminal;
    }

}
