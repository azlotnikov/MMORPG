package MonsterQuest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by razoriii on 04.03.14.
 */
public enum Direction {
   NONE, NORTH, SOUTH, EAST, WEST;

   private static final List<Direction> VALUES =
           Collections.unmodifiableList(Arrays.asList(values()));
   private static final int SIZE = VALUES.size();
   private static final Random RANDOM = new Random();

   public static Direction randomDirection()  {
      return VALUES.get(RANDOM.nextInt(SIZE));
   }

}
