public class Solver extends Tools {
    int[] scram = new int[N*N];
    int emptyPosition;
    int md;
    int moves;
    int[] route = new int[MAX_MOVES];
    
    int maxDistance;
    
    Boolean solved;
    int optimalDistance;

    Solver(int[] scram) {
        this.scram = scram;
        
        // get index of empty tile
        emptyPosition = getEmptyIndex(scram);

        // md 계산
        md = calcMD(scram, N);

        moves = 0;

        // init route
        route[0] = -1;

        solve();
    }

    public void solve() {
        solved = false;

        // already solve
        if (md == 0) {
            System.out.println("Already solved");
            return;
        }
        
        for (maxDistance = md % 2; maxDistance < MAX_MOVES; maxDistance += 2) {
            System.out.println(maxDistance);

            if (emptyPosition % 4 != 0)   L();
            if (emptyPosition % 4 != N-1) R();
            if (emptyPosition / 4 != 0)   U();
            if (emptyPosition / 4 != N-1) D();

            if (solved) {
                optimalDistance = maxDistance;
                break;
            }
        }
    }

    public void L() {
        if (solved) return;

        moves++;
        emptyPosition--;
        int diff_md = emptyPosition % 4 < scram[emptyPosition] % 4 ? -1 : 1;
        md += diff_md;

        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition++;
            moves--;
            return;
        }

       route[moves] = 0;

       swap(scram, emptyPosition, emptyPosition+1);
       if (emptyPosition % 4 != 0)   L();
       if (emptyPosition / 4 != 0)   U();
       if (emptyPosition / 4 != N-1) D();
       if (solved) return;
       swap(scram, emptyPosition, emptyPosition+1);

       md -= diff_md;
       emptyPosition++;
       moves--;
   }

    public void R() {
        if (solved) return;

        moves++;
        emptyPosition++;
        int diff_md = emptyPosition % 4 > scram[emptyPosition] % 4 ? -1 : 1;
        md += diff_md;

        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition--;
            moves--;
            return;
        }

        route[moves] = 1;

        if (md == 0) {
            solved = true;
            return;
        }

        swap(scram, emptyPosition, emptyPosition-1);
        if (emptyPosition % 4 != N-1) R();
        if (emptyPosition / 4 != 0)   U();
        if (emptyPosition / 4 != N-1) D();
        if (solved) return;
        swap(scram, emptyPosition, emptyPosition-1);

        md -= diff_md;
        emptyPosition--;
        moves--;
   }
   
    public void U() {
        if (solved) return;

        moves++;
        emptyPosition -= N;
        int diff_md = emptyPosition / 4 < scram[emptyPosition] / 4 ? -1 : 1;
        md += diff_md;

        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition += N;
            moves--;
            return;
        }

        route[moves] = 2;

        swap(scram, emptyPosition, emptyPosition+N);
        if (emptyPosition % 4 != 0)   L();
        if (emptyPosition % 4 != N-1) R();
        if (emptyPosition / 4 != 0)   U();
        if (solved) return;
        swap(scram, emptyPosition, emptyPosition+N);

        md -= diff_md;
        emptyPosition += N;
        moves--;
   }

    public void D() {
        moves++;
        emptyPosition += N;
        int diff_md = emptyPosition / 4 > scram[emptyPosition] / 4 ? -1 : 1;
        md += diff_md;

        if (moves + md > maxDistance) {
            md -= diff_md;
            emptyPosition -= N;
            moves--;
            return;
        }

        route[moves] = 3;

        if (md == 0) {
            solved = true;
            return;
        }

        swap(scram, emptyPosition, emptyPosition-N);
        if (emptyPosition % 4 != 0)   L();
        if (emptyPosition % 4 != N-1) R();
        if (emptyPosition / 4 != N-1) D();
        if (solved) return;
        swap(scram, emptyPosition, emptyPosition-N);

        md -= diff_md;
        emptyPosition -= N;
        moves--;
    }
}
