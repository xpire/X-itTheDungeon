package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Game;
import main.avatar.Avatar;
import main.maploading.Level;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.Iterator;

public class Bomb extends Entity {

    private Circle circ;

    private int fuseLength = 5;
    private boolean isLit = false;

    {
        symbol = '!';
    }

    public Bomb() {
        super("Bomb");
    }

    public Bomb(String name) {
        super(name);
    }

    public Bomb(String name, char symbol) {
        super(name, symbol);
    }

    public Bomb(String name, Level map, Vec2i pos) {
        super(name, map, pos);
    }


    public void onLit() {
        isLit = true;
    }


    public void onExplode() {
        destroyEntity(pos.add(-1, 0));
        destroyEntity(pos.add(1, 0));
        destroyEntity(pos.add(0, -1));
        destroyEntity(pos.add(0, 1));
    }

    public void destroyEntity(Vec2i target) {
        Iterator<Entity> it = map.getEntities(target);
        while(it.hasNext()) {
            Entity e = it.next();

            if (e.getName().equals("Boulder")) {
                map.removeEntity(e);
            }
            else if (e.getName().equals("Avatar")) {
                Avatar avatar = ((Avatar) e);

                if (!avatar.isRaged()) {
                    avatar.onDeath();
                }
            }
        }
    }


    public void onTurnUpdate() {
        if (!isLit) return;

        fuseLength--;
        circ.setFill(Color.rgb((int)((1 - fuseLength/5.0) * 255),0,0));

        if (fuseLength <= 0) {
            onExplode();
            Game.world.removeBomb(this);
            onRemovedFromMap();
        }
    }



    @Override
    public void onCreated(){
        circ = new Circle(5, Color.BLACK);
        view.addNode(circ);
        view.setCentre(new Vec2d(0, 0));
    }


    @Override
    public boolean isPassableFor(Entity other) {
        if (other.getName().equals("Boulder") || other.getName().equals("Key")) {
            return false;
        }

        return true;
    }


    @Override
    public void onEntityEnter(Entity other) {

        if (isLit) return;

        if (other instanceof Avatar) {
            Avatar avatar = (Avatar) other;

            if (avatar.pickUp(this)) {
                onRemovedFromMap();
            }
        }
    }
}
