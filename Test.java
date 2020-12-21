import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Test {
    // mode options: "normal", "fast", "faster"
    public static void testFile(String path, String mode) {
        BipartiteGraph g = new BipartiteGraph(path);
        long time = 0L;
        List<Edge> matching = new LinkedList<Edge>();
        long start = System.currentTimeMillis();
        switch (mode) {
            case "normal":
                matching = Algorithm.hopKarp(g);
                time = System.currentTimeMillis() - start;
                break;
            case "fast":
                matching = Algorithm.hopKarpFast(g);
                time = System.currentTimeMillis() - start;
                break;
            case "faster":
                matching = Algorithm.hopKarpFaster(g);
                time = System.currentTimeMillis() - start;
                break;
            default:
                System.out.println("Unsupported mode");
        }
        System.out.println("File: "+ path);
        System.out.println("Matching took: " + time + " ms");
        System.out.println("Matching size: " + matching.size());
    }

    public static void testDirectory(String path, String mode) {
        File[] files = new File(path).listFiles();
        for(File f: files) {
            testFile(f.getPath(), mode);
            System.out.println();
        }
    }
}
