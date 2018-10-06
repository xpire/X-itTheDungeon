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
        sprite = new SpriteView();
        sprite.addState("Face Up", getImage("idle/0.png"), new Vec2d(-12,-15), 1);
        sprite.addState("Face Down", getImage("idle/2.png"), new Vec2d(-12,-15), 1);
        sprite.addState("Face Left", getImage("idle/1.png"), new Vec2d(-11,-15), 1);
        sprite.addState("Face Right", getImage("idle/1.png"), new Vec2d(-11,-15), -1);


        SpriteAnimation swordLeft = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-11,-15),1);
        swordLeft.addState(getImage("idle/1.png"));
        swordLeft.addState(getImage("sword_left/0.png"));
        swordLeft.addState(getImage("sword_left/1.png"));
        swordLeft.addState(getImage("sword_left/2.png"));
        swordLeft.addState(getImage("sword_left/3.png"));
        swordLeft.addState(getImage("sword_left/4.png"));
        swordLeft.addState(getImage("sword_left/5.png"));
        swordLeft.addState(getImage("sword_left/6.png"));
        swordLeft.addState(getImage("sword_left/7.png"));
        swordLeft.addState(getImage("idle/1.png"));

        swordLeft.alignToLeft(2,1);
        swordLeft.alignToRight(2,2);
        swordLeft.alignToRight(2,3);
        swordLeft.alignToRight(2,4);
        swordLeft.alignToRight(2,5);
        swordLeft.alignToRight(2,6);
        swordLeft.alignToRight(2,7);
        swordLeft.alignToRight(2,8);
        swordLeft.alignToRight(2,9);


        swordLeft.alignToUp(1,1);
        swordLeft.alignToUp(1,2);
        swordLeft.alignToUp(1,3);
        swordLeft.alignToUp(1,4);
        swordLeft.alignToUp(1,5);
        swordLeft.alignToUp(1,6);
        swordLeft.alignToDown(1,7);
        swordLeft.alignToDown(1,8);
        swordLeft.alignToDown(1,9);



        sprite.addAnime("sword_left", swordLeft);

        SpriteAnimation swordRight = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-11,-15), -1);
        swordRight.addState(getImage("idle/1.png"));
        swordRight.addState(getImage("sword_left/0.png"));
        swordRight.addState(getImage("sword_left/1.png"));
        swordRight.addState(getImage("sword_left/2.png"));
        swordRight.addState(getImage("sword_left/3.png"));
        swordRight.addState(getImage("sword_left/4.png"));
        swordRight.addState(getImage("sword_left/5.png"));
        swordRight.addState(getImage("sword_left/6.png"));
        swordRight.addState(getImage("sword_left/7.png"));
        swordRight.addState(getImage("idle/1.png"));

        swordRight.alignToRight(2,1);
        swordRight.alignToLeft(2,2);
        swordRight.alignToLeft(2,3);
        swordRight.alignToLeft(2,4);
        swordRight.alignToLeft(2,5);
        swordRight.alignToLeft(2,6);
        swordRight.alignToLeft(2,7);
        swordRight.alignToLeft(2,8);
        swordRight.alignToLeft(2,9);

        swordRight.alignToUp(1,1);
        swordRight.alignToUp(1,2);
        swordRight.alignToUp(1,3);
        swordRight.alignToUp(1,4);
        swordRight.alignToUp(1,5);
        swordRight.alignToUp(1,6);
        swordRight.alignToDown(1,7);
        swordRight.alignToDown(1,8);
        swordRight.alignToDown(1,9);
        sprite.addAnime("sword_right", swordRight);
        
        SpriteAnimation swordUp = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);
        swordUp.addState(getImage("idle/0.png"));
        swordUp.addState(getImage("sword_up/0.png"));
        swordUp.addState(getImage("sword_up/1.png"));
        swordUp.addState(getImage("sword_up/2.png"));
        swordUp.addState(getImage("sword_up/3.png"));
        swordUp.addState(getImage("sword_up/4.png"));
        swordUp.addState(getImage("sword_up/5.png"));
        swordUp.addState(getImage("sword_up/6.png"));
        swordUp.addState(getImage("sword_up/7.png"));
        swordUp.addState(getImage("idle/0.png"));

        swordUp.alignToUp(2,1);
        swordUp.alignToUp(2,2);
        swordUp.alignToDown(2,3);
        swordUp.alignToDown(2,4);
        swordUp.alignToDown(2,5);
        swordUp.alignToDown(2,6);
        swordUp.alignToDown(2,7);
        swordUp.alignToDown(2,8);
        swordUp.alignToDown(2,9);

        swordUp.alignToRight(1,1);
        swordUp.alignToRight(1,2);
        swordUp.alignToRight(1,3);
        swordUp.alignToRight(1,4);
        swordUp.alignToRight(1,5);
        swordUp.alignToRight(1,6);
        swordUp.alignToLeft(1,7);
        swordUp.alignToLeft(1,8);
        swordUp.alignToLeft(1,9);
        sprite.addAnime("sword_up", swordUp);

        SpriteAnimation swordDown = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);
        swordDown.addState(getImage("idle/2.png"));
        swordDown.addState(getImage("sword_down/0.png"));
        swordDown.addState(getImage("sword_down/1.png"));
        swordDown.addState(getImage("sword_down/2.png"));
        swordDown.addState(getImage("sword_down/3.png"));
        swordDown.addState(getImage("sword_down/4.png"));
        swordDown.addState(getImage("sword_down/5.png"));
        swordDown.addState(getImage("sword_down/6.png"));
        swordDown.addState(getImage("sword_down/7.png"));
        swordDown.addState(getImage("idle/2.png"));

        swordDown.alignToDown(2,1);
        swordDown.alignToDown(2,2);
        swordDown.alignToUp(2,3);
        swordDown.alignToUp(2,4);
        swordDown.alignToUp(2,5);
        swordDown.alignToUp(2,6);
        swordDown.alignToUp(2,7);
        swordDown.alignToUp(2,8);
        swordDown.alignToUp(2,9);

        swordDown.alignToLeft(1,1);
        swordDown.alignToLeft(1,2);
        swordDown.alignToLeft(1,3);
        swordDown.alignToLeft(1,4);
        swordDown.alignToLeft(1,5);
        swordDown.alignToLeft(1,6);
        swordDown.alignToRight(1,7);
        swordDown.alignToRight(1,8);
        swordDown.alignToRight(1,9);

        sprite.addAnime("sword_down", swordDown);

