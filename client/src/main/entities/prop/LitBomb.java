package main.entities.prop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.Level;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteAnimation;
import main.sprite.SpriteView;

import java.util.Iterator;

/**
 * Class describing the Lit Bomb entity
 */
public class LitBomb extends Prop{

<<<<<<< HEAD
    private final int MAX_FUSE_LENGTH = 3;
=======
    private final int MAX_FUSE_LENGTH = 5;
>>>>>>> Entity package javadoc done
    private Integer fuseLength = MAX_FUSE_LENGTH;
    private int radius = 1;
    private EventHandler<ActionEvent> afterFinish =  e -> {
        this.destroyEntity(pos);
        destroy();
    };

    /**
     * Basic Constructor
     * @param level : level the lit bomb belongs to
     * @param radius : radius of the bomb's explosion
     */
    public LitBomb(Level level, int radius) {
        super(level);
        this.radius = radius;
    }

    @Override
    public void onCreated(){
        super.onCreated();
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
    public void onExplosion() {
        //Play explosion SFX
        soundManager.playSoundEffect("Explosion");

        //Check all squares within bomb radius
        for (Vec2i dir : Vec2i.DIRECTIONS) {
            Vec2i target = new Vec2i(pos);
            for (int i = 1; i <= radius; i++) {
                target = target.add(dir);
                if (!level.canReplaceProp(target, this)) {
                    //hit a wall
                    break;
                }

                //Generate a sprite for animation
                SpriteView temp = new SpriteView(
                        getImage("sprite/prop/litbomb/centre0.png"),
                        new Vec2d(-8 + dir.getX()*i*30,-8 + dir.getY()*i*30),
                        1.875,
                        1.875
                );

                //generate Animation
                if (i == radius) {
                    //render end
                    temp.addAnime(
                            "Default",
                            generateAnimation(
                                    "right",
                                    4,
                                    new Vec2d(dir.getX()*i*30,dir.getY()*i*30),
                                    temp
                            )
                    );
                } else {
                    //render middle
                    temp.addAnime(
                            "Default",
                            generateAnimation(
                                    "middle",
                                    4,
                                    new Vec2d(dir.getX()*i*30,dir.getY()*i*30),
                                    temp
                            )
                    );
                }

                //Rotation
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
                temp.playAnime("Default", e -> {});

                //destroy entity
                destroyEntity(target);
            }
        }
        sprite.playAnime("Centre",afterFinish);
    }

    /**
     * Logic for generating animations by importing each image
     * @param name : name of the animation to render
     * @param number : number of frames in animation
     * @param offset : offset to apply to the animation
     * @param sp : SpriteView this SpriteAnimation comes from
     * @return SpriteAnimation
     */
    public SpriteAnimation generateAnimation(String name, int number, Vec2d offset, SpriteView sp) {
        System.out.println(offset);
        SpriteAnimation end = new SpriteAnimation(
                sp,
                new Duration(500),
                new Vec2i(-8 + (int) offset.getX(), -8 + (int)offset.getY()),
                1.875,
                1.875
        );
        StringBuilder sb;
        for (Integer i = 0; i < number; i++) {
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
