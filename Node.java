import java.util.ArrayList;

public class Node implements Comparable<Node> {
    int name;
    boolean matched = false;
    ArrayList<Edge> edges = new ArrayList<Edge>();
    public Node(int name){
        this.name=name;
    }
    public boolean edgeExists(Node node){
        for (Edge i:edges){
            if(i.getLeft()==node || i.getRight()==node){
                return true;
            }
        }
        return false;
    }

    public boolean edgeExists(int name){
        for (Edge i:edges){
            if(i.getLeft().name==name || i.getRight().name==name){
                return true;
            }
        }
        return false;
    }

    public Edge getEdge(Node node){
        for (Edge i:edges){
            if(i.getLeft()==node || i.getRight()==node){
                return i;
            }
        }
        return null;
    }

    public String printNode(){
        String p = ""+this.name+" ";
        for(Edge edge: edges){
            if(edge.getRight()!=this){
                p+= (" connected to node " + edge.getRight().name + ", ");
            }
        }
        
        return p;
    }

    @Override
    public int compareTo(Node n) {
        return Integer.compare(name, n.name);
    }

    @Override
    public String toString() {
        return Integer.toString(name);
    }
}
