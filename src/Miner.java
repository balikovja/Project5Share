import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Miner extends AnimatedEntity
{
    private int resourceLimit;
    private int resourceCount;

    public Miner(String id, Point position, List<PImage> images, int actionPeriod,
                 int animationPeriod, int resourceCount, int resourceLimit)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit= resourceLimit;
    }

    protected boolean moveTo(
            Entity miner,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        Point nextPos = nextPositionMiner(miner, world, target.getPosition());

        if (!miner.getPosition().equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(miner, nextPos);
        }
        return false;
    }
    protected int getResourceCount()
    {
        return resourceCount;
    }
    protected void setResourceCount(int resourceCount)
    {
        this.resourceCount = resourceCount;
    }
    protected int getResourceLimit()
    {
        return resourceLimit;
    }
    protected Point nextPositionMiner(
            Entity entity, WorldModel world, Point destPos)
    {
        PathingStrategy aStarStrategy = new AStarPathingStrategy();

        List<Point> nextPoints = aStarStrategy.computePath(getPosition(), destPos, canPassThrough(world), withinReach(), PathingStrategy.CARDINAL_NEIGHBORS); //comment

        if (nextPoints.size() == 0) {

            return getPosition();
        }
        return nextPoints.get(0);
    }
    private static Predicate<Point> canPassThrough(WorldModel world) {
        return p -> (world.withinBounds(p) && !world.isOccupied(p));
    }


    private static BiPredicate<Point, Point> withinReach() {
        return Point::adjacent;
    }
}
