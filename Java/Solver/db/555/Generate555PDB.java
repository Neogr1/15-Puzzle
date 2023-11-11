import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;



public class Generate555PDB {
    public static final int OFFSET = 1; // record에서 0을 불가능한 scramble로 표시하기 위한 오프셋

    // test scramble: 12 16 10 11 13 8 1 2 3 14 9 15 4 7 6 5
    public static final int[][] BASE = {
        // { 0, 1, 2, 4, 5},
        // { 3, 6, 7,10,11},
        // { 8, 9,12,13,14} // 17.2 sec

        // { 0, 1, 4, 8,12},
        // { 2, 3, 6, 7,11},
        // { 5, 9,10,13,14} // 34.1 sec

        { 0, 1, 4, 5,10},
        { 2, 3, 6, 7,11},
        { 8, 9,12,13,14}
    };

    Queue<Scramble> scramQueue = new LinkedList<>();

    int size;
    int[] targetPosition = new int[8]; // 8은 그냥 적당히 큰 수.
    int moves;

    int[] distanceRecord;


    public Generate555PDB() {
        generate(0, 5);
        writeCSV(0);
        System.out.println("Set 1 done.");

        generate(1, 5);
        writeCSV(1);
        System.out.println("Set 2 done.");

        generate(2, 5);
        writeCSV(2);
        System.out.println("Set 3 done.");
    }



    public void generate(int baseNumber, int size_) {
        distanceRecord = new int[1048576]; // 16^5

        // make solved state
        size = size_;
        targetPosition = Arrays.copyOf(BASE[baseNumber], size);
        moves = OFFSET;
        Scramble scramble = new Scramble(size, targetPosition, moves);
        
        // save the distance 0
        int scrambleId = getIdFromTargetPosition();
        distanceRecord[scrambleId] = moves;
        scramQueue.add(scramble);

        // bfs
        while ((scramble = scramQueue.poll()) != null) {
            size = scramble.size;
            targetPosition = scramble.targetPosition;
            moves = scramble.moves;

            addNeighborScramble();
        }
    }

    // queue에 이웃 scramble을 추가
    public void addNeighborScramble() {
        int idx, dir, scrambleId;
        ArrayList<Pair> neighbors = getNeighborScrambles();

        for (Pair pair: neighbors) {
            idx = pair.a;
            dir = pair.b;

            targetPosition[idx] += dir;

            scrambleId = getIdFromTargetPosition();
            if (distanceRecord[scrambleId] == 0) {
                distanceRecord[scrambleId] = moves+1;
                scramQueue.add(new Scramble(size, targetPosition, moves+1));
            }
            
            targetPosition[idx] -= dir;
        }
    }

    // target을 한칸 이동시켜 새로운 scramble을 얻는 target,direction 조합을 리턴
    public ArrayList<Pair> getNeighborScrambles() {
        int[] board = new int[16];
        int target;
        ArrayList<Pair> neighbors = new ArrayList<>();
        
        for (int i = 0; i < size; i++) board[targetPosition[i]] = -1;

        for (int i = 0; i < size; i++) {
            target = targetPosition[i];

            if (target % 4 != 3 && board[target+1] != -1)
                neighbors.add(new Pair(i, 1));
            if (target / 4 != 3 && board[target+4] != -1)
                neighbors.add(new Pair(i, 4));
            if (target % 4 != 0 && board[target-1] != -1)
                neighbors.add(new Pair(i, -1));
            if (target / 4 != 0 && board[target-4] != -1)
                neighbors.add(new Pair(i, -4));
        }

        return neighbors;
    }



    // target position의 id를 비트로 표현
    public int getIdFromTargetPosition() {
        int id = 0;
        for (int i = 0; i < size; i++) {
            id = (id << 4) + targetPosition[i];
        }

        return id;
    }

    public int[] getTargetPositionFromId(int id) {
        int[] targetPosition = new int[size];
        for (int i = size-1; i >= 0; i--) {
            targetPosition[i] = id % 16;
            id = (id >> 4);
        }

        return targetPosition;
    }



    public void writeCSV(int baseNumber) {
        String filePath = "./db/555/";
        String fileName = "" + BASE[baseNumber][0];
        for (int i = 1; i < size; i++) fileName += "_" + BASE[baseNumber][i];

        File file = new File(filePath + fileName + ".csv");
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file));
            
            // write column name
            String columnNames = "" + BASE[baseNumber][0];
            for (int i = 1; i < size; i++) columnNames += "," + BASE[baseNumber][i];
            columnNames += ",distance";

            bw.write(columnNames);
            bw.newLine();


            for (int id = 0; id < Math.pow(16,size); id++) {
                if (distanceRecord[id] == 0) continue;

                targetPosition = getTargetPositionFromId(id);
                String line = "" + targetPosition[0];
                for (int i = 1; i < size; i++) line += "," + targetPosition[i];
                line += "," + (distanceRecord[id] - OFFSET);

                bw.write(line);
                bw.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



class Scramble {
    public int size;
    public int[] targetPosition;
    int emptyPosition;
    public int moves;

    Scramble(int size, int[] targetPosition, int moves) {
        this.size = size;
        this.targetPosition = Arrays.copyOf(targetPosition, size);
        this.moves = moves;
    }
}

class Pair {
    public int a;
    public int b;

    Pair(int a, int b) {
        this.a = a;
        this.b = b;
    } 
}
