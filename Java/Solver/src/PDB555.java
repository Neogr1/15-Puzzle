import java.io.*;
import java.util.*;

public class PDB555 {
    static final int[] BASE555 = {0, 0, 0, 1,
                                 0, 0, 1, 1,
                                 2, 2, 1, 1,
                                 2, 2, 2, 3};
    static final int[] POWER555 = {4, 3, 2, 4, 1, 0, 3, 2, 4, 3, 1, 0, 2, 1, 0, 0};

    int[][][] data555 = new int[3][][];      // file data
    int[][][] id555 = new int[3][][];        // id - dis
    int[][] pdbDis555 = new int[3][1048576]; // dis (index is id)

    public PDB555() {
        try {
            data555[0] = loadData("./Java/Solver/db/555/0_1_2_4_5.csv");
            data555[1] = loadData("./Java/Solver/db/555/3_6_7_10_11.csv");
            data555[2] = loadData("./Java/Solver/db/555/8_9_12_13_14.csv");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        id555[0] = getIdAndDis(data555[0], 5);
        id555[1] = getIdAndDis(data555[1], 5);
        id555[2] = getIdAndDis(data555[2], 5);

        for (int i = 0; i < id555[0].length; i++) pdbDis555[0][id555[0][i][0]] = id555[0][i][1];
        for (int i = 0; i < id555[0].length; i++) pdbDis555[1][id555[1][i][0]] = id555[1][i][1];
        for (int i = 0; i < id555[0].length; i++) pdbDis555[2][id555[2][i][0]] = id555[2][i][1];
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
            id[BASE555[tile]] += (i << (4 * POWER555[tile]));
        }
    }

    public void updatePDB_ID(int tile, int dir) {
        id[BASE555[tile]] += (dir << (4 * POWER555[tile]));
    }


    public int getPDB_Distance() {
        // System.out.println(pdbDis555[0][id[0]] + pdbDis555[1][id[1]] + pdbDis555[2][id[2]]);
        return pdbDis555[0][id[0]] + pdbDis555[1][id[1]] + pdbDis555[2][id[2]];
    }
}
