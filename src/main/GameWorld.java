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

public class GameWorld {

    private TileMap map;
    private Avatar avatar;

    private Group rootView;

    public GameWorld(TileMap map) {
        this.map = map;
        this.avatar = new Avatar(this);

        rootView = new Group();
        rootView.getChildren().add(map.getView());
        rootView.getChildren().add(avatar.getView());


        rootView.setTranslateX(150);
        rootView.setTranslateY(50);

        GridPositionComponent gpc = new GridPositionComponent(pos2i ->
            gridPosToWorldPosCentre(pos2i)
        );

        map.addGridEntity(5, 5, new Wall(gpc));
        map.addGridEntity(2, 8, new Wall(gpc));
        map.addGridEntity(1, 3, new Wall(gpc));
        map.addGridEntity(2, 0, new Wall(gpc));
        map.addEntity(4, 4, new Boulder());
    }


    public void update(double delta) {
        avatar.update(delta);

        Vec2i pos = avatar.getGridPos();
//        pos.clip(new Vec2i(0,0), new Vec2i(map.getNCols() - 1, map.getNRows() - 1));

        avatar.moveTo(pos);

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
//        pos.within(new Vec2i(0,0), new Vec2i(map.getNCols() - 1, map.getNRows() - 1));
        if (!pos.withinX(0, map.getNCols() - 1)) return false;
        if (!pos.withinY(0, map.getNRows() - 1)) return false;
        return map.isPassable(pos);
    }



    public TileMap getMap() {
        return map;
    }

//    public void push(Avatar avatar, Boulder boulder) {
//
//        Vec2i from = avatar.getGridPos();
//        Vec2i to = map.getGridPosFor(boulder);
//
//        Vec2i push = pushBoulderSystem.push(from, to);
//        avatar.
//    }
}
