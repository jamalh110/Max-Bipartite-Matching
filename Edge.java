public class Edge implements Comparable<Edge> {
    private Node left;
    private Node right;
    private int weight = 1;
    public Edge(Node left, Node right){
        this.left = left;
        this.right= right;
    }
    
    public int getWeight() { 
        return weight;
    };
    public Node getLeft(){
        return left;
    }
    public Node getRight(){
        return right;
    }

    @Override
    public int compareTo(Edge e) {
        if (left.name != e.getLeft().name) return Integer.compare(left.name, e.getLeft().name);
        if (right.name != e.getRight().name) return Integer.compare(right.name, e.getRight().name);
        return 0;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }
    public String toStringTest() {
        return left + " " + right;
    }
}
