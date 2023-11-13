import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;



public class Generate555PDB {
    public static final int MOVE_OFFSET = 1; // record에서 0을 불가능한 scramble로 표시하기 위한 오프셋

    public static final int[][] TARGET_SET = {
        { 0, 1, 2, 4, 5},
        { 3, 6, 7,10,11},
        { 8, 9,12,13,14}
    };

    Queue<Scramble> scramQueue = new LinkedList<>();

    int size;
    int[] targetPosition;
    int emptyPosition;
    int moves;

    int[] visit;
    int[] distanceRecord;


    public Generate555PDB() {
        generate(TARGET_SET[0], 5);
        writeCSV(TARGET_SET[0]);
        System.out.println("Set 1 done.");

        generate(TARGET_SET[1], 5);
        writeCSV(TARGET_SET[1]);
        System.out.println("Set 2 done.");

        generate(TARGET_SET[2], 5);
        writeCSV(TARGET_SET[2]);
        System.out.println("Set 3 done.");
    }



    public void generate(int[] target, int size_) {
        int max_size = (int) Math.pow(16, size_);

        visit = new int[max_size * 16]; // save min visit moves
        distanceRecord = new int[max_size]; // init 0
        
        // make solved state
        size = size_;
        targetPosition = Arrays.copyOf(target, size);
        emptyPosition = 15;
        moves = MOVE_OFFSET;
        Scramble scramble = new Scramble(size, targetPosition, emptyPosition, moves);
        
        // save the distance 0
        int scrambleId = getIdFromTargetPosition();
        distanceRecord[scrambleId] = moves;
        scramQueue.add(scramble);

        // bfs
        while ((scramble = scramQueue.poll()) != null) {
            size = scramble.size;
            targetPosition = scramble.targetPosition;
            emptyPosition = scramble.emptyPosition;
            moves = scramble.moves;

            addNeighborScramble();
        }
    }

    // scramQueue에 이웃 scramble을 추가
    public void addNeighborScramble() {
        int[] board = new int[16];
        int scrambleId, scramEmptyId;

        for (int i = 0; i < size; i++) board[targetPosition[i]] = i+100; // 100 is offset

        int[] dirIterator = {1, 4, -1, -4};

        for (int dir: dirIterator) {
            if (dir == 1 && emptyPosition % 4 == 3) continue;
            if (dir == 4 && emptyPosition / 4 == 3) continue;
            if (dir == -1 && emptyPosition % 4 == 0) continue;
            if (dir == -4 && emptyPosition / 4 == 0) continue;

            if (board[emptyPosition+dir] == 0) { // move non-target
                scrambleId = getIdFromTargetPosition();
                scramEmptyId = scrambleId * 16 + emptyPosition + dir;
                if (visit[scramEmptyId] == 0 || moves < visit[scramEmptyId]) {
                    visit[scramEmptyId] = moves;
                    distanceRecord[scrambleId] = (distanceRecord[scrambleId] == 0) ? moves : Math.min(distanceRecord[scrambleId], moves);
                    scramQueue.add(new Scramble(size, targetPosition, emptyPosition+dir, moves));
                }
            }
            else { // move target
                targetPosition[board[emptyPosition+dir]-100] -= dir;

                scrambleId = getIdFromTargetPosition();
                scramEmptyId = scrambleId * 16 + emptyPosition + dir;
                if (visit[scramEmptyId] == 0 || moves < visit[scramEmptyId]) {
                    visit[scramEmptyId] = moves;
                    distanceRecord[scrambleId] = (distanceRecord[scrambleId] == 0) ? moves+1 : Math.min(distanceRecord[scrambleId], moves+1);
                    scramQueue.add(new Scramble(size, targetPosition, emptyPosition+dir, moves+1));
                }

                targetPosition[board[emptyPosition+dir]-100] += dir;
            }
        
        }
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



    public void writeCSV(int[] target) {
        String filePath = "./db/555/";
        String fileName = "" + target[0];
        for (int i = 1; i < size; i++) fileName += "_" + target[i];

        File file = new File(filePath + fileName + ".csv");
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file));
            
            // write column name
            String columnNames = "" + target[0];
            for (int i = 1; i < size; i++) columnNames += "," + target[i];
            columnNames += ",distance";

            bw.write(columnNames);
            bw.newLine();


            for (int id = 0; id < Math.pow(16,size); id++) {
                if (distanceRecord[id] == 0) continue;

                targetPosition = getTargetPositionFromId(id);
                String line = "" + targetPosition[0];
                for (int i = 1; i < size; i++) line += "," + targetPosition[i];
                line += "," + (distanceRecord[id] - MOVE_OFFSET);

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

    Scramble(int size, int[] targetPosition, int emptyPosition, int moves) {
        this.size = size;
        this.targetPosition = Arrays.copyOf(targetPosition, size);
        this.emptyPosition = emptyPosition;
        this.moves = moves;
    }
}