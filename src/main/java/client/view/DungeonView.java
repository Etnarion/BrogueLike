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
import model.elements.tiles.Tile;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

public class DungeonView implements TerminalResizeListener {
    private Terminal terminal;
    final private Element[][] map;

    static DungeonView dungeonView;

    static {
        try {
            dungeonView = new DungeonView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DungeonView() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        terminal.setCursorVisible(false);
        terminal.addResizeListener(this);
        map = Dungeon.getDungeon().getElements();
    }

    public void showMap() throws IOException  {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                terminal.setBackgroundColor(new TextColor.RGB(map[i][j].color.getRed(), map[i][j].color.getGreen(), map[i][j].color.getBlue()));
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
                terminal.clearScreen();
                showMap();
            }
        } catch (IOException e) {
            return;
        }
    }

    public void displayEntity(Entity entity) throws IOException {
        Tile tile = entity.tile();
        terminal.setCursorPosition(entity.position().x*2, entity.position().y);
        terminal.setBackgroundColor(new TextColor.RGB(tile.color.getRed(), tile.color.getGreen(), tile.color.getBlue()));
        terminal.putCharacter(entity.symbol);
        terminal.flush();
    }

    public void displayElement(Point pos) throws IOException {
        Element element = map[pos.y][pos.x];
        terminal.setCursorPosition(pos.x * 2, pos.y);
        terminal.setBackgroundColor(new TextColor.RGB(element.color.getRed(), element.color.getGreen(), element.color.getBlue()));
        terminal.putCharacter(element.symbol);
    }

    public void move(Entity entity, Point previousPos) throws IOException {
        displayElement(new Point(previousPos.x, previousPos.y));
        displayEntity(entity);
    }

    public void attack(Entity entity, Direction direction, int range) {
        new Thread(() -> {
            try {
                Point position = entity.position();
                for (int i = 1; i <= range; i++) {
                    Point curPos = new Point(position.x + i * direction.x(), position.y + i * direction.y());
                    if (curPos.x < Dungeon.DUNGEON_SIZE && curPos.x >= 0 && curPos.y < Dungeon.DUNGEON_SIZE && curPos.y >= 0) {
                        Element element = map[curPos.y][curPos.x];
                        Entity curEntity = Dungeon.getDungeon().getEntity(new Point(curPos.x, curPos.y));
                        synchronized (this) {
                            terminal.setCursorPosition(curPos.x * 2, curPos.y);
                            terminal.setBackgroundColor(new TextColor.RGB(229, 99, 0));
                            if (curEntity == null)
                                terminal.putCharacter(element.symbol);
                            else
                                terminal.putCharacter(curEntity.symbol);
                        }
                        terminal.flush();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ie) {
                        }
                        synchronized (this) {
                            terminal.setCursorPosition(curPos.x * 2, curPos.y);
                            terminal.setBackgroundColor(new TextColor.RGB(element.color.getRed(), element.color.getGreen(), element.color.getBlue()));
                            if (curEntity == null)
                                terminal.putCharacter(element.symbol);
                            else
                                terminal.putCharacter(curEntity.symbol);
                        }
                        terminal.flush();
                    }
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

    public Terminal getTerminal() {
        return terminal;
    }

}
