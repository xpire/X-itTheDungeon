package main.sprite;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.math.Vec2d;

import java.util.HashMap;

public class SpriteView extends ImageView{

//    private Vec2d baseOffset = new Vec2d();
    private HashMap<String, Rectangle2D> states = new HashMap<>();
    private HashMap<String, Vec2d> offsets = new HashMap<>();

    public SpriteView(Image image) {
        super(image);
    }

    public void addState(String name, Rectangle2D viewport) {
        states.put(name, viewport);
        offsets.put(name, new Vec2d(0, 0));
    }

    public void addState(String name, Rectangle2D viewport, Vec2d offset) {
        states.put(name, viewport);
        offsets.put(name, offset);
    }

    public void setState(String name) {
        Rectangle2D viewport = states.get(name);
        Vec2d offset = offsets.get(name);

        if (viewport == null)
            return;

        setViewport(viewport);
        setTranslateX(offset.getX());
        setTranslateY(offset.getY());
    }

    public class SpriteState {
        private Rectangle2D viewport;
        private Vec2d offset;
    }
}
