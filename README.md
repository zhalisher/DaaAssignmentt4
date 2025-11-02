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

   <img width="453" height="288" alt="Снимок экрана 2025-11-03 004402" src="https://github.com/user-attachments/assets/fdfc5b11-8061-4d37-b8d5-c548eee5b726" />
