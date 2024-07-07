import java.io.*;
import java.util.*;

public class PDB663 {
    static final int[] BASE663 = {0, 0, 0, 0,
                                  0, 0, 2, 2,
                                  1, 1, 1, 2,
                                  1, 1, 1, 3};
    static final int[] POWER663 = {5, 4, 3, 2, 1, 0, 2, 1, 5, 4, 3, 0, 2, 1, 0, 0};

    int[][][] data663 = new int[3][][];      // file data
    int[][][] id663 = new int[3][][];        // id - dis
    int[][] pdbDis663 = new int[3][16777216]; // dis (index is id)

    public PDB663() {
        try {
            data663[0] = loadData("./db/663/0_1_2_3_4_5.csv");
            data663[1] = loadData("./db/663/8_9_10_12_13_14.csv");
            data663[2] = loadData("./db/663/6_7_11.csv");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        id663[0] = getIdAndDis(data663[0], 6);
        id663[1] = getIdAndDis(data663[1], 6);
        id663[2] = getIdAndDis(data663[2], 3);

        for (int i = 0; i < id663[0].length; i++) pdbDis663[0][id663[0][i][0]] = id663[0][i][1];
        for (int i = 0; i < id663[1].length; i++) pdbDis663[1][id663[1][i][0]] = id663[1][i][1];
        for (int i = 0; i < id663[2].length; i++) pdbDis663[2][id663[2][i][0]] = id663[2][i][1];
    }
    
    private int[][] loadData(String fileName) throws IOException {
        List<int[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine(); // skip first line

        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            int[] row = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                row[i] = Integer.parseInt(tokens[i]);
            }
            data.add(row);
        }
        br.close();

        return data.toArray(new int[0][]);
    }

    private int[][] getIdAndDis(int[][] data, int size) {
        int[][] result = new int[data.length][2];

        for (int i = 0; i < data.length; i++) {
            int id = 0;
            for (int j = 0; j < size; j++) {
                id = (id << 4) + data[i][j];
            }
            result[i][0] = id;
            result[i][1] = data[i][size];
        }
        return result;
    }


    
    int[] id = new int[4]; // 3 is enough. Here 4 is to handle error

    public void initPDB_ID(int[] scram) {
        int tile;
        for (int i = 0; i < 16; i++) {
            tile = scram[i];
            id[BASE663[tile]] += (i << (4 * POWER663[tile]));
        }
    }

    public void updatePDB_ID(int tile, int dir) {
        id[BASE663[tile]] += (dir << (4 * POWER663[tile]));
    }

    public int getPDB_Distance() {
        return pdbDis663[0][id[0]] + pdbDis663[1][id[1]] + pdbDis663[2][id[2]];
    }
}
