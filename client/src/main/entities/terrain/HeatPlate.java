package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.EnumMap;

/**
 * Class describing the Pit entity
 */
public class HeatPlate extends Terrain{

    private Rectangle[] plates;

    private static final int COLD = 0;
    private static final int GLOW = 1;
    private static final int WARM = 2;
    private static final int BURN = 3;
    private static final int TURNS = 4;

    private int state;

    {
        symbol = 'H';
    }

    /**
     * Basic constructor
     * @param level
     */
    public HeatPlate(Level level) {
        super(level);
    }

    public HeatPlate(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        plates = new Rectangle[4];
        state = COLD;

        for (int i = 0; i < plates.length; i++) {
            plates[i] = new Rectangle(10, 10, Color.BLACK);
            view.addNode(plates[i]);
        }
        plates[1].setX(20);
        plates[2].setY(20);
        plates[3].setX(20);
        plates[3].setY(20);

        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public void onTurnUpdate() {
        state = (state + 1) % TURNS;

        Color color = Color.BLACK;

        switch(state) {
            case COLD:
                color = Color.BLACK;
                break;
            case GLOW:
                color = Color.rgb(100,10,10);
                break;
            case WARM:
                color = Color.rgb(150,30,30);
                break;
            case BURN:
                color = Color.RED;
                break;
        }

        for (int i = 0; i < plates.length; i++) {
            plates[i].setFill(color);
        }

        if (state == BURN) {
            if (level.hasEnemy(pos)) {
                level.getEnemy(pos).onDestroyed(); //TodO differentiate between onDestroyed and destroy
            }

            if (level.hasAvatar(pos)) {
                level.getAvatar().onThreatenedByBomb(null);
            }
        }
    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return true;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }

    @Override
    public void onEnterByEnemy(Enemy enemy) {
        if (state == BURN)
            enemy.onDestroyed();
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        if (state == BURN)
            avatar.onThreatenedByBomb(null);
    }
}