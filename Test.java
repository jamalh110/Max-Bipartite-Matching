import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Test {
    // mode options: "normal", "fast", "faster"
    public static long testFile(String path, String mode) {
        int times = 5;
        BipartiteGraph g = new BipartiteGraph(path);
        long time = 0L;
        List<Edge> matching = new LinkedList<Edge>();
        long start = System.currentTimeMillis();
        switch (mode) {
            case "normal":
                for (int i = 0; i < times; i++) {
                    matching = Algorithm.hopKarp(g);
                }
                time = System.currentTimeMillis() - start;
                break;
            case "fast":
                for (int i = 0; i < times; i++) {
                    matching = Algorithm.hopKarpFast(g);
                }
                time = System.currentTimeMillis() - start;
                break;
            case "faster":
                for (int i = 0; i < times; i++) {
                    matching = Algorithm.hopKarpFaster(g);
                }
                time = System.currentTimeMillis() - start;
                break;
            default:
                System.out.println("Unsupported mode");
        }
        System.out.println(path+","+time/times);
        //System.out.println("File: " + path);
        //System.out.println("Matching took: " + time/times + " ms");
        //System.out.println(time/times + "\n");
        //System.out.println("Matching size: " + matching.size());
        return time;
    }

    public static double testDirectory(String path, String mode) {
        //System.out.println("Evaluating: " + path);
        //System.out.print("path, ");
        File[] files = new File(path).listFiles();
        long sum = 0;
        double count = 0.0;
        for (File f : files) {
            sum += testFile(f.getPath(), mode);
            count++;
        }
        double avg = sum / count;
        //System.out.format("Average time: %.3f ms\n\n", avg);
        return avg;
    }

    public static void testSuite(String path, String mode) {
        File[] files = new File(path).listFiles();
        for (File f : files) {
            if(f.getName().contains("2000")
            || f.getName().contains("3000") 
            || f.getName().contains("4000") 
            || f.getName().contains("5000") 
            || f.getName().contains("7500") 
            || f.getName().contains("10000") 
            || f.getName().contains("12500") 
            || f.getName().contains("15000") 
            || f.getName().contains("17500") 
            || f.getName().contains("20000") 
            || f.getName().contains("25000")
            || f.getName().contains("30000")
            || f.getName().contains("35000")
            || f.getName().contains("40000")
            || f.getName().contains("50000")
            || f.getName().contains("60000")
            || f.getName().contains("70000")
            || f.getName().contains("80000")
            || f.getName().contains("90000")
            || f.getName().contains("100000")){
                continue;
            }
            testDirectory(f.getPath(), mode);
        }
    }
}
