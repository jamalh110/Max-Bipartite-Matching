import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import java.util.LinkedList;

public class Algorithm {
    public static LinkedList<Edge> getAugmentingPath(BipartiteGraph graph, LinkedList<Edge> matching,
            LinkedList<Edge> unmatched) {
        ArrayList<Node> lfree = new ArrayList<Node>();
        ArrayList<Node> rfree = new ArrayList<Node>(); // may want to change this to better data structure later
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

        LinkedList<Node> hitNodes = new LinkedList<Node>();
        HashMap<Node,Node> map = new HashMap<Node,Node>();


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
        //switch to dfs thing later
        

        while (true) {
            // unmatched phase
            if(unmatchedEdgeFrontier.size()==0){
                break;
            }
            for (Edge e : unmatchedEdgeFrontier) {
                if (rfree.contains(e.getRight())) {
                    // found augmenting path!
                    freeNode = e.getRight();
                    map.put(freeNode, e.getLeft());
                    //set this to 0 so the outer loop breaks
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
            if(matchedEdgeFrontier.size()==0){
                break;
            }

            // matched phase
            for (Edge e : matchedEdgeFrontier) {
                if (!hitNodes.contains(e.getLeft())) {
                    hitNodes.add(e.getLeft());
                    for (Edge nodeEdge : e.getLeft().edges) {
                        if (unmatched.contains(nodeEdge)) {
                            map.put(e.getLeft(),e.getRight());
                            unmatchedEdgeFrontier.add(nodeEdge);
                        }
                    }
                } 
            }
            matchedEdgeFrontier = new LinkedList<Edge>();

        }
        if (freeNode == null) return null;
        
        Node currentNode = freeNode;
        LinkedList<Node> nodePath = new LinkedList<Node>();
        while(true){
            nodePath.add(currentNode);
            if(map.get(currentNode)==null){
                break;
            }
            currentNode = map.get(currentNode);
        }

        LinkedList<Edge> edgePath = new LinkedList<Edge>();
        assert nodePath.size() > 1;
        for(int i = 0;i<nodePath.size()-1;i++){
            edgePath.add(nodePath.get(i).getEdge(nodePath.get(i+1)));
        }
        return edgePath;

    }

    public static LinkedList<Edge> getAugmentingPathFast(BipartiteGraph graph, Set<Edge> matching,
            Set<Edge> unmatched) {
        Set<Node> lfree = new TreeSet<Node>();
        Set<Node> rfree = new TreeSet<Node>(); 
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

        Set<Node> hitNodes = new TreeSet<Node>();
        TreeMap<Node,Node> map = new TreeMap<Node,Node>();


        Set<Edge> unmatchedEdgeFrontier = new TreeSet<Edge>();
        for (Node n : lfree) {
            unmatchedEdgeFrontier.addAll(n.edges);
            map.put(n, null);
        }

        hitNodes.addAll(lfree);
        Set<Edge> matchedEdgeFrontier = new TreeSet<Edge>();
        // boolean lookingForMatched = false
        // BFS
        Node freeNode = null;
        //switch to dfs thing later
        

        while (true) {
            // unmatched phase
            if(unmatchedEdgeFrontier.size()==0){
                break;
            }
            for (Edge e : unmatchedEdgeFrontier) {
                if (rfree.contains(e.getRight())) {
                    // found augmenting path!
                    freeNode = e.getRight();
                    map.put(freeNode, e.getLeft());
                    //set this to 0 so the outer loop breaks
                    matchedEdgeFrontier = new TreeSet<Edge>();
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
            unmatchedEdgeFrontier = new TreeSet<Edge>();
            if(matchedEdgeFrontier.size()==0){
                break;
            }

            // matched phase
            for (Edge e : matchedEdgeFrontier) {
                if (!hitNodes.contains(e.getLeft())) {
                    hitNodes.add(e.getLeft());
                    for (Edge nodeEdge : e.getLeft().edges) {
                        if (unmatched.contains(nodeEdge)) {
                            map.put(e.getLeft(),e.getRight());
                            unmatchedEdgeFrontier.add(nodeEdge);
                        }
                    }
                } 
            }
            matchedEdgeFrontier = new TreeSet<Edge>();

        }
        if (freeNode == null) return null;
        
        Node currentNode = freeNode;
        LinkedList<Node> nodePath = new LinkedList<Node>();
        while(true){
            nodePath.add(currentNode);
            if(map.get(currentNode)==null){
                break;
            }
            currentNode = map.get(currentNode);
        }

        LinkedList<Edge> edgePath = new LinkedList<Edge>();
        assert nodePath.size() > 1;
        for(int i = 0;i<nodePath.size()-1;i++){
            edgePath.add(nodePath.get(i).getEdge(nodePath.get(i+1)));
        }
        return edgePath;

    }

    public static void xorMatch(AbstractCollection<Edge> matching, AbstractCollection<Edge> unmatched, List<Edge> path) {
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

        //boolean has_aug_path = true;
        LinkedList<Edge> path;
        while (true) {
            path = getAugmentingPath(graph, matching, unmatched);
            if (path != null) {
                xorMatch(matching, unmatched, path);
            } else {
                //has_aug_path = false;
                break;
            }
        }
        return matching;
    }

    public static List<Edge> hopKarpFast(BipartiteGraph graph) {
        TreeSet<Edge> matching = new TreeSet<Edge>();
        TreeSet<Edge> unmatched = new TreeSet<Edge>();
        // todo will some heursitic of how to initially pick the edges for the algo make
        // it faster?
        // we can make it so this automatically goes for the highest weight edges
        for (Node n : graph.left) {
            for (Edge e : n.edges) {
                unmatched.add(e);
            }
        }

        //boolean has_aug_path = true;
        LinkedList<Edge> path;
        while (true) {
            path = getAugmentingPathFast(graph, matching, unmatched);
            if (path != null) {
                xorMatch(matching, unmatched, path);
            } else {
                //has_aug_path = false;
                break;
            }
        }
        return new LinkedList<Edge>(matching);
    }
}
