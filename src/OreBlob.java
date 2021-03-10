import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class OreBlob extends AnimatedEntity
{
    private static final String QUAKE_KEY = "quake";

    public OreBlob(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public boolean moveToOreBlob(
            Entity blob,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(blob.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionOreBlob(blob, world, target.getPosition());

            if (!blob.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(blob, nextPos);
            }
            return false;
        }
    }

    public void executeActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(entity.getPosition(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(entity, world, blobTarget.get(), scheduler)) {
                Entity quake = Factory.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                ((ActiveEntity)quake).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(entity,
                Factory.createActivityAction(entity, world, imageStore),
                nextPeriod);
    }

    public Point nextPositionOreBlob(
            Entity entity, WorldModel world, Point destPos)
    {
        PathingStrategy aStarStrategy = new AStarPathingStrategy();

        List<Point> nextPoints = aStarStrategy.computePath(getPosition(), destPos, canPassThrough(world), withinReach(), PathingStrategy.CARDINAL_NEIGHBORS);

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
