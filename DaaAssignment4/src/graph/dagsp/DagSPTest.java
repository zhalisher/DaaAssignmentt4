package graph.dagsp;

import graph.common.Metrics;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DagSPTest {

    @Test
    void testShortestPathLinear() {
        Map<Integer, List<DagSP.Edge>> adj = new HashMap<>();
        adj.put(0, Arrays.asList(new DagSP.Edge(1, 2.0)));
        adj.put(1, Arrays.asList(new DagSP.Edge(2, 3.0)));
        adj.put(2, Arrays.asList());

        Metrics metrics = new Metrics();
        DagSP dagsp = new DagSP(adj, metrics);
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);

        DagSP.Result result = dagsp.shortestFrom(0, topoOrder);

        assertEquals(0.0, result.dist.get(0), 0.001);
        assertEquals(2.0, result.dist.get(1), 0.001);
        assertEquals(5.0, result.dist.get(2), 0.001);
    }

    @Test
    void testLongestPath() {
        Map<Integer, List<DagSP.Edge>> adj = new HashMap<>();
        adj.put(0, Arrays.asList(new DagSP.Edge(1, 2.0)));
        adj.put(1, Arrays.asList(new DagSP.Edge(2, 3.0)));
        adj.put(2, Arrays.asList());

        Metrics metrics = new Metrics();
        DagSP dagsp = new DagSP(adj, metrics);
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);

        DagSP.Result result = dagsp.longestFrom(0, topoOrder);

        assertEquals(0.0, result.dist.get(0), 0.001);
        assertEquals(2.0, result.dist.get(1), 0.001);
        assertEquals(5.0, result.dist.get(2), 0.001);

        List<Integer> path = result.reconstructPath(2);
        assertEquals(Arrays.asList(0, 1, 2), path);
    }

    @Test
    void testMultiplePaths() {
        Map<Integer, List<DagSP.Edge>> adj = new HashMap<>();
        adj.put(0, Arrays.asList(
                new DagSP.Edge(1, 1.0),
                new DagSP.Edge(2, 5.0)
        ));
        adj.put(1, Arrays.asList(new DagSP.Edge(3, 2.0)));
        adj.put(2, Arrays.asList(new DagSP.Edge(3, 1.0)));
        adj.put(3, Arrays.asList());

        Metrics metrics = new Metrics();
        DagSP dagsp = new DagSP(adj, metrics);
        List<Integer> topoOrder = Arrays.asList(0, 1, 2, 3);

        DagSP.Result result = dagsp.shortestFrom(0, topoOrder);

        //  0->1->3 = 3.0, 0->2->3 = 6.0
        assertEquals(3.0, result.dist.get(3), 0.001);
    }

    @Test
    void testUnreachableNodes() {
        Map<Integer, List<DagSP.Edge>> adj = new HashMap<>();
        adj.put(0, Arrays.asList(new DagSP.Edge(1, 1.0)));
        adj.put(1, Arrays.asList());
        adj.put(2, Arrays.asList());

        Metrics metrics = new Metrics();
        DagSP dagsp = new DagSP(adj, metrics);
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);

        DagSP.Result result = dagsp.shortestFrom(0, topoOrder);

        assertEquals(0.0, result.dist.get(0), 0.001);
        assertEquals(1.0, result.dist.get(1), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, result.dist.get(2), 0.001);
    }
}