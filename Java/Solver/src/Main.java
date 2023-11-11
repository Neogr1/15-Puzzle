import java.util.Scanner;

public class Main extends Tools {
    public static Scanner sc = new Scanner(System.in);
    private static int[] scram = new int[N*N];

    public static void main(String[] args) {
        System.out.println("Enter scramble:");
        String[] input = sc.nextLine().split(" ");
        System.out.println("Choose solver:");
        System.out.println("(1) A* (2) IDA* (3) IDA* with PDB");
        int solverChoice = sc.nextInt();
        sc.close();

        // input 처리
        boolean isExist0 = false;
        for (int i = 0; i < N*N; i++) {
            scram[i] = Integer.parseInt(input[i]);
            if (scram[i] == 0) isExist0 = true;
        }
        if (!isExist0) {
            for (int i = 0; i < N*N; i++) {
                scram[i]--;
            }
        }

        
        switch (solverChoice) {
        case 1:
            solveAstar();
            break;
        case 2:
            solveIDAstar();
            break;
        case 3:
            solveIDAstar_PDB();
        }

   
    }

    private static void solveAstar() {
        System.out.println("------ A* Solver Start ------");
        long startTime = System.nanoTime();
        new Astar(scram);
        System.out.println("Time: " + (System.nanoTime()-startTime)/1000000 + "ms");
        System.out.println("------ A* Solver Finish ------");
    }

    private static void solveIDAstar() {
        System.out.println("----- IDA* Solver Start -----");
        long startTime = System.nanoTime();
        new IDAstar(scram);
        System.out.println("Time: " + (System.nanoTime()-startTime)/1000000 + "ms");
        System.out.println("----- IDA* Solver Finish -----");
    }

    private static void solveIDAstar_PDB() {
        System.out.println("----- IDA* with PDB Solver Start -----");
        long startTime = System.nanoTime();
        new IDAstar_PDB(scram);
        System.out.println("Time: " + (System.nanoTime()-startTime)/1000000 + "ms");
        System.out.println("----- IDA* with PDB Solver Finish -----");
    }

}