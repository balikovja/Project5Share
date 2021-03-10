import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity
{
    private int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }
    // I recognize the commonality between MinerFull, MinerNotFull, and OreBlob in both their nextPosition and moveTo
    // functions. However, quake does not share these characteristics. In order to make this common code chunk in the
    // super class of AnimatedEntity, I would have to implement these methods in quake even though quake doesn't use
    // them. This seems counter to what we are trying to do. I could implement an interface for the three because of the
    // common method names, but this seems a waste as I would not be able to put the common code in their since
    // interfaces can have no implementation of methods. So I decided that I would put the common code of the miners
    // in the abstract miner class, but to leave the common code in OreBlob alone. I hope that is sufficient.
    protected void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, 0),
                getAnimationPeriod());
    }
    protected int getAnimationPeriod()
    {
        return this.animationPeriod;
    }
    protected void nextImage()
    {
        setImageIndex((getImageIndex() + 1) % getImages().size());
    }
}
