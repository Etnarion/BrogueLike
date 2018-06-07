package model;


import model.elements.tiles.Tile;

import java.util.LinkedList;

public class DungeonGraph {
    public class Edge {
        private int v1, v2;

        public Edge(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public int either() {
            return v1;
        }

        public int other(int v) {
            return (v == v1) ? v2 : v1;
        }
    }

    private LinkedList<Edge>[] edgeAdjacencyLists;

    public DungeonGraph(Tile[][] map) {
        edgeAdjacencyLists = (LinkedList<Edge>[])new LinkedList[map.length*map.length];
        for (int i = 0; i < edgeAdjacencyLists.length; i++) {
            edgeAdjacencyLists[i] = new LinkedList();
        }
        arrayToAdjacencyList(map);
    }

    public int V() {
        return edgeAdjacencyLists.length;
    }

    public void addEdge(int v1, int v2) {
        edgeAdjacencyLists[v1].add(new Edge(v1, v2));
    }

    private void arrayToAdjacencyList(Tile[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].isWalkable()) {
                    if (i-1 >= 0 && map[i-1][j].isWalkable()) {
                        addEdge(i*map.length+j, (i-1)*map.length+j);
                    }
                    if (i+1 < map.length && map[i+1][j].isWalkable()) {
                        addEdge(i*map.length+j, (i+1)*map.length+j);
                    }
                    if (j-1 >= 0 && map[i][j-1].isWalkable()) {
                        addEdge(i*map.length+j, i*map.length+j-1);
                    }
                    if (j+1 < map.length && map[i][j+1].isWalkable()) {
                        addEdge(i*map.length+j, i*map.length+j+1);
                    }
                }
            }
        }
    }

    public LinkedList<Edge> adjacents(int v) {
        return edgeAdjacencyLists[v];
    }
}
