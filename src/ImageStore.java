import java.util.*;

import processing.core.PApplet;
import processing.core.PImage;

public final class ImageStore
{
    private Map<String, List<PImage>> images;
    private List<PImage> defaultImages;

    public ImageStore(PImage defaultImage) {
        this.images = new HashMap<>();
        defaultImages = new LinkedList<>();
        defaultImages.add(defaultImage);
    }
    public void loadImages(
            Scanner in, PApplet screen)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                Functions.processImageLine(this.images, in.nextLine(), screen);
            }
            catch (NumberFormatException e) {
                System.out.println(
                        String.format("Image format error on line %d",
                                lineNumber));
            }
            lineNumber++;
        }
    }
    public List<PImage> getImageList(String key) {
        return this.images.getOrDefault(key, this.defaultImages);
    }
    public static PImage getCurrentImage(Object entity) {
        if (entity instanceof Background) {
            return ((Background)entity).getImages().get(
                    ((Background)entity).getImageIndex());
        }
        else if (entity instanceof Entity) {
            return ((Entity)entity).getImages().get(((Entity)entity).getImageIndex());
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }

}
