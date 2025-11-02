package graph.scc;

import graph.common.Metrics;

import java.util.*;

public class TarjanSCC {
    private final Map<String, List<String>> adj;
    private final Metrics metrics;

    private Map<String, Integer> index;
    private Map<String, Integer> lowlink;
    private Deque<String> stack;
    private Set<String> onStack;
    private int currentIndex;
    private List<List<String>> components;

    public TarjanSCC(Map<String, List<String>> adj, Metrics metrics) {
        this.adj = adj;
        this.metrics = metrics;
    }

    public List<List<String>> run() {
        index = new HashMap<>();
        lowlink = new HashMap<>();
        stack = new ArrayDeque<>();
        onStack = new HashSet<>();
        components = new ArrayList<>();
        currentIndex = 0;
        metrics.start();
        for (String v : adj.keySet()) {
            if (!index.containsKey(v)) strongconnect(v);
        }
        metrics.stop();
        return components;
    }

    private void strongconnect(String v) {
        index.put(v, currentIndex);
        lowlink.put(v, currentIndex);
        currentIndex++;
        metrics.dfsVisits++;
        stack.push(v); onStack.add(v);

        for (String w : adj.getOrDefault(v, Collections.emptyList())) {
            metrics.dfsEdges++;
            if (!index.containsKey(w)) {
                strongconnect(w);
                lowlink.put(v, Math.min(lowlink.get(v), lowlink.get(w)));
            } else if (onStack.contains(w)) {
                lowlink.put(v, Math.min(lowlink.get(v), index.get(w)));
            }
        }

        if (lowlink.get(v).equals(index.get(v))) {
            List<String> comp = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (!w.equals(v));
            components.add(comp);
        }
    }
}
