package main.entities.prop;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.entities.Entity;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteAnimation;
import main.sprite.SpriteView;

import java.util.Iterator;

/**
 * Class describing the Lit Bomb entity
 */
public class LitBomb extends Prop{

    private Circle bomb;

    private final int MAX_FUSE_LENGTH = 5;
    private Integer fuseLength = MAX_FUSE_LENGTH;
    private int radius = 1;

    /**
     * Basic Constructor
     * @param level
     */
    public LitBomb(Level level) {
        super(level);
    }

    public LitBomb(Level level, int radius) {
        super(level);
        this.radius = radius;
    }

    public LitBomb(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
//        bomb = new Circle(5, Color.BLACK);
//        view.addNode(bomb);
        sprite = new SpriteView(getImage("sprite/prop/litbomb/0.png"),new Vec2d(-8,-8), 1,1);
        sprite.addState("5", getImage("sprite/prop/litbomb/skull0.png"), new Vec2d(-8,-8), 1, 1);
        sprite.addState("4", getImage("sprite/prop/litbomb/skull1.png"), new Vec2d(-8,-8), 1, 1);
        sprite.addState("3", getImage("sprite/prop/litbomb/skull0.png"), new Vec2d(-8,-8), 1, 1);
        sprite.addState("2", getImage("sprite/prop/litbomb/skull1.png"), new Vec2d(-8,-8), 1, 1);
        sprite.addState("1", getImage("sprite/prop/litbomb/skull2.png"), new Vec2d(-8,-8), 1, 1);
        sprite.addState("1", getImage("sprite/prop/litbomb/centre0.png"), new Vec2d(-8,-8), 1, 1);
        SpriteAnimation explosion = new SpriteAnimation(sprite, new Duration(500),new Vec2i(-8,-8), 1);
        explosion.addState(getImage("sprite/prop/litbomb/centre0.png"));
        explosion.addState(getImage("sprite/prop/litbomb/centre1.png"));
        explosion.addState(getImage("sprite/prop/litbomb/centre2.png"));
        explosion.addState(getImage("sprite/prop/litbomb/centre3.png"));

        explosion.alignToUp(1,1);
        explosion.alignToUp(1,2);
        explosion.alignToUp(1,3);

        explosion.alignToLeft(1,1);
        explosion.alignToLeft(1,2);
        explosion.alignToLeft(1,3);
        sprite.addAnime("Explosion", explosion);

        view.addNode(sprite);
    }


    @Override
    public void onTurnUpdate() {
        sprite.setState(fuseLength.toString());
//        sprite.playAnime("Explosion");
        fuseLength--;

//        int count = MAX_FUSE_LENGTH - fuseLength;
//        int redness = Math.min( (int)(((double)count)/MAX_FUSE_LENGTH * 255), 255);
//        bomb.setFill(Color.rgb(redness, 0, 0));

        if (fuseLength <= 0)
            onExplosion();
    }


    /**
     * Logic when the bomb explodes, killing everythings in the
     * plus shape around the bomb
     */
    public void onExplosion() {
        System.out.println("EXPLOSION");
        sprite.playAnime("Explosion");
        destroyEntity(pos);

        for (Vec2i dir : Vec2i.DIRECTIONS) {
            Vec2i target = new Vec2i(pos);
            for (int i = 1; i <= radius; i++) {
                target = target.add(dir);
                destroyEntity(target);
            }
        }

        onDestroyed();
    }


    /**
     * Destroying entities on a certain position
     * @param pos : position to destroy entities
     */
    public void destroyEntity(Vec2i pos) {
        Iterator<Entity> it = level.getEntitiesAt(pos);
        it.forEachRemaining(Entity::onExploded);
    }


    @Override
    public boolean isPassableFor(Entity entity) {
        return true;
    }

    @Override
    public boolean isPassableForProp(Prop prop) {
        return prop.isProjectile;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }
}
