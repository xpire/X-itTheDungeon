package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Game;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.systems.PushSystem;

public class Boulder extends Entity{

    private PushSystem pushSystem = null;

    public Boulder() {
        super("Boulder");
    }

    public Boulder(String name) {
        super(name);
    }

    public Boulder(String name, Level map, Vec2i pos) {
        super(name, map, pos);
    }

    @Override
    public void onCreated(){
        symbol = 'O';

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
    public boolean onEntityPush(Entity other) {

        System.out.println(other.getName());
        if (other.getName().equals("Avatar")) {

            if (pushSystem == null) return false;

            Vec2i curr  = new Vec2i(pos);
            Vec2i dir   = curr.sub(other.getGridPos());

            System.out.println("Push in the direction: " + dir);

            if (pushSystem.canPushInto(curr.add(dir))) {
                Game.world.moveEntity(this, curr.add(dir));
                return true;
            }
        }

        return false;
    }


    public void attachPushSystem(PushSystem pushSystem) {
        this.pushSystem = pushSystem;
    }
}
