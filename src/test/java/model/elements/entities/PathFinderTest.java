package model.elements.entities;

import model.DungeonGraph;
import model.elements.tiles.Ground;
import model.elements.tiles.Tile;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    @Test
    void pathFinderTest() {
        Tile[][] tiles = new Tile[4][4];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                tiles[i][j] = new Ground(new Point(j, i));
            }
        }
        DungeonGraph dg = new DungeonGraph(tiles);
        PathFinder pf = new PathFinder(dg, new Point(0, 0));
        LinkedList<Integer> expectedPath = new LinkedList<>();
        expectedPath.add(4);
        expectedPath.add(8);
        expectedPath.add(9);
        assertEquals(expectedPath, pf.pathTo(10));
    }

}