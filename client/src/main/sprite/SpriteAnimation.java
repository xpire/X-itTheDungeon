package main.sprite;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;

    private ArrayList<Image> states = new ArrayList<>();
    private ArrayList<Vec2d> offsets = new ArrayList<>();
    private int lastIndex;
    private Vec2i initialOffset;
    private double scaleX;
    private double scaleY;

    private Runnable beforePlay;
    private Runnable afterPlay;

    public SpriteAnimation(ImageView imageView, Duration duration, Vec2i coord,
                           double setScaleX, double setScaleY) {
        this(imageView, duration, coord, setScaleX, setScaleY, () -> {}, () -> {});
    }

    public SpriteAnimation(ImageView imageView, Duration duration, Vec2i coord,
                           double setScaleX, double setScaleY,
                           Runnable beforePlay, Runnable afterPlay) {

        this.imageView      = imageView;
        this.initialOffset  = coord;
        this.scaleX         = setScaleX;
        this.scaleY         = setScaleY;

        this.beforePlay     = beforePlay;
        this.afterPlay      = afterPlay;

        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * states.size()), states.size() - 1);

        if (index == lastIndex)
            return;

        setState(index);
        lastIndex = index;
    }

    public void addState(Image image) {
        addState(image, new Vec2d(0, 0));
    }

    public void addState(Image image, Vec2d offset) {
        states.add(image);
        offsets.add(offset);
    }

    public void alignToDown(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseHeight = viewport.getHeight();
        double height = states.get(i).getHeight();
        offsets.get(i).setY(scaleFactor*(baseHeight-height));
    }

    public void alignToRight(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseWidth = viewport.getWidth();
        double width = states.get(i).getWidth();
        offsets.get(i).setX(scaleFactor * (baseWidth - width));
    }

    public void alignToUp(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseHeight = viewport.getHeight();
        double height = states.get(i).getHeight();
        offsets.get(i).setY((scaleFactor - 1) * (height - baseHeight));
    }

    public void alignToLeft(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseWidth = viewport.getWidth();
        double width = states.get(i).getWidth();
        offsets.get(i).setX((scaleFactor - 1) * (width - baseWidth));
    }

    private void setState(int index) {
        Image viewport = states.get(index);
        imageView.setTranslateX(offsets.get(index).getX());
        imageView.setTranslateY(offsets.get(index).getY());
        imageView.setImage(viewport);
    }

    public void alignOffset(Vec2d offset) {
        for (Vec2d o : offsets) {
            o.add(new Vec2d(30,0));
        }
        initialOffset.add(30,0);
    }

    public void play(EventHandler<ActionEvent> afterFinish) {
        imageView.setX(initialOffset.getX());
        imageView.setY(initialOffset.getY());
        imageView.setScaleX(scaleX);
        imageView.setScaleY(scaleY);

        beforePlay.run();
        setOnFinished(e -> {
            afterPlay.run();
            afterFinish.handle(e);
        });
        super.play();
    }
}