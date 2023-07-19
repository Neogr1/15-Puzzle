import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends Tools {
    final static int REPEATS = 10000;

    static List<Integer> shuffle = new ArrayList<>();
    static int[] scram = new int[N*N];

    public static void main(String[] args) {
        for (int i = 0; i < N*N; i++) {
            shuffle.add(i);
        }

        for (int repeat = 0; repeat < REPEATS; repeat++) {
            System.out.println("\nRepeat: " + repeat);
            
            // generate scramble
            boolean solvable = false;
            while (!solvable) {
                Collections.shuffle(shuffle);
                for (int i = 0; i < N*N; i++) scram[i] = shuffle.get(i);
                solvable = isSolvable(scram);
            }

            for (int i = 0; i < N*N; i++) {
                System.out.print(scram[i]);
                System.out.print(" ");
            }
            System.out.println();

            // solve
            Solver solver = new Solver(scram);
            
            // write the solution
            try (FileWriter writer = new FileWriter("solutions/solutions.csv", true)) {
                // write optimal distance
                writer.append(Integer.toString(solver.optimalDistance));
                writer.append(",");

                // write scramble
                for (int i = 0; i < N*N; i++) {
                    writer.append(Integer.toString(shuffle.get(i)));
                    writer.append(",");
                }

                // write route
                for (int i = 1; i <= solver.optimalDistance; i++) {
                    writer.append(Integer.toString(solver.route[i]));
                    writer.append(",");
                }

                writer.append("\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
