package model.elements.entities;

import model.Dungeon;
import model.DungeonGraph;

import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;

public class PathFinder {
    private static final int INF_DISTANCE = 10000000;
    private int[] edgeTo;
    private boolean[] marked;
    private int source;
    private DungeonGraph graph;
    private int[] distTo;

    public PathFinder(DungeonGraph graph, Point position) {
        this.graph = graph;
        source = position.y * (Dungeon.DUNGEON_SIZE) + position.x;
        edgeTo = new int[graph.V()];
        distTo = new int[graph.V()];
        marked = new boolean[graph.V()];
        bfs(source);
    }

    private void bfs(int source) {
        LinkedList<Integer> q = new LinkedList();
        for (int v = 0; v < graph.V(); v++)
            distTo[v] = INF_DISTANCE;
        distTo[source] = 0;
        edgeTo[source] = source;
        marked[source] = true;
        q.add(source);

        while (!q.isEmpty()) {
            int v = q.poll();
            for (DungeonGraph.Edge w : graph.adjacents(v)) {
                int v2 = w.other(v);
                if (!marked[v2]) {
                    edgeTo[v2] = v;
                    distTo[v2] = distTo[v]+1;
                    marked[v2] = true;
                    q.add(v2);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Finds the shortest path from the source edge to the v vertex
     * @param v the edge we want to find the shortest path to
     * @return the stack containing the path from the source (top of the stack, exclusive)
     * to v(bottom of the stack, inclusive). If no path exists to the destination(v) vertex, returns null.
     */
    public LinkedList<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        LinkedList<Integer> path = new LinkedList<>();
        int curr = v;
        while (distTo[curr] != 0) {
            path.addFirst(curr);
            curr = edgeTo[curr];
        }
        return path;
    }
}
