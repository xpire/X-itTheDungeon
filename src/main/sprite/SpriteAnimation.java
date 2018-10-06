package main.sprite;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.math.Vec2d;

import java.util.ArrayList;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private ArrayList<Rectangle2D> states = new ArrayList<>();
    private ArrayList<Vec2d> offsets = new ArrayList<>();
    private int lastIndex;

    public SpriteAnimation(ImageView imageView, Duration duration) {
        this.imageView = imageView;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * states.size()), states.size() - 1);

        if (index == lastIndex) return;

        setState(index);
        lastIndex = index;
    }

    public void addState(Rectangle2D viewport) {
        addState(viewport, new Vec2d(0, 0));
    }

    public void addState(Rectangle2D viewport, Vec2d offset) {
        states.add(viewport);
        offsets.add(offset);
    }

    public void alignToRight(double scaleFactor) {
        if (states.isEmpty()) return;

        Rectangle2D viewport = states.get(0);
        double baseWidth = viewport.getWidth();
        Vec2d baseOffset = offsets.get(0);

        for (int i = 1; i < states.size(); i++) {
            double width = states.get(i).getWidth();
            offsets.set(i, baseOffset.add(scaleFactor * (baseWidth - width), 0));
        }
    }

    public void alignToLeft(double scaleFactor) {
        if (states.isEmpty()) return;

        Rectangle2D viewport = states.get(0);
        double baseWidth = viewport.getWidth();
        Vec2d baseOffset = offsets.get(0);

        for (int i = 1; i < states.size(); i++) {
            double width = states.get(i).getWidth();
            offsets.set(i, baseOffset.add((scaleFactor - 1) * (width - baseWidth), 0));
        }
    }

    private void setState(int index) {
        Rectangle2D viewport = states.get(index);
        imageView.setTranslateX(offsets.get(index).getX());
        imageView.setTranslateY(offsets.get(index).getY());
        imageView.setViewport(viewport);
    }
}

//    private final ImageView imageView;
//    private final int count;
//    private final int columns;
//    private final int offsetX;
//    private final int offsetY;
//    private final int width;
//    private final int height;
//
//    private int lastIndex;
//
//    public SpriteAnimation(ImageView imageView, Duration duration,
//            int count,   int columns,
//            int offsetX, int offsetY,
//            int width,   int height) {
//
//        this.imageView = imageView;
//        this.count     = count;
//        this.columns   = columns;
//        this.offsetX   = offsetX;
//        this.offsetY   = offsetY;
//        this.width     = width;
//        this.height    = height;
//
//        setCycleDuration(duration);
//        setInterpolator(Interpolator.LINEAR);
//    }
//
//    @Override
//    protected void interpolate(double k) {
//        final int index = Math.min((int) Math.floor(k * count), count - 1);
//        if (index != lastIndex) {
//            final int x = (index % columns) * width  + offsetX;
//            final int y = (index / columns) * height + offsetY;
//            imageView.setViewport(new Rectangle2D(x, y, width, height));
//            lastIndex = index;
//        }
//    }

