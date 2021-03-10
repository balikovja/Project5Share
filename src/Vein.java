import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActiveEntity
{
    private static final Random rand = new Random();
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    public Vein(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public void executeActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            Entity ore = Factory.createOre(ORE_ID_PREFIX + getId(), openPt.get(),
                    ORE_CORRUPT_MIN + rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            ((ActiveEntity)ore).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(entity,
                Factory.createActivityAction(entity, world, imageStore),
                getActionPeriod());
    }
}
