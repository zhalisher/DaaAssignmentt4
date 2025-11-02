package graph.datagen;

import graph.common.IOUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {

    private static String toJsonString(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof String) {
                sb.append("\"").append(list.get(i)).append("\"");
            } else {
                sb.append(list.get(i));
            }
            if (i < list.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // small
    public static void generateSmallDataset1() throws IOException {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1.0), new Edge("B", "C", 2.0),
                new Edge("A", "D", 1.5), new Edge("D", "E", 1.0),
                new Edge("E", "F", 2.5)
        );
        writeToFile("data/tasks_small_1.json", vertices, edges);
    }

    public static void generateSmallDataset2() throws IOException {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E", "F", "G");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1.0), new Edge("B", "C", 2.0),
                new Edge("C", "A", 1.5), // cycle A-B-C-A
                new Edge("C", "D", 1.0), new Edge("D", "E", 2.0),
                new Edge("E", "F", 1.0), new Edge("F", "G", 1.5)
        );
        writeToFile("data/tasks_small_2.json", vertices, edges);
    }

    public static void generateSmallDataset3() throws IOException {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1.0), new Edge("B", "A", 1.5), // cycle A-B
                new Edge("C", "D", 2.0), new Edge("D", "C", 1.0), // cycle C-D
                new Edge("E", "F", 1.5), new Edge("F", "G", 2.0),
                new Edge("G", "H", 1.0), new Edge("H", "E", 2.5)  // cycle E-F-G-H-E
        );
        writeToFile("data/tasks_small_3.json", vertices, edges);
    }

    // medium
    public static void generateMediumDataset1() throws IOException {
        // Mixed structure with multiple SCCs - sparse
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < 15; i++) vertices.add("T" + i);

        List<Edge> edges = Arrays.asList(
                new Edge("T0", "T1", 1.0), new Edge("T1", "T2", 2.0), new Edge("T2", "T0", 1.5),

                new Edge("T3", "T4", 1.0), new Edge("T4", "T3", 2.0),

                new Edge("T0", "T5", 1.0), new Edge("T2", "T6", 2.0),
                new Edge("T3", "T7", 1.5), new Edge("T4", "T8", 1.0),

                new Edge("T5", "T9", 2.0), new Edge("T6", "T10", 1.0),
                new Edge("T7", "T11", 1.5), new Edge("T8", "T12", 2.0),
                new Edge("T9", "T13", 1.0), new Edge("T10", "T14", 2.0)
        );
        writeToFile("data/tasks_medium_1.json", vertices, edges);
    }

    public static void generateMediumDataset2() throws IOException {
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < 12; i++) vertices.add("N" + i);

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int from = i * 3 + j;
                for (int k = 0; k < 3 && (i + 1) * 3 + k < 12; k++) {
                    int to = (i + 1) * 3 + k;
                    edges.add(new Edge("N" + from, "N" + to, 0.5 + Math.random() * 2.0));
                }
            }
        }
        writeToFile("data/tasks_medium_2.json", vertices, edges);
    }

    public static void generateMediumDataset3() throws IOException {
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < 18; i++) vertices.add("V" + i);

        List<Edge> edges = Arrays.asList(
                new Edge("V0", "V1", 1.0), new Edge("V1", "V2", 2.0), new Edge("V2", "V3", 1.5),
                new Edge("V3", "V0", 1.0), new Edge("V1", "V3", 2.0), new Edge("V2", "V0", 1.5),

                new Edge("V4", "V5", 1.0), new Edge("V5", "V6", 2.0), new Edge("V6", "V4", 1.5),

                new Edge("V0", "V7", 1.0), new Edge("V3", "V8", 2.0), new Edge("V4", "V9", 1.5),

                new Edge("V7", "V10", 1.0), new Edge("V8", "V11", 2.0), new Edge("V9", "V12", 1.5),
                new Edge("V10", "V13", 1.0), new Edge("V11", "V14", 2.0), new Edge("V12", "V15", 1.5),
                new Edge("V13", "V16", 1.0), new Edge("V14", "V17", 2.0)
        );
        writeToFile("data/tasks_medium_3.json", vertices, edges);
    }

    // latge
    public static void generateLargeDataset1() throws IOException {
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < 35; i++) vertices.add("L" + i);

        List<Edge> edges = new ArrayList<>();
        Random rand = new Random(42);

        for (int scc = 0; scc < 3; scc++) {
            int start = scc * 5;
            for (int i = start; i < start + 5; i++) {
                for (int j = start; j < start + 5; j++) {
                    if (i != j && rand.nextDouble() < 0.3) {
                        edges.add(new Edge("L" + i, "L" + j, 0.5 + rand.nextDouble() * 2.0));
                    }
                }
            }
        }

        for (int i = 0; i < 15; i++) {
            for (int j = 15; j < 35; j++) {
                if (rand.nextDouble() < 0.1) {
                    edges.add(new Edge("L" + i, "L" + j, 1.0 + rand.nextDouble() * 3.0));
                }
            }
        }

        writeToFile("data/tasks_large_1.json", vertices, edges);
    }

    public static void generateLargeDataset2() throws IOException {
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < 40; i++) vertices.add("D" + i);

        List<Edge> edges = new ArrayList<>();
        Random rand = new Random(123);

        for (int i = 0; i < 40; i++) {
            for (int j = i + 1; j < 40; j++) {
                if (rand.nextDouble() < 0.4) { // 40% density
                    edges.add(new Edge("D" + i, "D" + j, 0.1 + rand.nextDouble() * 2.0));
                }
            }
        }

        writeToFile("data/tasks_large_2.json", vertices, edges);
    }

    public static void generateLargeDataset3() throws IOException {
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < 50; i++) vertices.add("M" + i);

        List<Edge> edges = new ArrayList<>();
        Random rand = new Random(456);

        for (int i = 0; i < 20; i++) {
            int next = (i + 1) % 20;
            edges.add(new Edge("M" + i, "M" + next, 0.5 + rand.nextDouble() * 1.5));
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (i != j && rand.nextDouble() < 0.2) {
                    edges.add(new Edge("M" + i, "M" + j, 1.0 + rand.nextDouble() * 2.0));
                }
            }
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 20; j < 50; j++) {
                if (rand.nextDouble() < 0.15) {
                    edges.add(new Edge("M" + i, "M" + j, 0.8 + rand.nextDouble() * 2.2));
                }
            }
        }

        for (int i = 20; i < 50; i++) {
            for (int j = i + 1; j < 50; j++) {
                if (rand.nextDouble() < 0.25) {
                    edges.add(new Edge("M" + i, "M" + j, 0.5 + rand.nextDouble() * 1.5));
                }
            }
        }

        writeToFile("data/tasks_large_3.json", vertices, edges);
    }

    public static void generateAllDatasets() throws IOException {
        new java.io.File("data").mkdirs();

        generateSmallDataset1();
        generateSmallDataset2();
        generateSmallDataset3();

        generateMediumDataset1();
        generateMediumDataset2();
        generateMediumDataset3();

        generateLargeDataset1();
        generateLargeDataset2();
        generateLargeDataset3();

        System.out.println("Generated 9 datasets in data/ directory");
    }

    public static void main(String[] args) throws IOException {
        generateAllDatasets();
    }

    private static class Edge {
        String from, to;
        double weight;

        Edge(String f, String t, double w) {
            from = f;
            to = t;
            weight = w;
        }
        private static void writeToFile(String filename, List<String> vertices, List<Edge> edges) throws IOException {
            IOUtils.GraphData data = new IOUtils.GraphData();
            data.vertices = vertices;
            data.edges = new ArrayList<>();
            for (Edge e : edges) {
                data.edges.add(new IOUtils.Edge(e.from, e.to, e.weight));
            }
            IOUtils.writeGraphToFile(filename, data);
        }
    }

    private static void writeToFile(String filename, List<String> vertices, List<Edge> edges) throws IOException {
        IOUtils.GraphData data = new IOUtils.GraphData();
        data.vertices = vertices;
        data.edges = new ArrayList<>();
        for (Edge e : edges) {
            data.edges.add(new IOUtils.Edge(e.from, e.to, e.weight));
        }
        IOUtils.writeGraphToFile(filename, data);
    }

}
