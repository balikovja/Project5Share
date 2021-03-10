import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    protected int getImageIndex()
    {
        return imageIndex;
    }
    protected Point getPosition()
    {
        return position;
    }
    protected void setPosition(Point position)
    {
        this.position = position;
    }
    protected List<PImage> getImages()
    {
        return images;
    }
    protected PImage getCurrentImage(Object entity)
    {
        return images.get(this.imageIndex);
    }
    protected void setImageIndex(int imageIndex)
    {
        this.imageIndex = imageIndex;
    }
    protected String getId()
    {
        return this.id;
    }
}
