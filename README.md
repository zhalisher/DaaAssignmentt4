This project implements and analyzes graph algorithms for smart city task scheduling, focusing on Strongly Connected Components (SCC), topological ordering, and shortest/longest paths in Directed Acyclic Graphs (DAGs). The system processes task dependency graphs ranging from 6 to 50 nodes, demonstrating efficient performance across various graph structures while providing optimal scheduling solutions.

1. Introduction
1.1 Project Overview

The assignment integrates two core graph algorithm topics - SCC detection and DAG path algorithms - in a unified smart city scheduling context. The system processes task dependencies to:
    Detect and compress cyclic dependencies using SCC
    Perform topological ordering on the condensed DAG
    Compute optimal scheduling paths using shortest and longest path algorithms

1.2 Problem Domain

Smart city services involve complex task dependencies where:
    Street cleaning, repairs, and maintenance tasks have precedence constraints
    Some dependencies form cycles (mutual dependencies)
    Optimal scheduling requires critical path analysis
    Resource allocation benefits from dependency compression

2. Algorithms and Implementation

    Algorithm	Purpose	Complexity	Choice Rationale
    Tarjan's Algorithm	SCC Detection	O(V + E)	Linear time, single DFS, efficient recursion
    Kahn's Algorithm	Topological Sort	O(V + E)	Intuitive, works well with condensation graphs
    Dynamic Programming	DAG Shortest/Longest Paths	O(V + E)	Optimal for DAGs, utilizes topological order
    <img width="486" height="289" alt="изображение" src="https://github.com/user-attachments/assets/c77b7fde-fa4d-44ca-950b-c7d19815528a" />



2.2 Implementation Architecture
src/
├── main/java/graph/
│   ├── scc/TarjanSCC.java and test of it   
│   ├── topo/TopologicalSort.java  and test of it
│   ├── dagsp/DagSP.java          and test of it     
│   ├── Condensation.java           
│   ├── common/                     
│   │   ├── IOUtils.java
│   │   └── Metrics.java
│   └── main/Main.java             
|             
└── datagen/DatasetGenerator.java   
2.3 Key Implementation Details

Weight Model: The implementation uses edge weights representing task duration or dependency cost. Weights are preserved during condensation by taking minimum weights between components.

