package main.entities.prop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.entities.Entity;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteAnimation;
import main.sprite.SpriteView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class describing the Lit Bomb entity
 */
public class LitBomb extends Prop{

    private Circle bomb;

    private final int MAX_FUSE_LENGTH = 5;
    private Integer fuseLength = MAX_FUSE_LENGTH;
    private int radius = 1;
    private Vec2i direction = new Vec2i(0,0);
    private EventHandler<ActionEvent> afterFinish =  e -> {
        this.destroyEntity(pos);
        destroy();
    };

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
        sprite.addState("0", getImage("sprite/prop/litbomb/centre0.png"), new Vec2d(-8,-8), 1, 1);
        sprite.addAnime("Centre", generateAnimation("centre", 4, new Vec2d(0,0), sprite));
        sprite.addAnime("Middle", generateAnimation("middle", 4, new Vec2d(0,0), sprite));
        sprite.addAnime("End", generateAnimation("right", 4, new Vec2d(0,0), sprite));
        view.addNode(sprite);

    }


    @Override
    public void onTurnUpdate() {
        sprite.setState(fuseLength.toString());
        fuseLength--;

        if (fuseLength < 0)
            onExplosion();
        else
            soundManager.playSoundEffect("Click");
    }


    /**
     * Logic when the bomb explodes, killing everythings in the
     * plus shape around the bomb
     */
    private void onExplosion() {
        System.out.println("EXPLOSION");
        soundManager.playSoundEffect("Explosion");
        ArrayList<Vec2i> targets = new ArrayList<>();

        for (Vec2i dir : Vec2i.DIRECTIONS) {
            Vec2i target = new Vec2i(pos);
            for (int i = 1; i <= radius; i++) {
                target = target.add(dir);
                if (!level.canReplaceProp(target, this)) {
                    break;
                }
                targets.add(target);

                SpriteView temp = new SpriteView(getImage("sprite/prop/litbomb/centre0.png"),new Vec2d(-8 + dir.getX()*i*30,-8 + dir.getY()*i*30), 1.875,1.875);
//                temp.setX(sprite.getX() + dir.getX()*i*30);
//                temp.setY(sprite.getY() + dir.getY()*i*30);
                if (i == radius) {
                    //render end
                    temp.addAnime("Default", generateAnimation("right", 4,   new Vec2d(dir.getX()*i*30,dir.getY()*i*30), temp));
                } else {
                    //render middle
                    temp.addAnime("Default", generateAnimation("middle", 4,  new Vec2d(dir.getX()*i*30,dir.getY()*i*30), temp));
                }
                if (dir.equals(Vec2i.WEST)) {
                    temp.setRotate(180);
                } else if (dir.equals(Vec2i.NORTH)) {
                    temp.setRotate(270);
                } else if (dir.equals(Vec2i.SOUTH)) {
                    temp.setRotate(90);
                } else if (dir.equals(Vec2i.EAST)) {
                    temp.setRotate(0);
                }
                view.addNode(temp);
                final Vec2i fTarget = target;
                temp.playAnime("Default", e -> {
                    System.out.println("ANIMATION ENDED!");
                    destroyEntity(fTarget);
                });
                destroyEntity(target);
            }
        }
//        System.out.printf("%d\n", targets.size());
//        HashMap<String, SpriteView> flareMapping = new HashMap<>();
        for (int j = 0; j < targets.size(); j++) {
            System.out.println(targets.get(j));
        }

        sprite.playAnime("Centre", afterFinish);
    }

    private SpriteAnimation generateAnimation(String name, int number, Vec2d offset, SpriteView sp) {
        System.out.println(offset);
        SpriteAnimation end = new SpriteAnimation(sp, new Duration(500), new Vec2i(-8 + (int) offset.getX(), -8 + (int)offset.getY()), 1.875, 1.875);
        StringBuilder sb;// = new StringBuilder("sprite/prop/litbomb/");
//        sb.append(name);
        for (int i = 0; i < number; i++) {
            sb = new StringBuilder("sprite/prop/litbomb/").append(name);
            end.addState(getImage(sb.append(i).append(".png").toString()));
        }
        end.alignOffset(offset);
        return end;
    }


    /**
     * D6estroying entities on a certain position
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
