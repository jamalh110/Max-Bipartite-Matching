import java.util.ArrayList;

public class BipartiteGraph {
    public ArrayList<Node> left = new ArrayList<Node>();
    public ArrayList<Node> right = new ArrayList<Node>();

    public String printGraph(){
        String print = "";
        print+=("size of left: " + left.size());
        print+=("\nsize of right: " + right.size()) + "\n";
        for(Node node: left){
            print+=(node.printNode()) + "\n";
        }
        return print;
    }
}




















