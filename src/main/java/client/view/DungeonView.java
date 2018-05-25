package client.view;
import java.awt.Point;
import java.io.IOException;

import model.Dungeon;
import utils.Direction;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.*;
import model.entities.Entity;
import model.tiles.Tile;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

public class DungeonView implements TerminalResizeListener {
    private Terminal terminal;
    private Tile[][] tmpMap;

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
        showMap(Dungeon.getDungeon().getTiles());
    }

    public void showMap(Tile[][] map) throws IOException  {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                terminal.setBackgroundColor(new TextColor.RGB(map[i][j].color.getRed(), map[i][j].color.getGreen(), map[i][j].color.getBlue()));
                terminal.putCharacter(map[i][j].symbol);
                if (i == 0 || i == map.length-1 || j == 0 || j == map[i].length-1) {
                    terminal.putCharacter(map[i][j].symbol);
                } else
                    terminal.putCharacter(' ');
            }
            terminal.setCursorPosition(0, i+1);
        }
        terminal.flush();
        tmpMap = map;
    }

    @Override
    public void onResized(Terminal terminal, TerminalSize terminalSize) {
        try {
            if (tmpMap != null) {
                terminal.clearScreen();
                showMap(tmpMap);
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

    private void displayTile(Point pos) throws IOException {
        Tile tile = tmpMap[pos.y][pos.x];
        terminal.setBackgroundColor(new TextColor.RGB(tile.color.getRed(), tile.color.getGreen(), tile.color.getBlue()));
        terminal.putCharacter(tmpMap[pos.y][pos.x].symbol);

    }

    public synchronized void move(Entity entity, Point previousPos) throws IOException {
        terminal.setCursorPosition(previousPos.x * 2, previousPos.y);
        displayTile(new Point(previousPos.x, previousPos.y));
        displayEntity(entity);
    }

    public void attack(Entity entity, Direction direction, int range) {
        new Thread(() -> {
            try {
                Point position = entity.position();
                for (int i = 1; i <= range; i++) {
                    Point curPos = new Point(position.x + i * direction.x(), position.y + i * direction.y());
                    if (curPos.x < Dungeon.DUNGEON_SIZE && curPos.x >= 0 && curPos.y < Dungeon.DUNGEON_SIZE && curPos.y >= 0) {
                        Tile tile = tmpMap[curPos.y][curPos.x];
                        synchronized (this) {
                            terminal.setCursorPosition(curPos.x * 2, curPos.y);
                            terminal.setBackgroundColor(new TextColor.RGB(229, 99, 0));
                            terminal.putCharacter(tile.symbol);
                        }
                        terminal.flush();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ie) {
                        }
                        synchronized (this) {
                            terminal.setCursorPosition(curPos.x * 2, curPos.y);
                            terminal.setBackgroundColor(new TextColor.RGB(tile.color.getRed(), tile.color.getGreen(), tile.color.getBlue()));
                            terminal.putCharacter(tile.symbol);
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

    static public DungeonView getDungeonView() {
        return dungeonView;
    }

}
