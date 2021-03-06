package main.sprite;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.HashMap;

/**
 * Class managing all sprites used in our game
 * This includes loading the sprite, storing it, setting its states
 * adding animations, parsing direction
 */
public class SpriteView extends ImageView{

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
    }

    public void addAnime(String name, SpriteAnimation animation) {
        anime.put(name, animation);
    }

    public void playAnime(String name, EventHandler<ActionEvent> afterFinish) {
        SpriteAnimation animation = anime.get(name);
        if (animation == null) return;
        animation.play(afterFinish);
    }

    public void playAnimation(String name, Vec2i direction, EventHandler<ActionEvent> afterFinish){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(directionParser(direction));
        playAnime(sb.toString(), afterFinish);
    }

    public String directionParser(Vec2i direction) {
        if (direction == Vec2i.NORTH) {
            return "Up";
        } else if (direction == Vec2i.SOUTH) {
            return "Down";
        } else if (direction == Vec2i.EAST) {
            return "Right";
        } else if (direction == Vec2i.WEST) {
            return "Left";
        } else {
            return "Up";
        }
    }

    public void magnifyScales(double newScaleX, double newScaleY) {
        scaleX.put("Default", scaleX.get("Default") * newScaleX);
        scaleY.put("Default", scaleY.get("Default") * newScaleY);

        setState("Default");
    }

}