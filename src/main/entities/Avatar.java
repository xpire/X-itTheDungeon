package main.entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import main.Level;
import main.entities.enemies.Enemy;
import main.entities.pickup.*;
import main.entities.prop.FlyingArrow;
import main.entities.prop.LitBomb;
import main.entities.terrain.Door;
import main.events.AvatarEvent;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteAnimation;
import main.sprite.SpriteView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Avatar extends Entity {

    private Key key;
    private Sword sword;
    private InvincibilityPotion ragePotion;

    private IntegerProperty numArrows;
    private IntegerProperty numBombs;
    private IntegerProperty numTreasures;

    private BooleanProperty isHovering;
    private BooleanProperty isRaged;

    private Vec2i direction;
    private ArrayList<Integer> pastMoves;

    private Line swordView;
    private Circle hoverView;
    private Circle rageView;

    private Runnable nextAction;

    // THINGS THAT CAN BE RUN AFTER ON_CREATED
    {
        symbol = 'P';

        key             = null;
        sword           = null;
        ragePotion      = null;

        numArrows       = new SimpleIntegerProperty(0);
        numBombs        = new SimpleIntegerProperty(0);
        numTreasures    = new SimpleIntegerProperty(0);

        direction       = Vec2i.SOUTH;
        pastMoves       = new ArrayList<>();

        nextAction      = null;
    }


    private SpriteView sprite;

    /**
     * Basic constructor
     * @param level current level
     */
    public Avatar(Level level) {
        super(level);
    }


    public Avatar(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated() {
//        Circle circle = new Circle(10, Color.AQUA);
//        view.addNode(circle);
//
        hoverView = new Circle(8, Color.LIMEGREEN);
        rageView  = new Circle(4, Color.TOMATO);
        swordView = new Line(-10, 0, 10, 0);
//
//        view.addNode(hoverView);
//        view.addNode(rageView);
//        view.addNode(swordView);

        isHovering = new SimpleBooleanProperty(false);
        isRaged = new SimpleBooleanProperty(false);

        hoverView.visibleProperty().bind(isHovering);
        rageView.visibleProperty().bind(isRaged);
        swordView.setVisible(false);



        Pane pane = new Pane();
//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //Passing FileInputStream object as a parameter
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./src/asset/avatar.png");
            Image image = new Image(inputStream);
            sprite = new SpriteView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sprite.addState("Face Up",
                new Rectangle2D(0, 20, 24, 30), new Vec2d(-12, -15));

        sprite.addState("Face Down",
                new Rectangle2D(2, 114, 24, 30), new Vec2d(-12, -15));

        sprite.addState("Face Left",
                new Rectangle2D(2, 66, 22, 30), new Vec2d(-11, -15));

        sprite.addState("Face Right",
                new Rectangle2D(2, 66, 22, 30), new Vec2d(-11, -15));

        pane.getChildren().add(sprite);
        faceDown();

        view.addNode(pane);
    }

    @Override
    public void onDestroyed() {
        level.removeAvatar();
        level.postEvent(new AvatarEvent(AvatarEvent.AVATAR_DIED));
    }

    @Override
    public void onExploded() {
        if (!isRaged.get()) {
            onDestroyed();
        }
    }


    /**
     * Update the grid and world position of the Avatar
     */
    public void update() {
        if (nextAction == null) return;
        nextAction.run();
        nextAction = null;
    }

    public void setNextAction(Runnable action) {
        nextAction = action;
    }

    private void endTurn() {
        level.postEvent(new AvatarEvent(AvatarEvent.AVATAR_TURN_ENDED));
    }


    /*
        MOVE AVATAR
     */
    public void moveUp() {
        tryMove(Vec2i.NORTH);
        faceUp();
    }
    public void moveDown() {
        tryMove(Vec2i.SOUTH);
        faceDown();
    }
    public void moveLeft() {
        tryMove(Vec2i.WEST);
        faceLeft();
    }
    public void moveRight() {
        tryMove(Vec2i.EAST);
        faceRight();
    }

    private void tryMove(Vec2i dir) {

        if (!dir.isDirection()) return;

        Vec2i newPos = pos.add(dir);
        if (level.isPassableForAvatar(newPos, this)) {
            move(dir);
        }
        else if(level.onPushByAvatar(newPos, this)) {
            move(dir);
        }
    }

    private void move(Vec2i dir) {
        level.moveAvatar(pos.add(dir));

        if (dir.equals(Vec2i.NORTH))
            pastMoves.add(0);
        else if (dir.equals(Vec2i.SOUTH))
            pastMoves.add(1);
        else if (dir.equals(Vec2i.WEST))
            pastMoves.add(2);
        else if (dir.equals(Vec2i.EAST))
            pastMoves.add(3);

        endTurn();
    }


    /*
        CHANGE AVATAR DIRECTION
     */
    public void faceUp() {
        setDirection(Vec2i.NORTH);
        sprite.setState("Face Up");
        sprite.setScaleX(1);
    }

    public void faceDown() {
        setDirection(Vec2i.SOUTH);
        sprite.setState("Face Down");
        sprite.setScaleX(1);
    }

    public void faceLeft() {
        setDirection(Vec2i.WEST);
//        sprite.setState("Face Left");
        sprite.setScaleX(1);

        SpriteAnimation animation = new SpriteAnimation(sprite, new Duration(300));
        sprite.setX(-11);
        sprite.setY(-15);
        animation.addState(new Rectangle2D(2, 66, 22, 30));
        animation.addState(new Rectangle2D(182, 66, 23, 30));
        animation.addState(new Rectangle2D(216, 66, 27, 30));
        animation.addState(new Rectangle2D(244, 66, 36, 30));
        animation.addState(new Rectangle2D(282, 66, 36, 30));
        animation.addState(new Rectangle2D(324, 66, 32, 30));
        animation.addState(new Rectangle2D(369, 66, 27, 38));
        animation.addState(new Rectangle2D(415, 66, 22, 38));
        animation.addState(new Rectangle2D(2, 66, 22, 30));
        animation.alignToRight(2);
        animation.play();
    }
    public void faceRight() {
        setDirection(Vec2i.EAST);
//        sprite.setState("Face Right");
        sprite.setScaleX(-1);

        SpriteAnimation animation = new SpriteAnimation(sprite, new Duration(300));
        sprite.setX(-11);
        sprite.setY(-15);
        animation.addState(new Rectangle2D(2, 66, 22, 30));
        animation.addState(new Rectangle2D(182, 66, 23, 30));
        animation.addState(new Rectangle2D(216, 66, 27, 30));
        animation.addState(new Rectangle2D(244, 66, 36, 30));
        animation.addState(new Rectangle2D(282, 66, 36, 30));
        animation.addState(new Rectangle2D(324, 66, 32, 30));
        animation.addState(new Rectangle2D(369, 66, 27, 38));
        animation.addState(new Rectangle2D(415, 66, 22, 38));
        animation.addState(new Rectangle2D(2, 66, 22, 30));
        animation.alignToLeft(2);
        animation.play();
    }

    private void setDirection(Vec2i newDir) {
        if (!newDir.isDirection()) return;
        direction = newDir;
    }




    /**
     * Logic when the Avatars turn ends
     * reduce potion durations
     */
    public void onRoundEnd() {
        if (ragePotion == null) return;

        ragePotion.reduceDuration();

        if (ragePotion.hasExpired())
            onRageEnd();
    }


    /**
     * The player requested to swing sword
     */
    public void swingSword() {
        // cannot swing if has no sword
        if (sword == null) return;

//        SpriteAnimation animation = new SpriteAnimation(sprite, new Duration(1000), 5, 1,
//                10, 30, 25, 30);
//        animation.play();

        // kill the entity in the avatar's direction
        Vec2i target = pos.add(direction);
        if (level.isValidGridPos(target) && level.hasEnemy(target)) {

            // Kill the enemy
            level.getEnemy(target).onDestroyed();
            sword.reduceDurability();

            // check durability and destroy
            if (sword.isBroken())
                onSwordUnequipped();

            endTurn();
        }
    }

    /**
     * when the player picks up a sword
     * @param s
     */
    public void onSwordEquipped(Sword s) {
        sword = s;
        swordView.setVisible(true);
    }

    /**
     * when the player loses their sword
     */
    public void onSwordUnequipped() {
        sword = null;
        swordView.setVisible(false);
    }


    /**
     * The player requested to shoot an arrow
     */
    public void shootArrow() {
        // cannot shoot if no arrow
        if (numArrows.get() <= 0) return;

        FlyingArrow arrow = new FlyingArrow(level);
        Vec2i arrowPos = new Vec2i(pos).add(direction);

        // kill first enemy in avatar's direction, if the enemy exists and is reachable
        while(level.isValidGridPos(arrowPos)) {

            // enemy hit
            if (level.hasEnemy(arrowPos)) {
                level.getEnemy(arrowPos).onDestroyed();
            }
            // non-passable entity hit
            else if (!level.isPassableForProp(arrowPos, arrow)) {
                break;
            }

            arrowPos = arrowPos.add(direction);
        }

        // -1 arrow
        numArrows.set(numArrows.get() - 1);
        endTurn();
    }


    /**
     * Player places a bomb
     */
    public void placeBomb() {
        if (numBombs.get() <= 0) return;

        LitBomb bomb = new LitBomb(level);

        if (level.canPlaceProp(pos, bomb)) {
            level.addProp(pos, bomb);
            numBombs.set(numBombs.get() - 1);
            endTurn();
        }
    }

    /**
     * check if Avatar has key
     * @return true if has key
     */
    public boolean hasKey() {
        return (key != null);
    }

    /**
     * check if Avatar has sword
     * @return true if has sword
     */
    public boolean hasSword() {
        return (sword != null);
    }

    /**
     * player drops their key
     */
    public void dropKey() {
        if (key == null) return;

        if (level.canPlacePickup(pos, key)) {
            level.addPickup(pos, key);
            key = null;
            endTurn();
        }
    }


    /**
     * check if the Avatar's key matches a door
     * @param door door being checked
     * @return true if key matches
     */
    public boolean hasKeyFor(Door door) {
        return key != null && key.isMatchingDoor(door);
    }

    /**
     * when the Avatar uses their key
     */
    public void useKey() {
        key = null;
    }



    /*
        Pickup functions
     */

    /**
     * Logic when the Avatar picks up a key
     * @param k key being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpKey(Key k) {
        if (key != null) return false;

        key = k;
        return true;
    }

    /**
     * Logic when the Avatar picks up a sword
     * @param s sword being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpSword(Sword s) {
        if (sword != null) return false;

        onSwordEquipped(s);
        return true;
    }


    /**
     * Logic when the Avatar picks up a Hover pot
     * @param p hover pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpHoverPotion(HoverPotion p) {
        onHoverBegin();
        return true;
    }

    /**
     * Logic when the Avatar picks up a Invinc pot
     * @param p invinc pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpInvincibilityPotion(InvincibilityPotion p) {
        onRageBegin(p);
        return true;
    }

    /**
     * Logic when the Avatar picks up a Arrow
     * @param arrow arrow being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpArrow(Arrow arrow) {
        numArrows.setValue(numArrows.get() + 1);
        return true;
    }

    /**
     * Logic when the Avatar picks up a Bomb
     * @param bomb bomb being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpBomb(Bomb bomb) {
        numBombs.setValue(numBombs.get() + 1);
        return true;
    }

    /**
     * Logic when the Avatar picks up a treasure
     * @param treasure treasure being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpTreasure(Treasure treasure) {
        numTreasures.setValue(numTreasures.get() + 1);
        return true;
    }


    /**
     * logic when hover pot is picked up
     */
    public void onHoverBegin() {
        isHovering.set(true);
    }

    /**
     * logic when hover pot ends
     */
    public void onHoverEnd() {
        isHovering.set(false);
    }

    /**
     * logic when invinc pot is picked up
     * @param p invinc pot picked up
     */
    public void onRageBegin(InvincibilityPotion p) {
        ragePotion = p;
        isRaged.set(true);
    }

    /**
     * logic when invinc pot ends
     */
    public void onRageEnd() {
        isRaged.set(false);
    }


    /**
     * Getter for curr # arrows
     * @return curr # arrows
     */
    public IntegerProperty getNumArrowsProperty() {
        return numArrows;
    }

    /**
     * Getter for curr # bomb
     * @return curr # bomb
     */
    public IntegerProperty getNumBombsProperty() {
        return numBombs;
    }

    /**
     * Getter for curr # treasure
     * @return curr # treasure
     */
    public IntegerProperty getNumTreasuresProperty() {
        return numTreasures;
    }


    /**
     * Check if hovering
     * @return true if hovering
     */
    public boolean isHovering() {
        return isHovering.get();
    }

    /**
     * check if enraged
     * @return true if enraged
     */
    public boolean isRaged() {
        return isRaged.get();
    }

    /**
     * Getter for Avatars past moves
     * @return
     */
    public ArrayList<Integer> getPastMoves() {
        return new ArrayList<>(pastMoves); // TODO unmodifiable?
    }




    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean isPassableForEnemy(Enemy enemy) {return true;}

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public void onEnterByEnemy(Enemy enemy) {
        if (isRaged())
            enemy.onDestroyed();
        else
            onDestroyed();
    }
}
