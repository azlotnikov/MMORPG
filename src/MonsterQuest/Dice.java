package MonsterQuest;

/**
 * Created by Alexander on 3/26/14.
 */

import java.util.Random;

public class Dice {

    private static Random rand = new Random();

    public static int getInt(int N, int count_shoot){
        int result = 0;
        while(count_shoot > 0){
            result += rand.nextInt(N);
            count_shoot--;
        }
        return result;
    }

    public static boolean getBool(int count_shoot){
        boolean result = true;
        while(count_shoot > 0){
            result &= rand.nextBoolean();
            count_shoot--;
        }
        return result;
    }

    public static Direction getDirection(){
        Direction result[] = {Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.NONE};
        return result[rand.nextInt(4)];
    }
}
