package p3.solver;

import p3.graph.Edge;
import p3.graph.Graph;

import java.util.*;

/**
 * Implementation of Kruskal's algorithm, a minimum spanning tree algorithm.
 * @param <N> The type of the nodes in the graph.
 */
public class KruskalMSTCalculator<N> implements MSTCalculator<N> {

    /**
     * Factory for creating new instances of {@link KruskalMSTCalculator}.
     */
    public static MSTCalculator.Factory FACTORY = KruskalMSTCalculator::new;

    /**
     * The graph to calculate the MST for.
     */

    protected final Graph<N> graph;

    /**
     * The edges in the MST.
     */
    protected final Set<Edge<N>> mstEdges;

    /**
     * The groups of nodes in the MST.
     * <p> Each group is represented by a set of nodes. Initially, each node is in its own group. </p>
     * <p> When two nodes are in the same groups, they are in the same MST which is created by {@link #mstEdges}. </p>
     * <p> Every node is in exactly one group. </p>
     */
    protected final List<Set<N>> mstGroups;

    /**
     * Construct a new {@link KruskalMSTCalculator} for the given graph.
     * @param graph the graph to calculate the MST for.
     */
    public KruskalMSTCalculator(Graph<N> graph) {
        this.graph = graph;
        this.mstEdges = new HashSet<>();
        this.mstGroups = new ArrayList<>();
    }
    @Override
    public Graph<N> calculateMST() {
        init();
        Set<Edge<N>> edges = graph.getEdges();
        for (Edge<N> edge : edges) {
            if (acceptEdge(edge)) mstEdges.add(edge);
        }
        return Graph.of(graph.getNodes(), mstEdges);
    }

    /**
     * Initializes the {@link #mstEdges} and {@link #mstGroups} with their default values.
     * <p> Initially, {@link #mstEdges} is empty and {@link #mstGroups} contains a set for each node in the graph.
     */
    protected void init() {
        mstEdges.clear();
        Set<N> nodes = graph.getNodes();
        for (N node : nodes) {
            Set<N> setOfNode = new HashSet<>();
            setOfNode.add(node);
            mstGroups.add(setOfNode);
        }
    }

    /**
     * Processes an edge during Kruskal's algorithm.
     * <p> If the edge's nodes are in the same MST (group), the edge is skipped.
     * <p> If the edge's nodes are in different MSTs (groups), the groups are merged via the {@link #joinGroups(int, int)} method.
     *
     * @param edge The edge to process.
     * @return {@code true} if the edge was accepted and the two MST's were merged,
     * {@code false} if it was skipped.
     */
    protected boolean acceptEdge(Edge<N> edge) {
        N nodeA = edge.a(), nodeB = edge.b();
        int aIndex = 0, bIndex = 0;

        for (int i = 0; i < mstGroups.size(); i++) {
            if (mstGroups.get(i).contains(nodeA) && mstGroups.get(i).contains(nodeB)) return false;
            if (mstGroups.get(i).contains(nodeA)) aIndex = i;
            if (mstGroups.get(i).contains(nodeB)) bIndex = i;
        }
        joinGroups(aIndex, bIndex);
        return true;
    }

    /**
     * Joins two sets in the list of all MST Groups.
     * <p> After joining the larger set will additionally contain all elements of the smaller set and
     * the smaller set will be removed from the list.
     *
     * @param aIndex The index of the first set to join.
     * @param bIndex The index of the second set to join.
     */
    protected void joinGroups(int aIndex, int bIndex) {
        if (mstGroups.get(aIndex).size() > mstGroups.get(bIndex).size()) {
            mstGroups.get(aIndex).addAll(mstGroups.get(bIndex));
            mstGroups.remove(bIndex);
        } else {
            mstGroups.get(bIndex).addAll(mstGroups.get(aIndex));
            mstGroups.remove(aIndex);
        }

    }
}
