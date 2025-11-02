package graph.scc;

import graph.common.Metrics;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TarjanSCCTest {

    @Test
    void testSimpleDAG() {
        Map<String, List<String>> adj = new HashMap<>();
        adj.put("A", Arrays.asList("B"));
        adj.put("B", Arrays.asList("C"));
        adj.put("C", Arrays.asList());
        adj.put("D", Arrays.asList("E"));
        adj.put("E", Arrays.asList());

        Metrics metrics = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(adj, metrics);
        List<List<String>> sccs = tarjan.run();

        assertEquals(5, sccs.size());
        assertTrue(sccs.stream().allMatch(comp -> comp.size() == 1));
    }

    @Test
    void testSingleCycle() {
        Map<String, List<String>> adj = new HashMap<>();
        adj.put("A", Arrays.asList("B"));
        adj.put("B", Arrays.asList("C"));
        adj.put("C", Arrays.asList("A"));

        Metrics metrics = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(adj, metrics);
        List<List<String>> sccs = tarjan.run();

        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).containsAll(Arrays.asList("A", "B", "C")));
    }

    @Test
    void testMultipleSCCs() {
        Map<String, List<String>> adj = new HashMap<>();
        adj.put("A", Arrays.asList("B"));
        adj.put("B", Arrays.asList("A"));

        adj.put("C", Arrays.asList("D"));
        adj.put("D", Arrays.asList("C"));

        adj.put("E", Arrays.asList());

        Metrics metrics = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(adj, metrics);
        List<List<String>> sccs = tarjan.run();

        assertEquals(3, sccs.size());
        // check all found comp
        boolean foundAB = sccs.stream().anyMatch(comp -> comp.contains("A") && comp.contains("B"));
        boolean foundCD = sccs.stream().anyMatch(comp -> comp.contains("C") && comp.contains("D"));
        boolean foundE = sccs.stream().anyMatch(comp -> comp.contains("E") && comp.size() == 1);

        assertTrue(foundAB && foundCD && foundE);
    }

    @Test
    void testEmptyGraph() {
        Map<String, List<String>> adj = new HashMap<>();
        Metrics metrics = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(adj, metrics);
        List<List<String>> sccs = tarjan.run();

        assertTrue(sccs.isEmpty());
    }
}