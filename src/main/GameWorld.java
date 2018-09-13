package main;

import javafx.scene.Group;
import javafx.scene.Node;
import main.avatar.Avatar;
import main.entities.*;
import main.maploading.Level;
import main.math.Vec2i;
import main.systems.GridMovementSystem;
import main.systems.PushSystem;

import java.util.Iterator;

public class GameWorld {

    private Level map;
    private Avatar avatar;

    private Group rootView;

    private PushSystem pushSystem;
    private GridMovementSystem moveSystem;

    public GameWorld(Level map) {
        this.map = map;

        rootView = new Group();


        rootView.setTranslateX(150);
        rootView.setTranslateY(50);

        pushSystem = new PushSystem(this);
        moveSystem = new GridMovementSystem(this, map);

        avatar = new Avatar(this);
        addNewEntity(0, 0, avatar);

        Door door1 = new Door();
        Door door2 = new Door();

        Key key1 = new Key();
        Key key2 = new Key();
        key1.setMatchingDoor(door1);
        key2.setMatchingDoor(door2);

        Switch sw = new Switch();
        addNewEntity(3, 3, sw);

        addNewEntity(5, 5, new Wall());
        addNewEntity(2, 8, new Wall());
        addNewEntity(7, 7, key2);
        addNewEntity(5, 3, key1);
        addNewEntity(8, 3, door2);
        addNewEntity(1, 3, door1);
        addNewEntity(2, 0, new Wall());
        addNewEntity(4, 3, new Boulder());
        addNewEntity(7, 6, new Boulder());



        Iterator<Entity> it = map.getEntities(new Vec2i(5, 5));

        rootView.getChildren().add(map.getView());
        rootView.getChildren().add(avatar.getView());

    }
    public void addEntity(int x, int y, Entity e) {
        map.addEntity(x, y, e);
    }

    public void addNewEntity(int x, int y, Entity e) {

        if (e instanceof  Boulder) {
            ((Boulder) e).attachPushSystem(pushSystem);
        }
        map.addNewEntity(x, y, e);
    }


    public void update(double delta) {
        avatar.update(delta);
    }

    public void render() {
        avatar.render();
    }

    public Node getView() {
        return rootView;
    }



    public boolean isPassable(Vec2i pos) {
        if (!map.isValidGridPos(pos)) return false;
        return map.isPassable(pos);
    }


    // WILL BE REMOVED
//    public Boulder getBoulder(Vec2i pos) {
//        if (!map.isValidGridPos(pos)) return null;
//        return map.getTile(pos).getBoulder();
//    }

    public Level getMap() {
        return map;
    }


    public boolean onPlace(Entity entity, Vec2i pos) {
        Iterator<Entity> it = map.getEntities(pos);
        while(it.hasNext()) {
            Entity e = it.next();
            if(!e.onEntityPush(entity)) {
                return false;
            }
        }

        map.addEntity(pos.getX(), pos.getY(), entity);
        return true;
    }


    // ambiguous name
    public void moveEntity(Entity e, Vec2i pos) {
        if (!map.isValidGridPos(pos)) return;

        moveSystem.onTileMovement(e, pos);
    }


}
