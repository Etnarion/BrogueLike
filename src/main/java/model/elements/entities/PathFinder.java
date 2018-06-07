package model.elements.entities;

import model.Dungeon;
import model.DungeonGraph;

import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;

public class PathFinder {
    private int[] edgeTo;
    private boolean[] marked;
    private int source;
    DungeonGraph graph;
    private int[] distTo;

    public PathFinder(DungeonGraph graph, Point position) {
        this.graph = graph;
        source = position.x * (Dungeon.DUNGEON_SIZE) + position.y;
        edgeTo = new int[graph.V()];
        distTo = new int[graph.V()];
        marked = new boolean[graph.V()];
        bfs(graph, source);
    }

    private void bfs(DungeonGraph graph, int source) {
        LinkedList<Integer> q = new LinkedList();
        for (int v = 0; v < graph.V(); v++)
            distTo[v] = 10000000;
        distTo[source] = 0;
        marked[source] = true;
        q.add(source);

        while (!q.isEmpty()) {
            int v = q.poll();
            for (DungeonGraph.Edge w : graph.adjacents(v)) {
                if (!marked[w.v2]) {
                    edgeTo[w.v2] = v;
                    distTo[w.v2] = distTo[v]+1;
                    marked[w.v2] = true;
                    q.add(w.v2);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Stack<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }
}
