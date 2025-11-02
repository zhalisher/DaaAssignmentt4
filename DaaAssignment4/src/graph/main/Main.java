package graph.main;

import graph.common.IOUtils;
import graph.common.Metrics;
import graph.scc.TarjanSCC;
import graph.Condensation;
import graph.topo.TopologicalSort;
import graph.dagsp.DagSP;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        graph.datagen.DatasetGenerator.generateAllDatasets();

        String[] files = {
                "data/tasks_small_1.json", "data/tasks_small_2.json", "data/tasks_small_3.json",
                "data/tasks_medium_1.json", "data/tasks_medium_2.json", "data/tasks_medium_3.json",
                "data/tasks_large_1.json", "data/tasks_large_2.json", "data/tasks_large_3.json"
        };

        for (String path : files) {
            System.out.println("\n=== Running " + path + " ===");
            runPipeline(path);
        }
    }
    private static void runPipeline(String path) throws IOException {
        IOUtils.GraphData data = IOUtils.readGraphFromFile(path);

        System.out.println("Loading graph from: " + path);
        System.out.println("Vertices: " + data.vertices.size());
        System.out.println("Edges: " + data.edges.size());

        //adjancy list
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Map<String, Double>> rawEdgeWeights = new HashMap<>();

        for (String v : data.vertices) {
            adj.put(v, new ArrayList<>());
            rawEdgeWeights.put(v, new HashMap<>());
        }

        for (IOUtils.Edge e : data.edges) {
            adj.computeIfAbsent(e.from, k -> new ArrayList<>()).add(e.to);
            rawEdgeWeights.computeIfAbsent(e.from, k -> new HashMap<>())
                    .put(e.to, Math.min(rawEdgeWeights.get(e.from).getOrDefault(e.to, Double.POSITIVE_INFINITY), e.weight));
        }

        System.out.println("Final graph - Vertices: " + adj.size() + ", Edges: " + countEdges(adj));

        Metrics sccMetrics = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(adj, sccMetrics);
        List<List<String>> sccs = tarjan.run();
        System.out.println("\nSCC count: " + sccs.size());

        Condensation.Result cond = Condensation.build(adj, sccs, rawEdgeWeights);
        System.out.println("Condensation DAG: " + cond.componentCount + " nodes");

        // topo
        Metrics topoMetrics = new Metrics();
        TopologicalSort topo = new TopologicalSort(cond.dagAdj, topoMetrics);
        List<Integer> compOrder = topo.kahnOrder();

        // dag both shortest amd longest
        Map<Integer, List<DagSP.Edge>> dagAdj = new HashMap<>();
        for (int u = 0; u < cond.componentCount; u++)
            dagAdj.put(u, new ArrayList<>());

        for (int u : cond.dagAdj.keySet()) {
            for (int v : cond.dagAdj.get(u)) {
                double w = cond.dagWeights.get(u).get(v);
                dagAdj.get(u).add(new DagSP.Edge(v, w));
            }
        }

        int source = compOrder.isEmpty() ? 0 : compOrder.get(0);

        // shortest
        Metrics spMetrics = new Metrics();
        DagSP dagsp = new DagSP(dagAdj, spMetrics);
        spMetrics.start();
        DagSP.Result shortRes = dagsp.shortestFrom(source, compOrder);
        spMetrics.stop();

        //longest
        Metrics longMetrics = new Metrics();
        DagSP dagsp2 = new DagSP(dagAdj, longMetrics);
        longMetrics.start();
        DagSP.Result longRes = dagsp2.longestFrom(source, compOrder);
        longMetrics.stop();

        int bestTarget = -1;
        double best = Double.NEGATIVE_INFINITY;
        for (Map.Entry<Integer, Double> e : longRes.dist.entrySet()) {
            if (e.getValue() > best && e.getValue() != Double.POSITIVE_INFINITY) {
                best = e.getValue();
                bestTarget = e.getKey();
            }
        }

        //sim
        printComprehensiveAnalysis(data, sccs, cond, compOrder, shortRes, longRes, bestTarget);
    }


    private static int countEdges(Map<String, List<String>> adj) {
        int count = 0;
        for (List<String> neighbors : adj.values())
            count += neighbors.size();
        return count;
    }

    private static void printComprehensiveAnalysis(IOUtils.GraphData data,
                                                   List<List<String>> sccs,
                                                   Condensation.Result cond,
                                                   List<Integer> compOrder,
                                                   DagSP.Result shortRes,
                                                   DagSP.Result longRes,
                                                   int bestTarget) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("COMPREHENSIVE ANALYSIS REPORT");
        System.out.println("=".repeat(60));

        System.out.println("Vertices: " + data.vertices.size() + ", Edges: " + data.edges.size());
        System.out.println("SCC count: " + sccs.size());
        System.out.println("Condensation DAG: " + cond.componentCount + " nodes");

        if (bestTarget != -1) {
            System.out.println("Critical path length: " + longRes.dist.get(bestTarget));
            System.out.println("Critical path (component sequence): " + longRes.reconstructPath(bestTarget));
        }
    }
}
