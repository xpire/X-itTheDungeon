package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.avatar.Avatar;
import main.enemies.Enemy;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Pit extends Entity {

    private Rectangle rect;


    {
        symbol = '#';
    }


    public Pit() {
        super("Switch");
    }

    public Pit(String name) {
        super(name);
    }

    public Pit(String name, Level level, Vec2i pos) {
        super(name, level, pos);
    }


    @Override
    public void onCreated(){

        rect = new Rectangle(30, 30, Color.BLACK);
        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public void onEntityEnter(Entity other) {
        if (other.getName().equals("Avatar")) {
            Avatar avatar = (Avatar) other;

            if (!avatar.isHovering())
                avatar.onDeath();

        }
        else if (other.getName().equals("Boulder")) {
            other.onDestroyed();
        }
    }

    @Override
    public boolean isPassableFor(Entity other) {
        if (other instanceof Enemy) {
            return false;
        }

        return true;
    }
}