//        SpriteAnimation bowUp = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);

        pane.getChildren().add(sprite);
        faceDown();

        view.addNode(pane);
    }

//    public SpriteView setInputStream(String path) {
//        FileInputStream inputStream = null;
//        StringBuilder sb = new StringBuilder();
//        sb.append("./src/asset/");
//        sb.append(path);
//        try {
//            inputStream = new FileInputStream(sb.toString());
//            Image image = new Image(inputStream);
//            sprite = new SpriteView(image);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return sprite;
//    }

    public Image getImage(String path) {
        FileInputStream inputStream = null;
        StringBuilder sb = new StringBuilder();
        sb.append("./src/asset/").append(path);
        try {
            inputStream = new FileInputStream(sb.toString());
            return new Image(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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

//        sprite.playAnime("sword_up");
//        sprite.setX(0);
//        sprite.setY(0);
//        sprite.setScaleX(1);
    }

    public void faceDown() {
        setDirection(Vec2i.SOUTH);
        sprite.setState("Face Down");

//        sprite.playAnime("sword_down");
//        sprite.setX(0);
//        sprite.setY(0);
//        sprite.setScaleX(1);
    }

    public void faceLeft() {
        setDirection(Vec2i.WEST);
        sprite.setState("Face Left");
//        sprite.setScaleX(1);


//        animation.alignToRight(1);
//        animation.play();
//        sprite.playAnime("sword_left");
    }
    public void faceRight() {
        setDirection(Vec2i.EAST);
        sprite.setState("Face Right");
//        sprite.setScaleX(-1);

//        sprite.playAnime("sword_right");

//        animation.play();
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
        System.out.println(direction);
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
