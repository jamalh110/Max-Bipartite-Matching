public class Edge {
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
}
