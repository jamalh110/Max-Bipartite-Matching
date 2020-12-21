import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.LinkedList;

public class Algorithm {
    public static void fillLFreeRFree(BipartiteGraph graph, AbstractCollection<Edge> matching,
            AbstractCollection<Node> lfree, AbstractCollection<Node> rfree) {
        for (Node n : graph.left) {
            lfree.add(n);
        }
        for (Node n : graph.right) {
            rfree.add(n);
        }

        for (Edge e : matching) {
            if (lfree.contains(e.getLeft())) {
                lfree.remove(e.getLeft());
            }
            if (lfree.contains(e.getRight())) {
                lfree.remove(e.getRight());
            }
            if (rfree.contains(e.getLeft())) {
                rfree.remove(e.getLeft());
            }
            if (rfree.contains(e.getRight())) {
                rfree.remove(e.getRight());
            }
        }
    }

    
    public static LinkedList<Edge> getAugmentingPathFast(BipartiteGraph graph, HashSet<Edge> matching,
            Set<Edge> unmatched, HashSet<Node> lfree, HashSet<Node> rfree) {
        // HashSet<Node> lfree = new HashSet<Node>();
        // HashSet<Node> rfree = new HashSet<Node>();
        // fillLFreeRFree(graph, matching, lfree, rfree);

        Set<Node> hitNodes = new HashSet<Node>();
        HashMap<Node, Node> map = new HashMap<Node, Node>();

        Set<Edge> unmatchedEdgeFrontier = new HashSet<Edge>();
        for (Node n : lfree) {
            unmatchedEdgeFrontier.addAll(n.edges);
            map.put(n, null);
        }
        //TODO: store free in nodes themselves 
        hitNodes.addAll(lfree);
        Set<Edge> matchedEdgeFrontier = new HashSet<Edge>();
        // boolean lookingForMatched = false
        // BFS
        Node freeNode = null;
        // switch to dfs thing later

        while (true) {
            // unmatched phase
            if (unmatchedEdgeFrontier.size() == 0) {
                break;
            }
            for (Edge e : unmatchedEdgeFrontier) {
                if (rfree.contains(e.getRight())) {
                    // found augmenting path!
                    freeNode = e.getRight();
                    map.put(freeNode, e.getLeft());
                    // set this to 0 so the outer loop breaks
                    matchedEdgeFrontier = new HashSet<Edge>();
                    break;
                } else if (!hitNodes.contains(e.getRight())) {
                    hitNodes.add(e.getRight());
                    for (Edge nodeEdge : e.getRight().edges) {
                        if (matching.contains(nodeEdge)) {
                            map.put(e.getRight(), e.getLeft());
                            matchedEdgeFrontier.add(nodeEdge);
                        }
                    }
                }
            }
            unmatchedEdgeFrontier = new HashSet<Edge>();
            if (matchedEdgeFrontier.size() == 0) {
                break;
            }

            // matched phase
            for (Edge e : matchedEdgeFrontier) {
                if (!hitNodes.contains(e.getLeft())) {
                    hitNodes.add(e.getLeft());
                    for (Edge nodeEdge : e.getLeft().edges) {
                        if (unmatched.contains(nodeEdge)) {
                            map.put(e.getLeft(), e.getRight());
                            unmatchedEdgeFrontier.add(nodeEdge);
                        }
                    }
                }
            }
            matchedEdgeFrontier = new HashSet<Edge>();

        }
        if (freeNode == null)
            return null;

        Node currentNode = freeNode;
        rfree.remove(freeNode);
        LinkedList<Node> nodePath = new LinkedList<Node>();
        while (true) {
            nodePath.add(currentNode);
            if (map.get(currentNode) == null) {
                lfree.remove(currentNode);
                break;
            }
            currentNode = map.get(currentNode);
        }

        LinkedList<Edge> edgePath = new LinkedList<Edge>();
        assert nodePath.size() > 1;
        for (int i = 0; i < nodePath.size() - 1; i++) {
            edgePath.add(nodePath.get(i).getEdge(nodePath.get(i + 1)));
        }
        return edgePath;

    }

    public static void xorMatch(AbstractCollection<Edge> matching, AbstractCollection<Edge> unmatched,
            List<Edge> path) {
        for (Edge pathEdge : path) {
            if (matching.contains(pathEdge)) {
                matching.remove(pathEdge);
                unmatched.add(pathEdge);
            } else {
                matching.add(pathEdge);
                unmatched.remove(pathEdge);
            }
        }
    }

