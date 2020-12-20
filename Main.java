import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BipartiteGraph g = generateTestGraph(1000, 5000);
        //BipartiteGraph g = test1();
        System.out.println(g.printGraph());

        List<Edge> matching  = Algorithm.hopKarp(g);
        
        for(Edge i: matching){
            System.out.println(i.getLeft().name+ " - " + i.getRight().name);
        }
        System.out.println("Matching size: " + matching.size());
        System.out.println("Bipartite Graph size: " + g.left.size());
    }


    public static BipartiteGraph test1(){
        BipartiteGraph b = new BipartiteGraph();
        for (int i = 0; i < 6; i++) {
            Node n = new Node(i);
            b.left.add(n);
        }
        for (int i = 0; i < 6; i++) {
            Node n = new Node(i+6);
            b.right.add(n);
        }

        Edge e1 = new Edge(b.left.get(0), b.right.get(0));
        Edge e2 = new Edge(b.left.get(0), b.right.get(1));
        Edge e3 = new Edge(b.left.get(0), b.right.get(2));
        Edge e4 = new Edge(b.left.get(0), b.right.get(3));
        Edge e5 = new Edge(b.left.get(0), b.right.get(4));
        Edge e6 = new Edge(b.left.get(0), b.right.get(5));


        b.left.get(0).edges.add(e2);
        b.right.get(1).edges.add(e2);

        b.left.get(0).edges.add(e3);
        b.right.get(2).edges.add(e3);

        b.left.get(0).edges.add(e1);
        b.right.get(0).edges.add(e1);
       
       
        b.left.get(0).edges.add(e5);
        b.right.get(4).edges.add(e5);

        b.left.get(0).edges.add(e6);
        b.right.get(5).edges.add(e6);

        b.left.get(0).edges.add(e4);
        b.right.get(3).edges.add(e4);


        Edge e7 = new Edge(b.left.get(1), b.right.get(0));
        Edge e8 = new Edge(b.left.get(1), b.right.get(3));
        Edge e9 = new Edge(b.left.get(1), b.right.get(2));

        b.left.get(1).edges.add(e7);
        b.right.get(0).edges.add(e7);

        b.left.get(1).edges.add(e8);
        b.right.get(3).edges.add(e8);

        b.left.get(1).edges.add(e9);
        b.right.get(2).edges.add(e9);


        Edge e10 = new Edge(b.left.get(2), b.right.get(3));
        Edge e11 = new Edge(b.left.get(2), b.right.get(1));
        b.left.get(2).edges.add(e10);
        b.right.get(3).edges.add(e10);

        b.left.get(2).edges.add(e11);
        b.right.get(1).edges.add(e11);

        Edge e12 = new Edge(b.left.get(3), b.right.get(2));
        Edge e13 = new Edge(b.left.get(3), b.right.get(0));
        Edge e14 = new Edge(b.left.get(3), b.right.get(3));

        b.left.get(3).edges.add(e12);
        b.right.get(2).edges.add(e12);

        b.left.get(3).edges.add(e13);
        b.right.get(0).edges.add(e13);

        b.left.get(3).edges.add(e14);
        b.right.get(3).edges.add(e14);

        Edge e15 = new Edge(b.left.get(4), b.right.get(2));

        b.left.get(4).edges.add(e15);
        b.right.get(2).edges.add(e15);

        Edge e16 = new Edge(b.left.get(5), b.right.get(5));

        b.left.get(1).edges.add(e16);
        b.right.get(5).edges.add(e16);

        return b;
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