package graph.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class IOUtils {
    public static class GraphData {
        public List<String> vertices;
        public List<Edge> edges;

        public GraphData() {
            vertices = new ArrayList<>();
            edges = new ArrayList<>();
        }
    }

    public static class Edge {
        public String from;
        public String to;
        public double weight;

        public Edge() {}
        public Edge(String from, String to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static GraphData readGraphFromFile(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename))).trim();
        GraphData data = new GraphData();

        // parser json
        content = content.replaceAll("\\s+", " ");


        int verticesStart = content.indexOf("\"vertices\":") + 10;
        int verticesEnd = content.indexOf("]", verticesStart) + 1;
        String verticesStr = content.substring(verticesStart, verticesEnd).trim();
        data.vertices = parseStringArray(verticesStr);

        int edgesStart = content.indexOf("\"edges\":") + 8;
        int edgesEnd = content.indexOf("]", edgesStart) + 1;
        String edgesStr = content.substring(edgesStart, edgesEnd).trim();
        data.edges = parseEdgesArray(edgesStr);

        return data;
    }

    private static List<String> parseStringArray(String arrayStr) {
        List<String> result = new ArrayList<>();
        // brackets
        String clean = arrayStr.substring(1, arrayStr.length() - 1).trim();
        if (clean.isEmpty()) return result;

        String[] parts = clean.split(",");
        for (String part : parts) {
            //quotes
            String item = part.trim().replaceAll("^\"|\"$", "");
            result.add(item);
        }
        return result;
    }

    private static List<Edge> parseEdgesArray(String arrayStr) {
        List<Edge> result = new ArrayList<>();
        // brackets
        String clean = arrayStr.substring(1, arrayStr.length() - 1).trim();
        if (clean.isEmpty()) return result;


        String[] edgeObjects = clean.split("\\},\\s*\\{");

        for (String edgeObj : edgeObjects) {
            edgeObj = edgeObj.replaceAll("^\\{", "").replaceAll("\\}$", "");
            Edge edge = new Edge();

            String[] properties = edgeObj.split(",");
            for (String prop : properties) {
                String[] keyValue = prop.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
                    String value = keyValue[1].trim();

                    switch (key) {
                        case "from":
                            edge.from = value.replaceAll("^\"|\"$", "");
                            break;
                        case "to":
                            edge.to = value.replaceAll("^\"|\"$", "");
                            break;
                        case "weight":
                            edge.weight = Double.parseDouble(value);
                            break;
                    }
                }
            }
            result.add(edge);
        }
        return result;
    }

    public static void writeGraphToFile(String filename, GraphData data) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"vertices\": ").append(stringListToJson(data.vertices)).append(",\n");
        json.append("  \"edges\": [\n");
        for (int i = 0; i < data.edges.size(); i++) {
            Edge e = data.edges.get(i);
            json.append("    {\"from\": \"").append(e.from)
                    .append("\", \"to\": \"").append(e.to)
                    .append("\", \"weight\": ").append(e.weight).append("}");
            if (i < data.edges.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ]\n}");

        Files.write(Paths.get(filename), json.toString().getBytes());
    }

    private static String stringListToJson(List<String> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("\"").append(list.get(i)).append("\"");
            if (i < list.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
