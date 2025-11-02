package graph.topo;

import graph.common.Metrics;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TopologicalSortTest {

    @Test
    void testLinearDAG() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(0, Arrays.asList(1));
        adj.put(1, Arrays.asList(2));
        adj.put(2, Arrays.asList(3));
        adj.put(3, Arrays.asList());

        Metrics metrics = new Metrics();
        TopologicalSort topo = new TopologicalSort(adj, metrics);
        List<Integer> order = topo.kahnOrder();

        assertEquals(Arrays.asList(0, 1, 2, 3), order);
        assertTrue(metrics.kahnPushes > 0);
        assertTrue(metrics.kahnPops > 0);
    }

    @Test
    void testComplexDAG() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(0, Arrays.asList(1, 2));
        adj.put(1, Arrays.asList(3));
        adj.put(2, Arrays.asList(3));
        adj.put(3, Arrays.asList(4));
        adj.put(4, Arrays.asList());

        Metrics metrics = new Metrics();
        TopologicalSort topo = new TopologicalSort(adj, metrics);
        List<Integer> order = topo.kahnOrder();

        assertEquals(5, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(3)); // 0 before 3
        assertTrue(order.indexOf(1) < order.indexOf(3)); // 1 before 3
        assertTrue(order.indexOf(2) < order.indexOf(3)); // 2 before 3
        assertTrue(order.indexOf(3) < order.indexOf(4)); // 3 before 4
    }

    @Test
    void testSingleNode() {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        adj.put(0, Arrays.asList());

        Metrics metrics = new Metrics();
        TopologicalSort topo = new TopologicalSort(adj, metrics);
        List<Integer> order = topo.kahnOrder();

        assertEquals(Arrays.asList(0), order);
    }
}