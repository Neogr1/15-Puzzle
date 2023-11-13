public class Solver_PDB extends Tools {
    PDB663 dis;

    int[] scram = new int[N*N];
    int emptyPosition;
    int moves = 0;
    int[] route = new int[MAX_MOVES];

    int solutionCount = 0;
    int maxDistance;
    
    Boolean solved;
    int optimalDistance;

    Solver_PDB(int[] scram, PDB663 dis) {
        this.dis = dis;
        this.dis.initPDB_ID(scram);

        this.scram = scram;
        emptyPosition = getEmptyIndex(scram);
        solved = false;
    }

    public void solve() {
        // already solve
        if (dis.getPDB_Distance() == 0) {
            System.out.println("Already solved");
            return;
        }

        route[0] = -1;
        
        for (maxDistance = dis.getPDB_Distance(); maxDistance < MAX_MOVES; maxDistance += 2) {
            System.out.print("\rFinding distance " + maxDistance + "...");

            if (emptyPosition % N != 0)   L();
            if (emptyPosition % N != N-1) R();
            if (emptyPosition / N != 0)   U();
            if (emptyPosition / N != N-1) D();

            if (solved) {
                optimalDistance = maxDistance;
                break;
            }
        }

        System.out.println("\rOptimal distance is " + maxDistance);
    }

    public void L() {
        if (solved) return;

        moves++;
        dis.updatePDB_ID(scram[emptyPosition-1], 1);

        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition-1], -1);
            return;
        }

       route[moves] = 0;

       emptyPosition--;
       swap(scram, emptyPosition, emptyPosition+1);
       if (emptyPosition % N != 0)   L();
       if (emptyPosition / N != 0)   U();
       if (emptyPosition / N != N-1) D();
       swap(scram, emptyPosition, emptyPosition+1);
       emptyPosition++;

       moves--;
       dis.updatePDB_ID(scram[emptyPosition-1], -1);
   }

    public void R() {
        if (solved) return;

        moves++;
        dis.updatePDB_ID(scram[emptyPosition+1], -1);
        
        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition+1], 1);
            return;
        }

        route[moves] = 1;

        if (dis.getPDB_Distance() == 0) {
            solved = true;
            return;
        }

        emptyPosition++;
        swap(scram, emptyPosition, emptyPosition-1);
        if (emptyPosition % N != N-1) R();
        if (emptyPosition / N != 0)   U();
        if (emptyPosition / N != N-1) D();
        swap(scram, emptyPosition, emptyPosition-1);
        emptyPosition--;

        moves--;
        dis.updatePDB_ID(scram[emptyPosition+1], 1);
   }
   
    public void U() {
        if (solved) return;

        moves++;
        dis.updatePDB_ID(scram[emptyPosition-N], N);
        
        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition-N], -N);
            return;
        }

        route[moves] = 2;

        emptyPosition -= N;
        swap(scram, emptyPosition, emptyPosition+N);
        if (emptyPosition % N != 0)   L();
        if (emptyPosition % N != N-1) R();
        if (emptyPosition / N != 0)   U();
        swap(scram, emptyPosition, emptyPosition+N);
        emptyPosition += N;

        moves--;
        dis.updatePDB_ID(scram[emptyPosition-N], -N);
   }

    public void D() {
        if (solved) return;

        moves++;
        dis.updatePDB_ID(scram[emptyPosition+N], -N);
        
        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition+N], N);
            return;
        }

        route[moves] = 3;

        if (dis.getPDB_Distance() == 0) {
            solved = true;
            return;
        }

        emptyPosition += N;
        swap(scram, emptyPosition, emptyPosition-N);
        if (emptyPosition % N != 0)   L();
        if (emptyPosition % N != N-1) R();
        if (emptyPosition / N != N-1) D();
        swap(scram, emptyPosition, emptyPosition-N);
        emptyPosition -= N;

        moves--;
        dis.updatePDB_ID(scram[emptyPosition+N], N);
    }
}
