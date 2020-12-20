import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BipartiteGraph {
    public ArrayList<Node> left = new ArrayList<Node>();
    public ArrayList<Node> right = new ArrayList<Node>();

    // change this to constructor
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
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(reader.nextLine());
                m.find();
                int l = Integer.parseInt(m.group(0));
                m.find();
                int r = Integer.parseInt(m.group(0));
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

    public String printGraph() {
        String print = "";
        print += ("size of left: " + left.size());
        print += ("\nsize of right: " + right.size()) + "\n";
        for (Node node : left) {
            for (Edge e : node.edges) {
                print += e.toString() + "\n";
            }
            // print += (node.printNode()) + "\n";
        }
        return print;
    }
}
