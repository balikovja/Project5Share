import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class MinerFull extends Miner
{
    public MinerFull(
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

    public void transformFull(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Entity miner = Factory.createMinerNotFull(getId(), getResourceLimit(),
                getPosition(), getActionPeriod(),
                getAnimationPeriod(),
                getImages());

        world.removeEntity(entity);
        scheduler.unscheduleAllEvents(entity);

        world.addEntity(miner);
        ((ActiveEntity)miner).scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveTo(
            Entity miner,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(miner.getPosition(), target.getPosition())) {
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
        Optional<Entity> fullTarget =
                world.findNearest(getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() && moveTo(entity, world,
                fullTarget.get(), scheduler))
        {
            transformFull(entity, world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(entity,
                    Factory.createActivityAction(entity, world, imageStore),
                    getActionPeriod());
        }
    }
}
