This report analyzes the performance of graph algorithms (SCC detection, topological sorting, and shortest/longest paths in DAGs) applied to smart city task scheduling problems. The implementation successfully processes graphs ranging from 6 to 50 nodes, demonstrating scalable performance across various graph structures.
1. Objective
The goal of this assignment was to integrate two core graph algorithm topics — Strongly Connected Components (SCC) and Shortest Paths in Directed Acyclic Graphs (DAG) — in a unified practical context related to Smart City Scheduling.
Aim:
Detect and compress cyclic dependencies between service tasks.
Perform topological ordering on the condensed DAG.
Compute shortest and longest (critical) paths for optimal task planning.

2. Algorithms Implemented
   
| Task                | Algorithm                    | Package       | Key Methods                   |
| ------------------- | ---------------------------- | ------------- | ----------------------------- |
| SCC Detection       | **Tarjan’s Algorithm**       | `graph.scc`   | `TarjanSCC.run()`             |
| Condensation Graph  | Custom DAG builder           | `graph`       | `Condensation.build()`        |
| Topological Sorting | **Kahn’s Algorithm**         | `graph.topo`  | `TopologicalSort.kahnOrder()` |
| DAG Shortest Path   | **Dynamic Programming (DP)** | `graph.dagsp` | `DagSP.shortestFrom()`        |
| DAG Longest Path    | **Sign Inversion + DP**      | `graph.dagsp` | `DagSP.longestFrom()`         |

Metrics were recorded using the graph.common.Metrics interface, tracking:
DFS visits / edges (SCC)
Pushes / pops (Kahn)
Relaxations (DAG-SP)
Execution time via System.nanoTime()

3. Dataset Description
Nine datasets were generated automatically under /data/:

| Category | File Count | Node Range | Edge Range | Description                  |
| -------- | ---------- | ---------- | ---------- | ---------------------------- |
| Small    | 3          | 6–9        | 5–8        | Simple, 1–2 cycles           |
| Medium   | 3          | 12–18      | 15–27      | Mixed cyclic & acyclic       |
| Large    | 3          | 35–50      | 37–313     | Dense graphs for performance |

Each file (tasks_*.json) defines vertices and directed weighted edges (weights = task duration or dependency cost).

4. Experimental Results
   
| Dataset  | Vertices | Edges | SCCs | Condensation Nodes | Critical Path Length |
| -------- | -------- | ----- | ---- | ------------------ | -------------------- |
| small_1  | 6        | 5     | 7    | 7                  | **5.0**              |
| small_2  | 7        | 7     | 6    | 6                  | **5.5**              |
| small_3  | 8        | 8     | 4    | 4                  | **0.0**              |
| medium_1 | 15       | 15    | 13   | 13                 | **3.0**              |
| medium_2 | 12       | 27    | 13   | 13                 | **0.0**              |
| medium_3 | 18       | 20    | 14   | 14                 | **4.5**              |
| large_1  | 35       | 37    | 33   | 33                 | **0.0**              |
| large_2  | 40       | 313   | 41   | 41                 | **18.94**            |
| large_3  | 50       | 311   | 32   | 32                 | **11.80**            |

5. Analysis
5.1 SCC Detection

Tarjan’s algorithm efficiently found all SCCs in O(V + E) time.
Most graphs were nearly acyclic, as indicated by SCC counts ≈ number of vertices.
No large strongly connected subgraphs were observed — this fits the expected “task dependency” model (few mutual dependencies).

5.2 Condensation and Topological Sorting
The condensation step correctly built reduced DAGs for further planning.
Kahn’s algorithm produced valid topological orders for all 9 datasets.
Both algorithms executed in microseconds, with negligible overhead compared to path computation.

5.3 Shortest & Longest Paths (DAG-SP)
The shortest paths represent optimal low-cost dependency sequences.
The longest paths represent the critical path — the minimal time to complete all dependent tasks.
Dense graphs (e.g., large_2, large_3) produced the longest critical paths due to more edges and dependency chains.

| Category | Avg Critical Path Length | Notes                         |
| -------- | ------------------------ | ----------------------------- |
| Small    | 3.5                      | Mostly short task chains      |
| Medium   | 2.5                      | Few larger SCCs               |
| Large    | **10.3**                 | Dense, more cumulative weigh  |

6. Performance and Bottlenecks
   
| Algorithm | Complexity | Observed Bottleneck                     |
| --------- | ---------- | --------------------------------------- |
| TarjanSCC | O(V + E)   | DFS recursion overhead for large graphs |
| Kahn Sort | O(V + E)   | None significant                        |
| DAG-SP    | O(V + E)   | Relaxations for dense grphs(E>V         |

7. Conclusions & Recommendations
SCC compression is critical before scheduling — it eliminates cyclic dependencies and ensures DAG properties for valid planning.
Topological sorting is ideal for dependency-based task scheduling due to simplicity and linear complexity.
DAG shortest/longest path algorithms provide efficient route optimization without needing general-purpose Dijkstra or Bellman-Ford.
For dense city networks, focus optimization on edge relaxation performance.
For real-world use, edge weights should represent real durations, allowing the longest path to model total project completion time.

8. Weight Model
All computations use edge weights, representing task duration or dependency cost.
If multiple edges exist between two components, the minimum weight is retained.

