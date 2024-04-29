package hackerrank;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

public class QueenAttackSolution {

    // Complete the queensAttack function below.
    static int queensAttack(int n, int k, int r_q, int c_q, int[][] obstacles) {
        HashMap<Long,Boolean> obs = new HashMap<Long,Boolean>(obstacles.length);
        for (int i =0;i<obstacles.length;i++) {
            obs.put(getFlattenedIndex(obstacles[i][0]-1, obstacles[i][1]-1, n),true);
        }
        int ret = 0;
        // normalize queen position
        r_q -= 1;
        c_q -= 1;
        // left
        ret += countWays(r_q, c_q, 0, -1, n,obs);
        // up
        ret += countWays(r_q, c_q, -1, 0, n,obs);
        // down
        ret += countWays(r_q, c_q, 1, 0, n,obs);
        // right
        ret += countWays(r_q, c_q, 0, 1, n,obs);
        // up-left
        ret += countWays(r_q, c_q, -1, -1, n,obs);
        // up-right
        ret += countWays(r_q, c_q, -1, 1, n,obs);
        // down-left
        ret += countWays(r_q, c_q, 1, -1, n,obs);
        // down-right
        ret += countWays(r_q, c_q, 1, 1, n,obs);

        return ret;
    }

    private static int countWays(int row, int col, int rowInc, int colInc, int max, HashMap<Long, Boolean> obs) {
        int ret = 0;
         System.out.println("moving"+rowInc+","+colInc);
        while (true) {
            row += rowInc;
            col += colInc;
            // we hit an obstacle
            Boolean obstacle = obs.get(getFlattenedIndex(row,col,max));
            if (obstacle != null && obstacle.booleanValue()) {
                 System.out.println("obstacle at "+row+","+col);
                break;
            }
            // we broke out of the grid
            if (row < 0 || col < 0 || row >= max || col >= max) {
                 System.out.println("OOB at "+row+","+col+":"+max);
                break;
            }
            ret++;
        }
        // System.out.println("found "+ ret+ "moves");
        return ret;
    }

    private static long getFlattenedIndex(int row, int col, int rowlength) {
        return (long)row * rowlength + col;
    }

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        scanner = new Scanner(new File("queenattack_test_data.txt"));
        String[] nk = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nk[0]);

        int k = Integer.parseInt(nk[1]);

        String[] r_qC_q = scanner.nextLine().split(" ");

        int r_q = Integer.parseInt(r_qC_q[0]);

        int c_q = Integer.parseInt(r_qC_q[1]);

        int[][] obstacles = new int[k][2];

        for (int i = 0; i < k; i++) {
            String[] obstaclesRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int j = 0; j < 2; j++) {
                int obstaclesItem = Integer.parseInt(obstaclesRowItems[j]);
                obstacles[i][j] = obstaclesItem;
            }
        }
        int result = queensAttack(n, k, r_q, c_q, obstacles);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedWriter.flush();

        bufferedWriter.close();

        scanner.close();
    }
}
