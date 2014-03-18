package MonsterQuest;

/**
 * Created by Alexander on 3/18/14.
 */
public abstract class Monster {
    protected final MonsterType type;
    protected Location location;

    public Monster(MonsterType type, Location location){
        this.type = type;
        this.location = location;
    }

    private void saveStateToBD() {
    //TODO new table in DB
    }

    public abstract void move();

    public Location getLocation() {
        return location;
    }
}