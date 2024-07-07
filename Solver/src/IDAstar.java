/*
 * iterative deepening A* search
 * using Manhattan distance as a heuristic
 */

public class IDAstar extends Tools {
    int[] scram = new int[N*N];
    int emptyPosition;
    int md;
    int moves = 0;
    char[] route = new char[MAX_MOVES];

    int solutionCount = 0;
    int maxDistance;

    IDAstar(int[] scram) {
        this.scram = scram;
        emptyPosition = getEmptyIndex(scram);
        md = calcMD(scram, N);

        solve();
    }

    public void solve() {
        // already solved
        if (md == 0) {
            System.out.println("Already solved");
            return;
        }

        route[0] = '\0';
        
        for (maxDistance = md % 2; maxDistance < MAX_MOVES; maxDistance += 2) {
            System.out.println(maxDistance);

            if (emptyPosition % N != 0)   L();
            if (emptyPosition % N != N-1) R();
            if (emptyPosition / N != 0)   U();
            if (emptyPosition / N != N-1) D();

            if (solutionCount > 0) break;
        }

        if (solutionCount == 0) {
            System.out.println("No Solution");
        }
    }

    public void L() {
        moves++;
        emptyPosition--;
        int diff_md = emptyPosition % N < scram[emptyPosition] % N ? -1 : 1;
        md += diff_md;

        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition++;
            moves--;
            return;
        }

        route[moves] = 'L';

        swap(scram, emptyPosition, emptyPosition+1);
        if (emptyPosition % N != 0)   L();
        if (emptyPosition / N != 0)   U();
        if (emptyPosition / N != N-1) D();
        swap(scram, emptyPosition, emptyPosition+1);

        md -= diff_md;
        emptyPosition++;
        moves--;
    }

    public void R() {
        moves++;
        emptyPosition++;
        int diff_md = emptyPosition % N > scram[emptyPosition] % N ? -1 : 1;
        md += diff_md;

        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition--;
            moves--;
            return;
        }

        route[moves] = 'R';

        if (md == 0) {
            solutionCount++;       
            String routeString = String.join(" ", String.valueOf(route));
            System.out.println(solutionCount + ": " + routeString);
            md -= diff_md;
            emptyPosition--;
            moves--;
            return;
        }

        swap(scram, emptyPosition, emptyPosition-1);
        if (emptyPosition % N != N-1) R();
        if (emptyPosition / N != 0)   U();
        if (emptyPosition / N != N-1) D();
        swap(scram, emptyPosition, emptyPosition-1);

        md -= diff_md;
        emptyPosition--;
        moves--;
    }
    
    public void U() {
        moves++;
        emptyPosition -= N;
        int diff_md = emptyPosition / N < scram[emptyPosition] / N ? -1 : 1;
        md += diff_md;

        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition += N;
            moves--;
            return;
        }

        route[moves] = 'U';

        swap(scram, emptyPosition, emptyPosition+N);
        if (emptyPosition % N != 0)   L();
        if (emptyPosition % N != N-1) R();
        if (emptyPosition / N != 0)   U();
        swap(scram, emptyPosition, emptyPosition+N);

        md -= diff_md;
        emptyPosition += N;
        moves--;
    }

    public void D() {
        moves++;
        emptyPosition += N;
        int diff_md = emptyPosition / N > scram[emptyPosition] / N ? -1 : 1;
        md += diff_md;
        
        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition -= N;
            moves--;
            return;
        }
        
        route[moves] = 'D';

        if (md == 0) {
            solutionCount++;
            String routeString = String.join(" ", String.valueOf(route));
            System.out.println(solutionCount + ": " + routeString);
            md -= diff_md;
            emptyPosition -= N;
            moves--;
            return;
        }

        swap(scram, emptyPosition, emptyPosition-N);
        if (emptyPosition % N != 0)   L();
        if (emptyPosition % N != N-1) R();
        if (emptyPosition / N != N-1) D();
        swap(scram, emptyPosition, emptyPosition-N);

        md -= diff_md;
        emptyPosition -= N;
        moves--;
    }
}

