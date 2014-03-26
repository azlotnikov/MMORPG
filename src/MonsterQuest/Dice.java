package MonsterQuest;

/**
 * Created by Alexander on 3/26/14.
 */

import java.util.Random;

public class Dice {

    private static Random rand = new Random();

    public static int shoot(int N, int count_shoot){
        int result = 0;
        while(count_shoot > 0){
            result += rand.nextInt(N);
            count_shoot--;
        }
        return result;
    }
}