Metrics Tracking: Comprehensive instrumentation tracks:
    DFS visits and edges (SCC)
    Queue operations (Kahn's algorithm)
    Edge relaxations (path algorithms)
    Execution time via System.nanoTime()

3. Dataset Generation

    Category	Node Range	Edge Range	Variants	Characteristics
    Small	6--10	5--8	3	Simple cycles, basic DAGs
    Medium	10--20	15-27	3	Mixed structures, multiple SCCs
    Large	20-50	37-313	3	Dense graphs, performance testing
<img width="621" height="289" alt="изображение" src="https://github.com/user-attachments/assets/a2a612cb-5eab-436f-a226-8b105ac26a51" />

3.2 Dataset Details
    Dataset	Vertices	Edges	SCCs	Condensation Nodes	Density
    small_1	6	5	7	7	Sparse
    small_2	7	7	6	6	Medium
    small_3	8	8	4	4	Medium
    medium_1	15	15	13	13	Sparse
    medium_2	12	27	13	13	Dense
    medium_3	18	20	14	14	Sparse
    large_1	35	37	33	33	Sparse
    large_2	40	313	41	41	Very Dense
    large_3	50	311	32	32	Dense
    <img width="756" height="265" alt="изображение" src="https://github.com/user-attachments/assets/677d0f97-1d64-4933-8c77-62d0b3e20100" />
    
Observations:
    Most graphs show high SCC counts relative to vertices, indicating predominantly acyclic structures
    small_3 and large_3 show significant cycle compression (4 and 32 SCCs from 8 and 50 vertices respectively)
    large_2 has the highest density with 313 edges among 40 vertices

4. Experimental Results
4.1 SCC Detection and Condensation Results
   
   Dataset	Vertices	Edges	SCCs Found	Condensation Nodes	Compression Ratio
    small_1	6	5	7	7	0% (Pure DAG)
    small_2	7	7	6	6	14.3%
    small_3	8	8	4	4	50.0%
    medium_1	15	15	13	13	13.3%
    medium_2	12	27	13	13	-8.3%*
    medium_3	18	20	14	14	22.2%
    large_1	35	37	33	33	5.7%
    large_2	40	313	41	41	-2.5%*
    large_3	50	311	32	32	36.0%
    <img width="836" height="265" alt="изображение" src="https://github.com/user-attachments/assets/a6c08922-8ae9-4205-9f02-927af1f380d0" />

Key Findings:
    small_3 achieved 50% compression through multiple 2-node cycles
    large_3 showed 36% compression, indicating significant cyclic structures
    medium_2 and large_2 have near 1:1 vertex-to-component mapping, suggesting highly acyclic structures despite dense connectivity

4.2 Critical Path Analysis
    Dataset	Critical Path Length	Critical Path Components	Path Interpretation
    small_1	5.0	[5, 4, 3, 2]	4-component linear chain
    small_2	5..5	[4, 3, 2, 1, 0]	5-component path through cycle
    small_3	0.0	[0]	Single component, no path cost
    medium_1	3.0	[4, 1, 0]	3-component medium chain
    medium_2	0.0	[0]	Source component only
    medium_3	4..5	[3, 2, 1, 0]	4-component path
    large_1	0.0	[0]	Isolated source
    large_2	18.94	[37, 36, 34, 33, 32, 31, 28, 24, 22, 12, 10, 8, 7, 1, 0]	15-component complex chain
    large_3	11..80	[30, 29, 28, 11, 8, 4, 2, 1, 0]	9-component mixed path
    <img width="662" height="481" alt="изображение" src="https://github.com/user-attachments/assets/afacebcb-fb66-4f56-aa41-dfbbf1007490" />

4.3 Structural Analysis and Performance Patterns
    Dataset	Edge Density	SCC Pattern	Critical Path Characteristics
    small_1	16.7%	All single nodes	Linear dependency chain
    small_2	16.7%	One 3-node cycle	Path through cyclic component
    small_3	14.3%	Multiple 2-node cycles	Self-contained components
    medium_1	7.1%	Mostly single nodes	Short critical path
    medium_2	40.9%	All single nodes	Dense but acyclic
    medium_3	6.5%	Small SCCs	Medium-length path
    large_1	3.1%	Mostly single nodes	Sparse connections
    large_2	20.1%	All single nodes	Very long critical path
    large_3	12.7%	Mixed sizes	Balanced path length
    <img width="768" height="337" alt="изображение" src="https://github.com/user-attachments/assets/4546fefc-ff49-431d-8a11-19899527db3e" />

5. Performance Analysis
5.1 Algorithm Bottlenecks
    Algorithm	Primary Bottleneck	Observed Impact	Optimization Potential
    Tarjan's SCC	DFS recursion depth	Minimal for tested sizes	Iterative DFS for very large graphs
    Kahn's Topo Sort	Queue operations	Linear scaling	Already optimal
    DAG-SP	Edge relaxations	Significant for dense graphs	Parallel processing
    <img width="768" height="169" alt="изображение" src="https://github.com/user-attachments/assets/14a5119b-11b5-4996-a214-b485b2dfb1dc" />

5.2 Structural Impact Analysis
5.2.1 Density Effects
    Low density (<10%): Linear performance, minimal memory usage (small_1, medium_1, large_1)
    Medium density (10-20%): Balanced performance (small_2, small_3, large_3)
    High density (>20%): Increased computation time (medium_2, large_2)

5.2.2 SCC Distribution Patterns
    Pure DAGs: 1:1 vertex-to-component ratio (small_1, medium_2, large_2)
    Mixed structures: Moderate compression (small_2, medium_1, medium_3)
    Cyclic-rich: High compression ratios (small_3, large_3)

5.3 Critical Path Insights

Longest Paths:
    large_2: 18.94 units through 15 components - represents complex dependency chains
    large_3: 11.80 units through 9 components - balanced cyclic/acyclic mix
    small_2: 5.5 units - demonstrates cycle traversal cost

Zero-Length Paths: Occur when source component has no outgoing edges to other components, indicating isolated task groups.
6. Conclusions and Recommendations
6.1 Algorithm Selection Guidelines
    Use Case	Recommended Algorithm	Rationale
    Cyclic Dependency Detection	Tarjan's SCC	Linear time, identifies all cycles
    Task Scheduling	Kahn's + DAG-SP	Natural fit for dependency ordering
    Critical Path Analysis	Longest Path in DAG	Models project completion time
    Large Dense Graphs	Iterative DFS variants	Avoids recursion depth issues
    <img width="539" height="265" alt="изображение" src="https://github.com/user-attachments/assets/a5ebb7c0-6369-4bfb-9ba3-539350245ff9" />

6.2 Practical Recommendations for Smart City Scheduling
    Preprocessing: Always run SCC compression before scheduling - achieved 36-50% compression in cyclic cases
    Resource Allocation: Critical path lengths (0-18.94 units) provide realistic timeline estimates
    Parallelization: Independent SCCs can be scheduled concurrently
    Monitoring: High edge density (>20%) may indicate over-constrained systems

6.3 Performance Insights
    Scalability: Algorithms handle 50-node graphs efficiently with complex structures
    Compression Benefits: SCC reduction significantly simplifies scheduling complexity
    Real-time Viability: Linear complexity enables dynamic rescheduling

6.4 Key Technical Findings
    Cycle Impact: Even small cycles (2-3 nodes) can significantly affect critical paths
    Density vs Complexity: High edge count doesn't necessarily imply complex dependencies
    Compression Efficiency: Best results with balanced cyclic-acyclic mixes

7. Technical Specifications
    Weight Model Documentation
The system uses edge weights to represent task durations or dependency costs. During SCC condensation, when multiple edges exist between components, the minimum weight is preserved to ensure optimal path calculations.

8. Acknowledgments

This implementation demonstrates the practical application of graph algorithms in urban planning and resource scheduling contexts. The experimental results validate the effectiveness of SCC compression and critical path analysis for complex dependency management in smart city applications.
