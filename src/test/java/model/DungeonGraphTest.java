package model;

import model.elements.tiles.Ground;
import model.elements.tiles.ShallowWater;
import model.elements.tiles.Tile;
import model.elements.tiles.Wall;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DungeonGraphTest {

    @Test
    void itShouldGenerateGraphOfRightSize() {
        Tile[][] tiles = new Tile[3][3];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new Wall(new Point(j, i));
            }
        }
        tiles[1][1] = new Ground(new Point(1, 1));
        DungeonGraph dg = new DungeonGraph(tiles);
        assertEquals(dg.V(), 9);
    }
}