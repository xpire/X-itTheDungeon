package main.sprite;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.HashMap;

public class SpriteView extends ImageView{

    //    private Vec2d baseOffset = new Vec2d();
    private HashMap<String, Image> states = new HashMap<>();
    private HashMap<String, Vec2d> offsets = new HashMap<>();
    private HashMap<String, Double> scaleX = new HashMap<>();
    private HashMap<String, Double> scaleY = new HashMap<>();
    private HashMap<String, SpriteAnimation> anime = new HashMap<>();

    public SpriteView(Image image, Vec2d offset, double scaleToBeSetX, double scaleToBeSetY) {
        super();
        addState("Default", image, offset, scaleToBeSetX, scaleToBeSetY);
        setState("Default");
    }

    public void addState(String name, Image image) {
        states.put(name, image);
        offsets.put(name, new Vec2d(0, 0));
        scaleX.put(name,1.0);
        scaleY.put(name,1.0);
    }

    public void addState(String name, Image image, Vec2d offset, double scaleToBeSetX, double scaleToBeSetY) {
        states.put(name, image);
        offsets.put(name, offset);
        scaleX.put(name, scaleToBeSetX);
        scaleY.put(name, scaleToBeSetY);
    }

    public void setState(String name) {
        Image image = states.get(name);
        Vec2d offset = offsets.get(name);
        double scaleToBeSetX = scaleX.get(name);
        double scaleToBeSetY = scaleY.get(name);


        if (image == null)
            return;

        setImage(image);
        setX(offset.getX());
        setY(offset.getY());
        setScaleX(scaleToBeSetX);
        setScaleY(scaleToBeSetY);
//        setTranslateX(offset.getX());
//        setTranslateY(offset.getY());
    }

    public void addAnime(String name, SpriteAnimation animation) {
        anime.put(name, animation);
    }

    public void playAnime(String name) {
        SpriteAnimation animation = anime.get(name);
        if (animation == null) return;
        animation.play();
    }

    public void playAnimation(String name, Vec2i direction){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ");
        if (direction == Vec2i.NORTH) {
            sb.append("Up");
        } else if (direction == Vec2i.SOUTH) {
            sb.append("Down");
        } else if (direction == Vec2i.EAST) {
            sb.append("Right");
        } else if (direction == Vec2i.WEST) {
            sb.append("Left");
        }
        playAnime(sb.toString());
    }

    public void magnifyScales(double newScaleX, double newScaleY) {
        scaleX.put("Default", scaleX.get("Default") * newScaleX);
        scaleY.put("Default", scaleY.get("Default") * newScaleY);

        setState("Default");
    }

    public class SpriteState {
        private Rectangle2D viewport;
        private Vec2d offset;
    }

}