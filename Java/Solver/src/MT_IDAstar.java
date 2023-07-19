/*
 * multithreading
 * iterative deepening A* search
 * using Manhattan distance as a heuristic
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MT_IDAstar extends Tools {
    final int MAX_THREADS = 4;

    int[] scram = new int[N*N];
    int solutionCount;


    MT_IDAstar(int[] scram) {
        this.scram = scram;

        solve();
    }


    public void solve() {
        solutionCount = 0;

        int emptyPosition = getEmptyIndex(scram);
        int md = calcMD(scram, N);
        int moves = 0;
        char[] route = new char[MAX_MOVES];
        route[0] = '\0';

        // already solved
        if (md == 0) {
            System.out.println("Already solved");
            return;
        }
        
        for (int maxDistance = md % 2; maxDistance < MAX_MOVES; maxDistance += 2) {
            System.out.println(maxDistance);
            
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            executor.execute(new SolveThread(executor, scram, emptyPosition, md, moves, route, maxDistance));

            // 어떻게 기다려야 하는가?????????

            if (solutionCount > 0) break;
        }

        if (solutionCount == 0) {
            System.out.println("No Solution");
        }
    }
    
    public class SolveThread implements Runnable {
        ThreadPoolExecutor executor;

        int[] scram = new int[N*N];
        int emptyPosition;
        int md;
        int moves;
        char[] route = new char[MAX_MOVES];

        int maxDistance;

        SolveThread(ThreadPoolExecutor executor, int[] scram, int emptyPosition, int md, int moves, char[] route, int maxDistance) {
            this.executor = executor;

            this.scram = scram.clone();
            this.emptyPosition = emptyPosition;
            this.md = md;
            this.moves = moves;
            for (int i = 0; i <= moves; i++) this.route[i] = route[i];

            this.maxDistance = maxDistance;
        }


        @Override
        public void run() {
            if (emptyPosition % 4 != 0)   L();
            if (emptyPosition % 4 != N-1) R();
            if (emptyPosition / 4 != 0)   U();
            if (emptyPosition / 4 != N-1) D();
        }

        public void L() {
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

            route[moves] = 'L';

            swap(scram, emptyPosition, emptyPosition+1);
            if (executor.getActiveCount() < MAX_THREADS) {
                executor.execute(new SolveThread(executor, scram, emptyPosition, md, moves, route, maxDistance));
                swap(scram, emptyPosition, emptyPosition+1);
                md -= diff_md;
                emptyPosition++;
                moves--;
                return;
            }
            
            if (emptyPosition % 4 != 0)   L();
            if (emptyPosition / 4 != 0)   U();
            if (emptyPosition / 4 != N-1) D();

            swap(scram, emptyPosition, emptyPosition+1);

            md -= diff_md;
            emptyPosition++;
            moves--;
        }

        public void R() {
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

            if (executor.getActiveCount() < MAX_THREADS) {
                executor.execute(new SolveThread(executor, scram, emptyPosition, md, moves, route, maxDistance));
                swap(scram, emptyPosition, emptyPosition-1);
                md -= diff_md;
                emptyPosition--;
                moves--;
                return;
            }

            if (emptyPosition % 4 != N-1) R();
            if (emptyPosition / 4 != 0)   U();
            if (emptyPosition / 4 != N-1) D();

            swap(scram, emptyPosition, emptyPosition-1);

            md -= diff_md;
            emptyPosition--;
            moves--;
        }
        
        public void U() {
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

            route[moves] = 'U';

            swap(scram, emptyPosition, emptyPosition+N);

            if (executor.getActiveCount() < MAX_THREADS) {
                executor.execute(new SolveThread(executor, scram, emptyPosition, md, moves, route, maxDistance));
                swap(scram, emptyPosition, emptyPosition+N);
                md -= diff_md;
                emptyPosition += N;
                moves--;
                return;
            }

            if (emptyPosition % 4 != 0)   L();
            if (emptyPosition % 4 != N-1) R();
            if (emptyPosition / 4 != 0)   U();

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

            if (executor.getActiveCount() < MAX_THREADS) {
                executor.execute(new SolveThread(executor, scram, emptyPosition, md, moves, route, maxDistance));
                swap(scram, emptyPosition, emptyPosition-N);
                md -= diff_md;
                emptyPosition -= N;
                moves--;
                return;
            }
            
            if (emptyPosition % 4 != 0)   L();
            if (emptyPosition % 4 != N-1) R();
            if (emptyPosition / 4 != N-1) D();

            swap(scram, emptyPosition, emptyPosition-N);

            md -= diff_md;
            emptyPosition -= N;
            moves--;
        }
    }

}

