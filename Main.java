import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BipartiteGraph g = generateTestGraph(6, 12);
        System.out.println(g.printGraph());
    }

    public static BipartiteGraph generateTestGraph(int num_nodes, int num_connections) {
        BipartiteGraph b = new BipartiteGraph();
        for (int i = 0; i < num_nodes; i++) {
            Node n = new Node(i);
            b.left.add(n);
        }
        for (int i = 0; i < num_nodes; i++) {
            Node n = new Node(i+num_nodes);
            b.right.add(n);
        }

        for (int i = 0; i < num_connections; i++) {
            Random r = new Random();
            Node left = b.left.get(r.nextInt(num_nodes));
            Node right = b.right.get(r.nextInt(num_nodes));
            if(!left.edgeExists(right)){
                Edge e = new Edge(left, right);
                left.edges.add(e);
                right.edges.add(e);
            }
        }
        return b;

    }

}