/*
 * A* algorithm using Manhattan distance as a heuristic
 */

import java.util.PriorityQueue;


public class Astar extends Tools {
    PriorityQueue<ScramInfo> priorityQueue;

    Astar(int[] scram) {
        
        // get index of empty tile
        int emptyPosition = getEmptyIndex(scram);

        // md 계산
        int md = calcMD(scram, N);

        // init route
        char[] route = new char[MAX_MOVES];
        route[0] = '\0';
        
        // init priority queue
        ScramInfo scramInfo = new ScramInfo(scram, emptyPosition, md, 0, route);
        priorityQueue = new PriorityQueue<>((s1, s2) -> Integer.compare(s1.md + s1.moves, s2.md + s2.moves));
        priorityQueue.offer(scramInfo);


        System.out.println("----- A* Solver Start -----");
        solve();
        System.out.println("------ A* Solver End ------");
    }

    public void solve() {
        int solutionCount = 0;
        int optimalMoves = MAX_MOVES;

        int emptyPosition, md, moves;
        int[] scram = new int[N*N];
        char[] route = new char[MAX_MOVES];
        int diff_md;

        while (!priorityQueue.isEmpty()) {
            ScramInfo scramInfo = priorityQueue.poll();
            emptyPosition = scramInfo.emptyPosition;
            md = scramInfo.md;
            moves = scramInfo.moves;
            scram = scramInfo.scram;
            route = scramInfo.route;
            
            if (md == 0) {
                if (solutionCount++ == 0) {
                    System.out.println("optimal move count: " + moves);
                    optimalMoves = moves;
                }
                
                String routeString = String.join(" ", String.valueOf(route));
                System.out.println(solutionCount + ": " + routeString);

                continue;
            }

            if (solutionCount > 0 && md + moves > optimalMoves) break;
            
            // left move
            if (emptyPosition % 4 != 0) {
                diff_md = (emptyPosition-1) % 4 < scram[emptyPosition-1] % 4 ? -1 : 1;
                route[moves+1] = 'L';
                swap(scram, emptyPosition, emptyPosition-1);
                priorityQueue.offer(new ScramInfo(scram, emptyPosition-1, md + diff_md, moves+1, route));
                swap(scram, emptyPosition, emptyPosition-1);
            }

            // right move
            if (emptyPosition % 4 != N-1) {
                diff_md = (emptyPosition+1) % 4 > scram[emptyPosition+1] % 4 ? -1 : 1;
                route[moves+1] = 'R';
                swap(scram, emptyPosition, emptyPosition+1);
                priorityQueue.offer(new ScramInfo(scram, emptyPosition+1, md + diff_md, moves+1, route));
                swap(scram, emptyPosition, emptyPosition+1);
            }

            // up move
            if (emptyPosition / 4 != 0) {
                diff_md = (emptyPosition-N) / 4 < scram[emptyPosition-N] / 4 ? -1 : 1;
                route[moves+1] = 'U';
                swap(scram, emptyPosition, emptyPosition-N);
                priorityQueue.offer(new ScramInfo(scram, emptyPosition-N, md + diff_md, moves+1, route));
                swap(scram, emptyPosition, emptyPosition-N);
            }

            // down move
            if (emptyPosition / 4 != N-1) {
                diff_md = (emptyPosition+N) / 4 > scram[emptyPosition+N] / 4 ? -1 : 1;
                route[moves+1] = 'D';
                swap(scram, emptyPosition, emptyPosition+N);
                priorityQueue.offer(new ScramInfo(scram, emptyPosition+N, md + diff_md, moves+1, route));
                swap(scram, emptyPosition, emptyPosition+N);
            }

        }

        if (solutionCount == 0) {
            System.out.println("No Solution");
        }
    }


    
    class ScramInfo {
        public int[] scram = new int[N*N];
        public int emptyPosition;
        public int md;
        public int moves;
        public char[] route = new char[MAX_MOVES];

        ScramInfo(int[] scram, int emptyPosition, int md, int moves, char[] route) {
            this.scram = scram.clone();
            this.emptyPosition = emptyPosition;
            this.md = md;
            this.moves = moves;
            for (int i = 0; i <= moves; i++) this.route[i] = route[i]; // 이동수가 작은 경우가 많으므로 이동수까지만 카피
        }
    }
}

