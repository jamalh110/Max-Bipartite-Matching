import java.util.List;
import java.util.Set;
import java.util.Stack;
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

    public static LinkedList<Edge> getInitialMatching(BipartiteGraph graph, HashSet<Edge> matching, Set<Edge> unmatched,
            HashSet<Node> lfree, HashSet<Node> rfree) {
        LinkedList<Edge> matches = new LinkedList<Edge>();
        HashSet<Node> matchedRight = new HashSet<Node>();
        for (Node n : graph.left) {
            for (Edge e : n.edges) {
                if (!matchedRight.contains(e.getRight())) {
                    matches.add(e);
                    matchedRight.add(e.getRight());
                    lfree.remove(n);
                    rfree.remove(e.getRight());
                    break;
                }
            }
        }

        return matches;
    }

    public static LinkedList<Edge> getAugmentingPathFaster(BipartiteGraph graph, HashSet<Edge> matching,
            Set<Edge> unmatched, HashSet<Node> lfree, HashSet<Node> rfree) {

        Set<Node> hitNodes = new HashSet<Node>();
        HashMap<Node, Node> map = new HashMap<Node, Node>();

        Set<Edge> unmatchedEdgeFrontier = new HashSet<Edge>();
        for (Node n : lfree) {
            unmatchedEdgeFrontier.addAll(n.edges);
            map.put(n, null);
        }
        // TODO: store free in nodes themselves
        hitNodes.addAll(lfree);
        Set<Edge> matchedEdgeFrontier = new HashSet<Edge>();
        // boolean lookingForMatched = false
        // BFS
        //Node freeNode = null;
        // switch to dfs thing later
        boolean lastLayer = false;
        HashSet<Node> freeNodes = new HashSet<Node>();
        int k = 0;
        LinkedList<HashSet<Node>> layers = new LinkedList<HashSet<Node>>();
        layers.add(new HashSet<Node>());
        for(Node n:lfree){
            layers.get(0).add(n);
        }

        while (true) {
            k++;
            layers.addLast(new HashSet<Node>());
            // unmatched phase
            if (unmatchedEdgeFrontier.size() == 0) {
                break;
            }
            for (Edge e : unmatchedEdgeFrontier) {
                if (rfree.contains(e.getRight())) {
                    // found augmenting path!
                    map.put(e.getRight(), e.getLeft());
                    if(!freeNodes.contains(e.getRight())){
                        freeNodes.add(e.getRight());
                    }
                    lastLayer=true;
                } else if (!hitNodes.contains(e.getRight())) {
                    hitNodes.add(e.getRight());

                    //maybe move this?
                    layers.getLast().add(e.getRight());

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
            if(lastLayer){
                break;
            }

            k++;
            layers.addLast(new HashSet<Node>());



            // matched phase
            for (Edge e : matchedEdgeFrontier) {
                if (!hitNodes.contains(e.getLeft())) {
                    hitNodes.add(e.getLeft());
                    //mabye move this;
                    layers.getLast().add(e.getLeft());
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
        if (freeNodes.size() == 0){
            return null;
        }



        //dfs for maximal augments

        layers.remove(k);
        hitNodes = new HashSet<Node>();
        HashSet<Node> foundPathNodes = new HashSet<Node>();
        LinkedList<Edge> combinedPath = new LinkedList<Edge>();

        //System.out.println("Layers size: "+layers.size());
        //System.out.println("K size: "+k);
        //System.out.println("Free size: "+freeNodes.size());

        for(HashSet<Node> layer : layers){
            for(Node n : layer){
                //System.out.println(n.name);
            }
            //System.out.println("\n\n");
        }
        for(Node freeNode : freeNodes){
            //Node freeNode = n;
            Stack<NodeVector> stack = new Stack<NodeVector>();
            map = new HashMap<Node, Node>();
            map.put(freeNode, null);
            stack.push(new NodeVector(freeNode,k, false));
            Node foundNode = null;
            while(!stack.empty()){
                NodeVector currNode = stack.pop();
                boolean brake = false;
                for(Edge e:currNode.node.edges){
                    boolean layerboolean = false;
                    Node n = null;
                    if(currNode.isLeft){
                        layerboolean = layers.get(currNode.layer-1).contains(e.getRight()) && matching.contains(e);
                        n = e.getRight();
                    }
                    else if(!currNode.isLeft){
                        layerboolean = layers.get(currNode.layer-1).contains(e.getLeft());
                        //System.out.println(e.getLeft().name);
                        n = e.getLeft();
                    }
                    if(!hitNodes.contains(n) && layerboolean){
                        //check if you should only add this if it results in a path
                        hitNodes.add(n);
                        map.put(n, currNode.node);
                        if(lfree.contains(n)){
                            //System.out.println("found free node: " + n.name);
                            brake = true;
                            foundNode = n;
                            break;
                        }
                        else{
                            stack.push(new NodeVector(n, currNode.layer-1, !currNode.isLeft));
                        }
                    }

                }

                if(brake){
                    //System.out.println("broke");
                    foundPathNodes.add(freeNode);
                    Node currentNode = foundNode;
                    LinkedList<Node> nodePath = new LinkedList<Node>();
                    while (true) {
                        nodePath.add(currentNode);
                        if (map.get(currentNode) == null) {
                            //foundPathNodes.add(currentNode);
                            lfree.remove(foundNode);
                            break;
                        }
                        currentNode = map.get(currentNode);
                    }

                    LinkedList<Edge> edgePath = new LinkedList<Edge>();
                    assert nodePath.size() > 1;
                    for (int i = 0; i < nodePath.size() - 1; i++) {
                        edgePath.add(nodePath.get(i).getEdge(nodePath.get(i + 1)));
                    }

                    //add path
                    combinedPath.addAll(edgePath);
                    break;
                }
            }


        }

        //remove from rfree
        rfree.removeAll(foundPathNodes);
        //lfree.removeAll(foundPathNodes);

        //chnage this to hashset
        //System.out.println("Combined path size: "+combinedPath.size());
        return combinedPath;

        // Node currentNode = freeNode;
        // rfree.remove(freeNode);
        // LinkedList<Node> nodePath = new LinkedList<Node>();
        // while (true) {
        //     nodePath.add(currentNode);
        //     if (map.get(currentNode) == null) {
        //         lfree.remove(currentNode);
        //         break;
        //     }
        //     currentNode = map.get(currentNode);
        // }

        // LinkedList<Edge> edgePath = new LinkedList<Edge>();
        // assert nodePath.size() > 1;
        // for (int i = 0; i < nodePath.size() - 1; i++) {
        //     edgePath.add(nodePath.get(i).getEdge(nodePath.get(i + 1)));
        // }
        // return edgePath;

    }

    public static LinkedList<Edge> getAugmentingPathFast(BipartiteGraph graph, HashSet<Edge> matching,
            Set<Edge> unmatched, HashSet<Node> lfree, HashSet<Node> rfree) {

        Set<Node> hitNodes = new HashSet<Node>();
        HashMap<Node, Node> map = new HashMap<Node, Node>();

        Set<Edge> unmatchedEdgeFrontier = new HashSet<Edge>();
        for (Node n : lfree) {
            unmatchedEdgeFrontier.addAll(n.edges);
            map.put(n, null);
        }
        // TODO: store free in nodes themselves
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

    public static List<Edge> hopKarpFaster(BipartiteGraph graph) {
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

        LinkedList<Edge> path = new LinkedList<Edge>();

        //path = getInitialMatching(graph, matching, unmatched, lfree, rfree);
        xorMatch(matching, unmatched, path);
        //System.out.println("Size after first pass: " + matching.size());
        while (true) {
            path = getAugmentingPathFaster(graph, matching, unmatched, lfree, rfree);
            if (path != null) {
                xorMatch(matching, unmatched, path);
            } else {
                break;
            }
            // System.out.println(matching.size());
        }
        return new LinkedList<Edge>(matching);
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

        LinkedList<Edge> path = new LinkedList<Edge>();

        //path = getInitialMatching(graph, matching, unmatched, lfree, rfree);
        xorMatch(matching, unmatched, path);
        //System.out.println("Size after first pass: " + matching.size());
        while (true) {
            path = getAugmentingPathFast(graph, matching, unmatched, lfree, rfree);
            if (path != null) {
                xorMatch(matching, unmatched, path);
            } else {
                break;
            }
            // System.out.println(matching.size());
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
