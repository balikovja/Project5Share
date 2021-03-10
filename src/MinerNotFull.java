import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner
{
    public MinerNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, resourceCount, resourceLimit);
    }

    public boolean transformNotFull(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (getResourceCount() >= getResourceLimit()) {
            Entity miner = Factory.createMinerFull(getId(), getResourceLimit(),
                    getPosition(), getActionPeriod(),
                    getAnimationPeriod(),
                    getImages());

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(miner);
            ((ActiveEntity)miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveTo(
            Entity miner,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(miner.getPosition(), target.getPosition())) {
            setResourceCount(getResourceCount() + 1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            return super.moveTo(miner, world, target, scheduler);
        }
    }

    public void executeActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !moveTo(entity, world,
                notFullTarget.get(),
                scheduler)
                || !transformNotFull(entity, world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(entity,
                    Factory.createActivityAction(entity, world, imageStore),
                    getActionPeriod());
        }
    }
}
