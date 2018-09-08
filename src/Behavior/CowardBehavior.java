package Behavior;
import java.util.ArrayList;

public class CowardBehavior implements AIBehavior {
    @Override
    public int[] decideMove(int[] map, int[] currLocation, int[] playerLocation, ArrayList<Integer> pastMoves) {
        int[] direction = new int[2];
        direction[0] = playerLocation[0] - currLocation[0];
        direction[1] = playerLocation[1] - currLocation[1];
        //         N   E   S   W
        // x (sin) 0   +   0   -   (direction[0])
        // y (-cos)-   0   +   0   (direction[1])
        //         0   1   2   3
        //         0   90  180 270
        // find out where player is in relation to coward
        // java.lang.Math.asin(direction[0]*java.lang.Math.PI/2);
        ArrayList<int[]> targetSquares = new ArrayList<int[2]>();
        int[] output = new int[2];
        if (direction[0] >= 0) {
            // EAST
            output[0] = currLocation[0]+1;
            outout[1] = currLocation[1];
            //check if output coord cell type (if a wall or undefined(-1), don't add)
            targetSquares.add(output);
        } else if (direction[1] < 0) {
            // WEST
            output[0] = currLocation[0]-1;
            outout[1] = currLocation[1];
            //check if output coord cell type (if a wall or undefined(-1), don't add)
            targetSquares.add(output);
        }
        // -java.lang.Math.acos(direction[1]*java.lang.Math.PI/2);
        if (direction[1] >= 0) {
            // SOUTH
            output[0] = currLocation[0];
            outout[1] = currLocation[1]+1;
            //check if output coord cell type (if a wall or undefined(-1), don't add)
            targetSquares.add(output);
        } else if (direction[1] < 0) {
            // NORTH
            output[0] = currLocation[0];
            outout[1] = currLocation[1]-1;
            //check if output coord cell type (if a wall or undefined(-1), don't add)
            targetSquares.add(output);
        }
        return targetSquares;

//        return new int[0];
    }
}
