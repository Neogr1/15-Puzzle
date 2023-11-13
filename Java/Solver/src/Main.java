import java.util.Scanner;

public class Main extends Tools {
    public static Scanner sc = new Scanner(System.in);
    private static int[] scram = new int[N*N];

    public static void main(String[] args) {
        System.out.println("Enter scramble:");
        String[] input = sc.nextLine().split(" ");
        System.out.println("Choose solver:");
        System.out.print("(1) A*\n(2) IDA*\n(3) IDA* - 555 PDB\n(4) IDA* - 663 PDB\n>>> ");
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
            solveIDAstar_555PDB();
            break;
        case 4:
            solveIDAstar_663PDB();
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

    private static void solveIDAstar_555PDB() {
        System.out.println("----- IDA* with 555 PDB Solver Start -----");
        long startTime = System.nanoTime();
        new IDAstar_555PDB(scram);
        System.out.println("Time: " + (System.nanoTime()-startTime)/1000000 + "ms");
        System.out.println("----- IDA* with 555 PDB Solver Finish -----");
    }

    private static void solveIDAstar_663PDB() {
        System.out.println("----- IDA* with 663 PDB Solver Start -----");
        long startTime = System.nanoTime();
        new IDAstar_663PDB(scram);
        System.out.println("Time: " + (System.nanoTime()-startTime)/1000000 + "ms");
        System.out.println("----- IDA* with 663 PDB Solver Finish -----");
    }
}