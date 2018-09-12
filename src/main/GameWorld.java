package main;

import javafx.scene.Group;
import javafx.scene.Node;
import main.avatar.Avatar;
import main.entities.*;
import main.maploading.TileMap;
import main.math.Vec2i;
import main.systems.GridMovementSystem;
import main.systems.PushSystem;

import java.util.Iterator;

public class GameWorld {

    private TileMap map;
    private Avatar avatar;

    private Group rootView;

    private PushSystem pushSystem;
    private GridMovementSystem moveSystem;

    public GameWorld(TileMap map) {
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



        addNewEntity(5, 5, new Wall());
        addNewEntity(2, 8, new Wall());
        addNewEntity(7, 7, key2);
        addNewEntity(5, 3, key1);
        addNewEntity(8, 3, door2);
        addNewEntity(1, 3, door1);
        addNewEntity(2, 0, new Wall());
        addNewEntity(4, 3, new Boulder());
        addNewEntity(7, 6, new Boulder());

        Switch sw = new Switch();
        addNewEntity(3, 3, sw);

        Iterator<Entity> it = map.getEntities(new Vec2i(5, 5));

        rootView.getChildren().add(map.getView());
        rootView.getChildren().add(avatar.getView());

    }

    public void addNewEntity(int row, int col, Entity e) {

        if (e instanceof  Boulder) {
            ((Boulder) e).attachPushSystem(pushSystem);
        }
        map.addNewEntity(row, col, e);
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

    public TileMap getMap() {
        return map;
    }


    // ambiguous name
    public void moveEntity(Entity e, Vec2i pos) {
        if (!map.isValidGridPos(pos)) return;

        moveSystem.onTileMovement(e, pos);
    }
}
