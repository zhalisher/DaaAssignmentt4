package graph.common;

public class Metrics {
    private long startTime = 0;
    private long elapsedNanos = 0;


    public long dfsVisits = 0;
    public long dfsEdges = 0;
    public long kahnPushes = 0;
    public long kahnPops = 0;
    public long relaxations = 0;

    public void start() { startTime = System.nanoTime(); }
    public void stop() { elapsedNanos = System.nanoTime() - startTime; }
    public long elapsedNanos() { return elapsedNanos; }

    @Override
    public String toString() {
        return String.format("Time: %d ns, dfsVisits=%d, dfsEdges=%d, kahnPushes=%d, kahnPops=%d, relaxations=%d",
                elapsedNanos, dfsVisits, dfsEdges, kahnPushes, kahnPops, relaxations);
    }
}
