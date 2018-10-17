package main.sprite;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.PlayMode;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private ArrayList<Image> states = new ArrayList<>();
    private ArrayList<Vec2d> offsets = new ArrayList<>();
    private int lastIndex;
    private Vec2i initialOffset;
    private int scale;

    public SpriteAnimation(ImageView imageView, Duration duration, Vec2i coord, int setScale) {
        this.imageView = imageView;
        this.initialOffset = coord;
        this.scale = setScale;
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

    public void addState(Image image) {
        addState(image, new Vec2d(0, 0));
    }

    public void addState(Image image, Vec2d offset) {
        states.add(image);
        offsets.add(offset);
    }

//    public void alignToCorner(double scaleFactor, double direction, int i) {
//        if (states.isEmpty()) return;
//
//        Image image = states.get(0);
//        double baseHeight = image.getHeight();
//        Vec2d baseOffset = offsets.get(0);
//
//        double height = states.get(i).getHeight();
//        double width = states.get(i).getWidth();
//    }

    public void alignToDown(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseHeight = viewport.getHeight();
        Vec2d baseOffset = offsets.get(0);

        double height = states.get(i).getHeight();
        offsets.get(i).setY(scaleFactor*(baseHeight-height));
//        offsets.set(i, baseOffset.add(0,scaleFactor*(baseHeight-height)));
//        System.out.printf("%d, %f,%f Down\n", i, baseHeight, height);
//        System.out.println(offsets);



    }

    public void alignToRight(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseWidth = viewport.getWidth();
        Vec2d baseOffset = offsets.get(0);

//        for (int i = 1; i < states.size(); i++) {
//            double width = states.get(i).getWidth();
//            offsets.set(i, baseOffset.add(scaleFactor * (baseWidth - width), 0));
//        }
        double width = states.get(i).getWidth();
        offsets.get(i).setX(scaleFactor * (baseWidth - width));
//        System.out.printf("%d, %f,%f RIGHT\n", i, baseWidth, width);
//        offsets.set(i, baseOffset.add(scaleFactor * (baseWidth - width),0));
//        System.out.println(offsets);
    }

    public void alignToUp(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseHeight = viewport.getHeight();
        Vec2d baseOffset = offsets.get(0);

        double height = states.get(i).getHeight();
        offsets.get(i).setY((scaleFactor - 1) * (height - baseHeight));
//        baseOffset.setY((scaleFactor - 1) * (height - baseHeight));
//        System.out.printf("%d, %f,%f UP\n", i, baseHeight, height);
//        offsets.set(i, baseOffset);
//        offsets.set(i, offsets.get(i))
//        System.out.println(offsets);

    }

    public void alignToLeft(double scaleFactor, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        double baseWidth = viewport.getWidth();
        Vec2d baseOffset = offsets.get(0);

//        for (int i = 1; i < states.size(); i++) {
//            double width = states.get(i).getWidth();
//            offsets.set(i, baseOffset.add((scaleFactor - 1) * (width - baseWidth), 0));
//        }
        double width = states.get(i).getWidth();
        offsets.get(i).setX((scaleFactor - 1) * (width - baseWidth));
//        System.out.printf("%d,%f,%f LEFT\n", i,baseWidth, width);
//        baseOffset.setX((scaleFactor - 1) * (width - baseWidth));
//        offsets.set(i, baseOffset);
//        System.out.println(offsets);

    }

    public void alignManual(Vec2d coord, int i) {
        if (states.isEmpty()) return;

        Image viewport = states.get(0);
        Vec2d baseOffset = offsets.get(0);
        offsets.get(i).setX((baseOffset.getX()+coord.getX()));
        offsets.get(i).setY(baseOffset.getY()+coord.getY());
//        System.out.printf("%d %f %f MANUAL\n",i, coord.getX(), coord.getY());
//        System.out.println(offsets);
    }

    private void setState(int index) {
        Image viewport = states.get(index);
        imageView.setTranslateX(offsets.get(index).getX());
        imageView.setTranslateY(offsets.get(index).getY());
        imageView.setImage(viewport);
    }



    @Override
    public void play() {
        this.imageView.setX(initialOffset.getX());
        this.imageView.setY(initialOffset.getY());
        this.imageView.setScaleX(scale);
//TODO: turn off input when animation plays
        PlayMode.input.stopListening();
        System.out.println("STOP INPUT");
        this.setOnFinished(e -> {
            PlayMode.input.startListening();
            System.out.println("START INPUT");
        });
        super.play();
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