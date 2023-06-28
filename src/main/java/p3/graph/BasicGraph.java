package p3.graph;

import p3.SetUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A basic implementation of an immutable {@link Graph} that uses a {@link Map} to store the edges that are adjacent to each node.
 * @param <N>
 */
public class BasicGraph<N> implements Graph<N> {

    /**
     * A map from nodes to the edges that are adjacent to them.
     * If a node has no adjacent edges, it is mapped to an empty set.
     */
    protected final Map<N, Set<Edge<N>>> backing;

    /**
     * The nodes in this graph.
     */
    protected final Set<N> nodes;

    /**
     * The edges in this graph.
     */
    protected final Set<Edge<N>> edges;

    /**
     * Constructs a new empty {@link BasicGraph}.
     */
    public BasicGraph() {
        this(Set.of(), Set.of());
    }

    /**
     * Constructs a new {@link BasicGraph} with the given nodes and edges.
     * @param nodes the nodes.
     * @param edges the edges.
     */
    public BasicGraph(Set<N> nodes, Set<Edge<N>> edges) {
        this.nodes = SetUtils.immutableCopyOf(nodes);
        this.edges = SetUtils.immutableCopyOf(edges);
        this.backing = new HashMap<>();
        for (Edge<N> edge: edges) {
            for (N node : nodes) {
                if (edge.a().equals(node) || edge.b().equals(node)) {
                    if (backing.containsKey(node)) {
                        backing.get(node).add(edge);
                    }
                    else {
                        Set<Edge<N>> setOfEdges = new HashSet<>();
                        setOfEdges.add(edge);
                        backing.put(node, setOfEdges);
                    }
                }
            }
        }
    }

    @Override
    public Set<N> getNodes() {
        return nodes;
    }

    @Override
    public Set<Edge<N>> getEdges() {
        return edges;
    }

    @Override
    public Set<Edge<N>> getAdjacentEdges(N node) {
        Set<Edge<N>> result = backing.get(node);
        if (result == null) {
            throw new IllegalArgumentException("Node not found: " + node);
        }
        return result;
    }

    @Override
    public MutableGraph<N> toMutableGraph() {
        return MutableGraph.of(nodes, edges);
    }

    @Override
    public Graph<N> toGraph() {
        return this;
    }

    /**
     * An empty immutable {@link Graph}.
     */
    static Supplier<Graph<Object>> EMPTY = () -> new BasicGraph<>(Set.of(), Set.of());
}
