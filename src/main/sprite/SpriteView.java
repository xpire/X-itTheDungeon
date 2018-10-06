package main.sprite;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.math.Vec2d;

import java.util.HashMap;

public class SpriteView extends ImageView{

//    private Vec2d baseOffset = new Vec2d();
    private HashMap<String, Image> states = new HashMap<>();
    private HashMap<String, Vec2d> offsets = new HashMap<>();
    private HashMap<String, SpriteAnimation> anime = new HashMap<>();
    private HashMap<String, Integer> scale = new HashMap<>();

    public SpriteView() {
        super();
    }

    public void addState(String name, Image image) {
        states.put(name, image);
        offsets.put(name, new Vec2d(0, 0));
        scale.put(name,1);
    }

    public void addState(String name, Image image, Vec2d offset, int scaleToBeSet) {
        states.put(name, image);
        offsets.put(name, offset);
        scale.put(name,scaleToBeSet);
    }

    public void addAnime(String name, SpriteAnimation animation) {
        anime.put(name, animation);
    }

    public void setState(String name) {
        Image image = states.get(name);
        Vec2d offset = offsets.get(name);
        int scaleToBeSet = scale.get(name);

        if (image == null)
            return;

        setImage(image);
        setX(offset.getX());
        setY(offset.getY());
        setScaleX(scaleToBeSet);
//        setTranslateX(offset.getX());
//        setTranslateY(offset.getY());
    }

    public void playAnime(String name) {
        SpriteAnimation animation = anime.get(name);
        if (animation == null) return;
        animation.play();
    }

    public class SpriteState {
        private Rectangle2D viewport;
        private Vec2d offset;
    }
}
