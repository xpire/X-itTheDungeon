package main;

import javafx.scene.Group;
import javafx.scene.Node;
import main.avatar.Avatar;
import main.entities.Boulder;
import main.entities.Wall;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;
import main.systems.PushingBoulderSystem;

public class GameWorld {

    private TileMap map;
    private Avatar avatar;

    private Group rootView;

    private PushingBoulderSystem pushSystem = new PushingBoulderSystem(this);

    public GameWorld(TileMap map) {
        this.map = map;

        rootView = new Group();


        rootView.setTranslateX(150);
        rootView.setTranslateY(50);

        avatar = new Avatar(this);

        map.addNewEntity(5, 5, new Wall());
        map.addNewEntity(2, 8, new Wall());
        map.addNewEntity(1, 3, new Wall());
        map.addNewEntity(2, 0, new Wall());
        map.addNewEntity(4, 3, new Boulder());
        map.addNewEntity(7, 6, new Boulder());

        rootView.getChildren().add(map.getView());
        rootView.getChildren().add(avatar.getView());
    }


    public void update(double delta) {
        avatar.update(delta);

        Vec2i pos = avatar.getGridPos();
//        pos.clip(new Vec2i(0,0), new Vec2i(map.getNCols() - 1, map.getNRows() - 1));
    }

    public void render() {
        avatar.render();
    }

    public Node getView() {
        return rootView;
    }

    public Vec2d gridPosToWorldPosCentre(Vec2i gridPos) {
        return map.gridPosToWorldPosCentre(gridPos);
    }


    public boolean isPassable(Vec2i pos) {
        if (!map.isValidGridPos(pos)) return false;
        return map.isPassable(pos);
    }


    // WILL BE REMOVED
    public Boulder getBoulder(Vec2i pos) {
        if (!map.isValidGridPos(pos)) return null;
        return map.getTile(pos).getBoulder();
    }

    public TileMap getMap() {
        return map;
    }

    public void push(Avatar avatar, Vec2i pos) {

        Boulder boulder = getBoulder(pos);

        if (boulder == null) return;

        Vec2i from = avatar.getGridPos();
        Vec2i target = boulder.getGridPos();

        Vec2i push = pushSystem.push(from, target);
        map.getTile(pos).removeEntity(boulder);
        map.getTile(pos.add(push)).addEntity(boulder);

        avatar.moveBy(push);
        boulder.moveBy(push);
    }
}
