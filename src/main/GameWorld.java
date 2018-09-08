package main;

import javafx.scene.Group;
import javafx.scene.Node;
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
    }

    public void update(double delta) {
        avatar.update(delta);

        Vec2i pos = avatar.getGridPos();
        pos.clip(new Vec2i(0,0), new Vec2i(map.getNCols() - 1, map.getNRows() - 1));
        avatar.moveTo(pos);

    }

    public void render() {
        avatar.render();
    }

    public Node getView() {
        return rootView;
    }

    public Vec2d gridPosToWorldPos(Vec2i gridPos) {
        return map.gridPosToWorldPos(gridPos);
    }
}
