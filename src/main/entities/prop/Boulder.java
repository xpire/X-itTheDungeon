package main.entities.prop;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.entities.enemies.Enemy;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Boulder extends Prop {


    {
        symbol = 'O';
        isHeavy = true;
    }

    public Boulder(Level level) {
        super(level);
    }

    public Boulder(Level level, Vec2i pos) {
        super(level, pos);
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
    public boolean isPassableForEnemy(Enemy enemy) {
        return false;
    }

    @Override
    public boolean isPassableForProp(Prop prop) { return false; }

    @Override
    public boolean isPassableForAvatar(Avatar avatar) {
        return false;
    }


    @Override
    public boolean onPush(Avatar avatar) {
        Vec2i dir = pos.sub(avatar.getGridPos());

        if (level.isPassableForProp(pos.add(dir), this)) {
            level.moveProp(pos.add(dir), this);
            return true;
        }

        return false;
    }

    @Override
    public void onExploded() {
        onDestroyed();
    }
}
