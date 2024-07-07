
public class Tools {
    public static final int N = 4;
    public static final int MAX_MOVES = 81;

    public static int getEmptyIndex(int[] scram) {
        for (int i = 0; i < N*N; i++) {
            if (scram[i] == N*N-1) {
                return i;
            }
        }

        return -1; // error
    }

    public static void swap(int[] scram, int a, int b) {
        int temp = scram[a];
        scram[a] = scram[b];
        scram[b] = temp;
    }

    public static int calcMD(int[] scram, int N) {
        int md = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (scram[i*N+j] == N*N-1) continue;

                md += Math.abs(scram[i*N+j] / N - i) + Math.abs(scram[i*N+j] % N - j);
            }
        }

        return md;
    }

    public static int[] transposeScramble(int[] scram) {
        int[] transposeTable = { 0, 4, 8,12,
                                 1, 5, 9,13,
                                 2, 6,10,14,
                                 3, 7,11,15};
        int[] transposed_scram = new int[N*N];

        for (int i = 0; i < N*N; i++) {
            transposed_scram[i] = transposeTable[scram[i]];
        }
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                swap(transposed_scram, i*4+j, transposeTable[i*4+j]);
            }
        }

        return transposed_scram;
    }

    public static int countHorizontalInversion(int[] scram) {
        int inversion = 0;

        for (int i = 0; i < N*N-1; i++) {
            if (scram[i] == N*N-1) continue;
            for (int j = i+1; j < N*N; j++) {
                if (scram[j] == N*N-1) continue;
                if (scram[i] > scram[j]) inversion++;
            }
        }

        return inversion;
    }

    public static int countVerticalInversion(int[] scram) {
        int[] transposed_scram = transposeScramble(scram);
        int inversion = 0;

        for (int i = 0; i < N*N-1; i++) {
            if (transposed_scram[i] == N*N-1) continue;
            for (int j = i+1; j < N*N; j++) {
                if (transposed_scram[j] == N*N-1) continue;
                if (transposed_scram[i] > transposed_scram[j]) inversion++;
            }
        }

        return inversion;
    }

    public static int calcID(int[] scram) {
        int horIC = countHorizontalInversion(scram);
        int verIC = countVerticalInversion(scram);

        return (horIC % 3 + horIC / 3) + (verIC % 3 + verIC / 3);
    }

    public static boolean isSolvable(int[] scram) {
        int emptyIdx = getEmptyIndex(scram);
        int horIC = countHorizontalInversion(scram);
        int verIC = countVerticalInversion(scram);

        return (horIC % 3 + horIC / 3) % 2 != emptyIdx / 4 % 2 && (verIC % 3 + verIC / 3) % 2 != emptyIdx % 4 % 2;
    }
}
