package MonsterQuest;

/**
 * Created by Alexander on 3/18/14.
 */
public class RunningMonster extends Monster{
    private static final double step = 0.015;
    private Direction direction;

    public RunningMonster(Location location, Direction direction){
        super(MonsterType.RUNNING_MONSTER, location);
        this.direction = direction;
    }

    public void move(){
        Location newLocation = location.getNewLocation(direction, step);
        if (super.location.equal(newLocation))
            switch (direction) {
                case NORTH:
                    direction = Direction.SOUTH;
                    break;
                case SOUTH:
                    direction = Direction.NORTH;
                    break;
                case WEST:
                    direction = Direction.EAST;
                    break;
                case EAST:
                    direction = Direction.WEST;
                    break;
            }
        else
            super.location = newLocation;
    }
}
