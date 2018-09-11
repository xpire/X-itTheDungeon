package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.math.Vec2d;
import main.math.Vec2i;
import main.systems.PushSystem;

import java.util.function.Function;

public class Boulder extends Entity{

    private PushSystem pushSystem = null;

    public Boulder() {
        super("Wall");
    }

    public Boulder(String name) {
        super(name);
    }

    public Boulder(String name, Function<Vec2i, Vec2d> gridToWorld) {
        super(name, gridToWorld);
    }

    public Boulder(String name, Function<Vec2i, Vec2d> gridToWorld, Vec2i pos) {
        super(name, gridToWorld, pos);
    }

    @Override
    public void onCreated(){
        Circle circle = new Circle();
        circle.setFill(Color.HONEYDEW);
        circle.setRadius(12);

        view.addNode(circle);
        view.setCentre(new Vec2d(0, 0));
    }

    @Override
    public boolean isPassable() {
        return false;
    }


    @Override
    public boolean onInvadeBy(Entity other) {

        System.out.println(other.getName());
        if (other.getName().equals("Avatar")) {

            if (pushSystem == null) return false;

            Vec2i dir = pos.sub(other.getGridPos());
            moveBy(dir);

            return pushSystem.canPushInto(pos.add(dir));
        }

        return false;
    }


    public void attachPushSystem(PushSystem pushSystem) {
        this.pushSystem = pushSystem;
    }
}