    public static List<Edge> hopKarpFast(BipartiteGraph graph) {
        HashSet<Edge> matching = new HashSet<Edge>();
        HashSet<Edge> unmatched = new HashSet<Edge>();
        HashSet<Node> lfree = new HashSet<Node>();
        HashSet<Node> rfree = new HashSet<Node>();
        fillLFreeRFree(graph, matching, lfree, rfree);
        // todo will some heursitic of how to initially pick the edges for the algo make
        // it faster?
        // we can make it so this automatically goes for the highest weight edges
        for (Node n : graph.left) {
            for (Edge e : n.edges) {
                unmatched.add(e);
            }
        }

        LinkedList<Edge> path;
        while (true) {
            path = getAugmentingPathFastTree(graph, matching, unmatched, lfree, rfree);
            if (path != null) {
                xorMatch(matching, unmatched, path);
            } else {
                break;
            }
            //System.out.println(matching.size());
        }
        return new LinkedList<Edge>(matching);
    }


    public static List<Edge> hopKarp(BipartiteGraph graph) {
        LinkedList<Edge> matching = new LinkedList<Edge>();
        LinkedList<Edge> unmatched = new LinkedList<Edge>();
        // todo will some heursitic of how to initially pick the edges for the algo make
        // it faster?
        // we can make it so this automatically goes for the highest weight edges
        for (Node n : graph.left) {
            for (Edge e : n.edges) {
                unmatched.add(e);
            }
        }

        // boolean has_aug_path = true;
        LinkedList<Edge> path;
        while (true) {
            path = getAugmentingPath(graph, matching, unmatched);
            if (path != null) {
                xorMatch(matching, unmatched, path);
            } else {
                // has_aug_path = false;
                break;
            }
            System.out.println(matching.size());
        }
        return matching;
    }

    

    public static LinkedList<Edge> getAugmentingPath(BipartiteGraph graph, LinkedList<Edge> matching,
            LinkedList<Edge> unmatched) {
        ArrayList<Node> lfree = new ArrayList<Node>();
        ArrayList<Node> rfree = new ArrayList<Node>(); // may want to change this to better data structure later
        fillLFreeRFree(graph, matching, lfree, rfree);

        LinkedList<Node> hitNodes = new LinkedList<Node>();
        HashMap<Node, Node> map = new HashMap<Node, Node>();

        LinkedList<Edge> unmatchedEdgeFrontier = new LinkedList<Edge>();
        for (Node n : lfree) {
            unmatchedEdgeFrontier.addAll(n.edges);
            map.put(n, null);
        }

        hitNodes.addAll(lfree);
        LinkedList<Edge> matchedEdgeFrontier = new LinkedList<Edge>();
        // boolean lookingForMatched = false
        // BFS
        Node freeNode = null;
        // switch to dfs thing later

        while (true) {
            // unmatched phase
            if (unmatchedEdgeFrontier.size() == 0) {
                break;
            }
            for (Edge e : unmatchedEdgeFrontier) {
                if (rfree.contains(e.getRight())) {
                    // found augmenting path!
                    freeNode = e.getRight();
                    map.put(freeNode, e.getLeft());
                    // set this to 0 so the outer loop breaks
                    matchedEdgeFrontier = new LinkedList<Edge>();
                    break;
                } else if (!hitNodes.contains(e.getRight())) {
                    hitNodes.add(e.getRight());
                    for (Edge nodeEdge : e.getRight().edges) {
                        if (matching.contains(nodeEdge)) {
                            map.put(e.getRight(), e.getLeft());
                            matchedEdgeFrontier.add(nodeEdge);
                        }
                    }
                }
            }
            unmatchedEdgeFrontier = new LinkedList<Edge>();
            if (matchedEdgeFrontier.size() == 0) {
                break;
            }

            // matched phase
            for (Edge e : matchedEdgeFrontier) {
                if (!hitNodes.contains(e.getLeft())) {
                    hitNodes.add(e.getLeft());
                    for (Edge nodeEdge : e.getLeft().edges) {
                        if (unmatched.contains(nodeEdge)) {
                            map.put(e.getLeft(), e.getRight());
                            unmatchedEdgeFrontier.add(nodeEdge);
                        }
                    }
                }
            }
            matchedEdgeFrontier = new LinkedList<Edge>();

        }
        if (freeNode == null)
            return null;

        Node currentNode = freeNode;
        LinkedList<Node> nodePath = new LinkedList<Node>();
        while (true) {
            nodePath.add(currentNode);
            if (map.get(currentNode) == null) {
                break;
            }
            currentNode = map.get(currentNode);
        }

        LinkedList<Edge> edgePath = new LinkedList<Edge>();
        assert nodePath.size() > 1;
        for (int i = 0; i < nodePath.size() - 1; i++) {
            edgePath.add(nodePath.get(i).getEdge(nodePath.get(i + 1)));
        }
        return edgePath;

    }

}
