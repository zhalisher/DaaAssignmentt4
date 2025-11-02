package graph.topo;

import graph.common.Metrics;

import java.util.*;


public class TopologicalSort {
    private final Map<Integer, List<Integer>> adj;
    private final Metrics metrics;

    public TopologicalSort(Map<Integer, List<Integer>> adj, Metrics metrics) {
        this.adj = adj;
        this.metrics = metrics;
    }

    public List<Integer> kahnOrder() {
        Map<Integer, Integer> indeg = new HashMap<>();
        for (int u : adj.keySet()) indeg.put(u, 0);
        for (int u : adj.keySet()) {
            for (int v : adj.get(u)) indeg.put(v, indeg.getOrDefault(v, 0) + 1);
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (Map.Entry<Integer,Integer> e : indeg.entrySet()) {
            if (e.getValue() == 0) { q.add(e.getKey()); metrics.kahnPushes++; }
        }
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            metrics.kahnPops++;
            order.add(u);
            for (int v : adj.getOrDefault(u, Collections.emptyList())) {
                indeg.put(v, indeg.get(v) - 1);
                if (indeg.get(v) == 0) { q.add(v); metrics.kahnPushes++; }
            }
        }
        return order;
    }
}
