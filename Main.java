import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Main {
    
    public static void main(String[] args) throws IOException{
       
    //     int[] node_sizes = {10, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 7500, 10000, 12500, 15000, 17500, 20000, 25000, 30000, 35000, 40000, 50000, 60000, 70000, 80000, 90000, 100000};
    //     int[] num_connections =  {100, 1000, 2000, 5000, 10000, 20000, 30000, 40000, 50000, 75000, 100000, 125000, 150000, 175000, 200000, 250000, 300000, 350000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000};

    //     for(int i = 0;i<node_sizes.length;i++){
    //         BipartiteGraph g = generateTestGraph(node_sizes[i], num_connections[i]);
    //         g.toFile("tests/test_"+node_sizes[i]+"/test_"+node_sizes[i]+"_"+num_connections[i]+".txt");
    //     }
    //    System.exit(0);
       // System.out.println("Generating graph");
        String mode = "normal";
        Test.testSuite("tests", mode);
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