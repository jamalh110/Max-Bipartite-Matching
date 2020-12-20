import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BipartiteGraph {
    public ArrayList<Node> left = new ArrayList<Node>();
    public ArrayList<Node> right = new ArrayList<Node>();

    public BipartiteGraph() {

    }

    public BipartiteGraph(String path) {
        // reads in a file at path
        // format (number of nodes on first line then all edges):
        // n
        // v1 v2
        // v3 v4
        // ...
        File file = new File(path);
        try {
            Scanner reader = new Scanner(file);

            int num_nodes = Integer.parseInt(reader.nextLine());
            assert num_nodes % 2 == 0;
            for (int i = 0; i < num_nodes / 2; i++) {
                Node n = new Node(i);
                left.add(n);
            }
            for (int i = num_nodes / 2; i < num_nodes; i++) {
                Node n = new Node(i);
                right.add(n);
            }

            while (reader.hasNextLine()) {
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(reader.nextLine());
                m.find();
                int l = Integer.parseInt(m.group(0));
                m.find();
                int r = Integer.parseInt(m.group(0));
                if (l >= num_nodes / 2) { // swapped
                    int tmp = r;
                    r = l;
                    l = tmp;
                }
                Node left_node = left.get(l);
                Node right_node = right.get(r - (num_nodes / 2));
                Edge e = new Edge(left_node, right_node);
                left_node.edges.add(e);
                right_node.edges.add(e);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file");
        }
    }

    public void toFile(String path) {
        String s = Integer.toString(left.size() * 2);
        for (Node node : left) {
            for (Edge e : node.edges) {
                s += "\n" + e.toString();
            }
        }
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file...");
        }
    }

    public String printGraph(boolean testOutput) {
        String print = "";
        if (testOutput) {
            print += this.left.size() * 2 + "\n";
        } else {
            print += ("size of left: " + left.size());
            print += ("\nsize of right: " + right.size()) + "\n";
        }
        for (Node node : left) {
            for (Edge e : node.edges) {
                if (testOutput) {
                    print += e.toStringTest() + "\n";
                } else {
                    print += e.toString() + "\n";
                }
            }
        }
        return print;
    }
}
