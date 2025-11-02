package graph.dagsp;

import graph.common.Metrics;

import java.util.*;

public class DagSP {
    private final Map<Integer, List<Edge>> adj;
    private final Metrics metrics;

    public static class Edge {
        public final int to;
        public final double weight;
        public Edge(int to, double weight) { this.to = to; this.weight = weight; }
    }

    public DagSP(Map<Integer, List<Edge>> adj, Metrics metrics) {
        this.adj = adj;
        this.metrics = metrics;
    }

    public Result shortestFrom(int source, List<Integer> topoOrder) {
        int n = adj.size();
        double INF = Double.POSITIVE_INFINITY;
        Map<Integer, Double> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        for (int u : adj.keySet()) { dist.put(u, INF); parent.put(u, -1); }
        dist.put(source, 0.0);
        // relax in topo order
        for (int u : topoOrder) {
            if (dist.get(u) == INF) continue;
            for (Edge e : adj.getOrDefault(u, Collections.emptyList())) {
                metrics.relaxations++;
                if (dist.get(e.to) > dist.get(u) + e.weight) {
                    dist.put(e.to, dist.get(u) + e.weight);
                    parent.put(e.to, u);
                }
            }
        }
        return new Result(dist, parent);
    }

    public Result longestFrom(int source, List<Integer> topoOrder) {
        double NINF = Double.NEGATIVE_INFINITY;
        Map<Integer, Double> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        for (int u : adj.keySet()) { dist.put(u, NINF); parent.put(u, -1); }
        dist.put(source, 0.0);
        for (int u : topoOrder) {
            if (dist.get(u) == NINF) continue;
            for (Edge e : adj.getOrDefault(u, Collections.emptyList())) {
                metrics.relaxations++;
                if (dist.get(e.to) < dist.get(u) + e.weight) {
                    dist.put(e.to, dist.get(u) + e.weight);
                    parent.put(e.to, u);
                }
            }
        }
        return new Result(dist, parent);
    }

    public static class Result {
        public final Map<Integer, Double> dist;
        public final Map<Integer, Integer> parent;
        public Result(Map<Integer, Double> dist, Map<Integer, Integer> parent) {
            this.dist = dist; this.parent = parent;
        }
        public List<Integer> reconstructPath(int target) {
            if (!parent.containsKey(target)) return Collections.emptyList();
            List<Integer> path = new ArrayList<>();
            int cur = target;
            while (cur != -1) { path.add(cur); cur = parent.get(cur); }
            Collections.reverse(path);
            return path;
        }
    }
}
