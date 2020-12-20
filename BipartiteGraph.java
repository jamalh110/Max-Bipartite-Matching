import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BipartiteGraph {
    public ArrayList<Node> left = new ArrayList<Node>();
    public ArrayList<Node> right = new ArrayList<Node>();

    public static BipartiteGraph readGraph(String path) {
        // reads in a file at path
        // format (number of nodes on first line then all edges):
        // n
        // v1 v2
        // v3 v4
        // ...
        File file = new File(path);
        BipartiteGraph b = new BipartiteGraph();
        try {
            Scanner reader = new Scanner(file);

            int num_nodes = Integer.parseInt(reader.nextLine());
            assert num_nodes % 2 == 0;
            for (int i = 0; i < num_nodes / 2; i++) {
                Node n = new Node(i);
                b.left.add(n);
            }
            for (int i = num_nodes / 2; i < num_nodes; i++) {
                Node n = new Node(i);
                b.right.add(n);
            }

            while (reader.hasNextLine()) {
                String[] s = reader.nextLine().split(" ");
                int l = Integer.parseInt(s[0]);
                int r = Integer.parseInt(s[1]);
                if (l >= num_nodes/2) { // swapped
                    int tmp = r;
                    r = l;
                    l = tmp;
                } 
                Node left = b.left.get(l);
                Node right = b.right.get(r-(num_nodes/2));
                Edge e = new Edge(left, right);
                left.edges.add(e);
                right.edges.add(e);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file");
        }
        return b;
    }

    public String printGraph(boolean testOutput) {
        String print = "";
        if(testOutput){
            print+=this.left.size()*2+"\n";
        }
        else{
        print += ("size of left: " + left.size());
        print += ("\nsize of right: " + right.size()) + "\n";
        }
        for (Node node : left) {
            for (Edge e : node.edges) {
                if(testOutput){
                    print += e.toStringTest() + "\n";
                }
                else{
                    print += e.toString() + "\n";
                }
            }
            // print += (node.printNode()) + "\n";
        }
        return print;
    }
}
