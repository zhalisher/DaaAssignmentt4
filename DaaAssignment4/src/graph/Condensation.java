package graph;

import java.util.*;

public class Condensation {
    public static class Result {
        public Map<String, Integer> compId;
        public Map<Integer, List<Integer>> dagAdj;
        public Map<Integer, Map<Integer, Double>> dagWeights; // ADDED: Store weights between components
        public int componentCount;
    }

    public static Result build(Map<String, List<String>> adj,
                               List<List<String>> sccs,
                               Map<String, Map<String, Double>> edgeWeights) { // ADDED: edgeWeights parameter
        Result r = new Result();
        r.compId = new HashMap<>();
        r.dagWeights = new HashMap<>();

        for (int i = 0; i < sccs.size(); i++) {
            for (String v : sccs.get(i)) r.compId.put(v, i);
        }

        r.componentCount = sccs.size();
        r.dagAdj = new HashMap<>();

        for (String u : adj.keySet()) {
            int cu = r.compId.get(u);
            for (String v : adj.getOrDefault(u, Collections.emptyList())) {
                int cv = r.compId.get(v);
                if (cu != cv) {
                    r.dagAdj.computeIfAbsent(cu, k -> new ArrayList<>());
                    if (!r.dagAdj.get(cu).contains(cv)) {
                        r.dagAdj.get(cu).add(cv);
                    }

                    // store min
                    double weight = edgeWeights.get(u).get(v);
                    r.dagWeights.computeIfAbsent(cu, k -> new HashMap<>())
                            .merge(cv, weight, Math::min);
                }
            }
        }

        for (int i = 0; i < r.componentCount; i++) {
            r.dagAdj.computeIfAbsent(i, k -> new ArrayList<>());
            r.dagWeights.computeIfAbsent(i, k -> new HashMap<>());
        }
        return r;
    }
}