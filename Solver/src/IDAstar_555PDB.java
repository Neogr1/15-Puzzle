/*
 * iterative deepening A* search
 * using pattern database as a heuristic
 */


public class IDAstar_555PDB extends Tools {
    PDB555 dis = new PDB555();

    int[] scram = new int[N*N];
    int emptyPosition;
    int moves = 0;
    char[] route = new char[MAX_MOVES];

    int solutionCount = 0;
    int maxDistance;

    IDAstar_555PDB(int[] scram) {
        this.scram = scram;
        dis.initPDB_ID(scram);
        emptyPosition = getEmptyIndex(scram);
        solve();
    }

    public void solve() {
        // already solved
        if (dis.getPDB_Distance() == 0) {
            System.out.println("Already solved");
            return;
        }

        route[0] = '\0';
        
        for (maxDistance = dis.getPDB_Distance(); maxDistance < MAX_MOVES; maxDistance += 2) {
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
        dis.updatePDB_ID(scram[emptyPosition-1], 1);
        
        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition-1], -1);
            return;
        }
        
        route[moves] = 'L';
        
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
        moves++;
        dis.updatePDB_ID(scram[emptyPosition+1], -1);
        
        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition+1], 1);
            return;
        }
        
        route[moves] = 'R';
        
        if (dis.getPDB_Distance() == 0) {
            solutionCount++;       
            String routeString = String.join(" ", String.valueOf(route));
            System.out.println(solutionCount + ": " + routeString);
            
            moves--;
            dis.updatePDB_ID(scram[emptyPosition+1], 1);
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
        moves++;
        dis.updatePDB_ID(scram[emptyPosition-N], N);
        
        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition-N], -N);
            return;
        }
        
        route[moves] = 'U';
        
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
        moves++;
        dis.updatePDB_ID(scram[emptyPosition+N], -N);
        
        if (moves + dis.getPDB_Distance() > maxDistance) {
            moves--;
            dis.updatePDB_ID(scram[emptyPosition+N], N);
            return;
        }
        
        route[moves] = 'D';
        
        if (dis.getPDB_Distance() == 0) {
            solutionCount++;
            String routeString = String.join(" ", String.valueOf(route));
            System.out.println(solutionCount + ": " + routeString);

            moves--;
            dis.updatePDB_ID(scram[emptyPosition+N], N);
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