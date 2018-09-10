package main;

import javafx.scene.Group;
import javafx.scene.Node;
import main.avatar.Avatar;
import main.component.GridPositionComponent;
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

        GridPositionComponent gpc = new GridPositionComponent(pos2i ->
            gridPosToWorldPosCentre(pos2i)
        );

        avatar = new Avatar(gpc.clone(), this);

        map.addEntity(5, 5, new Wall(gpc.clone()));
        map.addEntity(2, 8, new Wall(gpc.clone()));
        map.addEntity(1, 3, new Wall(gpc.clone()));
        map.addEntity(2, 0, new Wall(gpc.clone()));
        map.addEntity(4, 3, new Boulder(gpc.clone()));
        map.addEntity(7, 6, new Boulder(gpc.clone()));

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
