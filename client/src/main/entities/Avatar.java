package main.entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import main.Level;
import main.PlayMode;
import main.entities.enemies.Enemy;
import main.entities.pickup.*;
import main.entities.prop.FlyingArrow;
import main.entities.prop.LitBomb;
import main.entities.terrain.Door;
import main.entities.terrain.Pit;
import main.events.AvatarDeathEvent;
import main.events.AvatarEvent;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sound.SoundManager;
import main.sprite.SpriteAnimation;
import main.sprite.SpriteView;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Avatar extends Entity {

    /*
    - refactor: exposing property is unsafe
    - restart button should work
    - image display also
    - buff?
     */

    private Key key;
    private Sword sword;
    private InvincibilityPotion ragePotion;

    private IntegerProperty swordDurability;
    private BooleanProperty hasKeyProperty;
    private IntegerProperty numArrows;
    private IntegerProperty numBombs;
    private IntegerProperty numTreasures;

    private BooleanProperty isHovering;
    private BooleanProperty isRaged;
    private IntegerProperty bombRadius;

    private Vec2i direction;
    private ArrayList<Integer> pastMoves;

    private Line swordView;
    private Circle hoverView;
    private Circle rageView;
    private SoundManager soundManager;

    private EventHandler<ActionEvent> afterFinish;
    private EventHandler<ActionEvent> doNothing;

    private Runnable nextAction;

    // THINGS THAT CAN BE RUN AFTER ON_CREATED
    {
        symbol = 'P';

        key             = null;
        sword           = null;
        ragePotion      = null;

        hasKeyProperty  = new SimpleBooleanProperty(false);
        swordDurability = new SimpleIntegerProperty(0);
        numArrows       = new SimpleIntegerProperty(0);
        numBombs        = new SimpleIntegerProperty(0);
        numTreasures    = new SimpleIntegerProperty(0);

        direction       = new Vec2i(1,0);
        pastMoves       = new ArrayList<>();

        nextAction      = null;
    }


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

        hoverView   = new Circle(8, Color.LIMEGREEN);
        rageView    = new Circle(4, Color.TOMATO);
        swordView   = new Line(-10, 0, 10, 0);

//        view.addNode(hoverView);
//        view.addNode(rageView);
//        view.addNode(swordView);

        isHovering  = new SimpleBooleanProperty(false);
        isRaged     = new SimpleBooleanProperty(false);
        bombRadius  = new SimpleIntegerProperty(1);


        hoverView.visibleProperty().bind(isHovering);
        rageView.visibleProperty().bind(isRaged);
        swordView.setVisible(false);

        soundManager = SoundManager.getInstance(5);

        sprite = new SpriteView(getImage("sprite/idle/0.png"), new Vec2d(-12,-15), 1,1);
        sprite.addState("Face Up", getImage("sprite/idle/0.png"), new Vec2d(-12,-15), 1,1);
        sprite.addState("Face Down", getImage("sprite/idle/2.png"), new Vec2d(-12,-15), 1,1);
        sprite.addState("Face Left", getImage("sprite/idle/1.png"), new Vec2d(-11,-15), 1,1);
        sprite.addState("Face Right", getImage("sprite/idle/1.png"), new Vec2d(-11,-15), -1,1);


        afterFinish =  e -> {
            PlayMode.input.startListening();
            System.out.println("START INPUT");
        };

        doNothing = e -> {};

        SpriteAnimation swordLeft = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-11,-15),1);
        swordLeft.addState(getImage("sprite/idle/1.png"));
        swordLeft.addState(getImage("sprite/sword_left/0.png"));
        swordLeft.addState(getImage("sprite/sword_left/1.png"));
        swordLeft.addState(getImage("sprite/sword_left/2.png"));
        swordLeft.addState(getImage("sprite/sword_left/3.png"));
        swordLeft.addState(getImage("sprite/sword_left/4.png"));
        swordLeft.addState(getImage("sprite/sword_left/5.png"));
        swordLeft.addState(getImage("sprite/sword_left/6.png"));
        swordLeft.addState(getImage("sprite/sword_left/7.png"));
        swordLeft.addState(getImage("sprite/idle/1.png"));

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



        sprite.addAnime("Sword Left", swordLeft);

        SpriteAnimation swordRight = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-11,-15), -1);
        swordRight.addState(getImage("sprite/idle/1.png"));
        swordRight.addState(getImage("sprite/sword_left/0.png"));
        swordRight.addState(getImage("sprite/sword_left/1.png"));
        swordRight.addState(getImage("sprite/sword_left/2.png"));
        swordRight.addState(getImage("sprite/sword_left/3.png"));
        swordRight.addState(getImage("sprite/sword_left/4.png"));
        swordRight.addState(getImage("sprite/sword_left/5.png"));
        swordRight.addState(getImage("sprite/sword_left/6.png"));
        swordRight.addState(getImage("sprite/sword_left/7.png"));
        swordRight.addState(getImage("sprite/idle/1.png"));

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
        sprite.addAnime("Sword Right", swordRight);

        SpriteAnimation swordUp = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);
        swordUp.addState(getImage("sprite/idle/0.png"));
        swordUp.addState(getImage("sprite/sword_up/0.png"));
        swordUp.addState(getImage("sprite/sword_up/1.png"));
        swordUp.addState(getImage("sprite/sword_up/2.png"));
        swordUp.addState(getImage("sprite/sword_up/3.png"));
        swordUp.addState(getImage("sprite/sword_up/4.png"));
        swordUp.addState(getImage("sprite/sword_up/5.png"));
        swordUp.addState(getImage("sprite/sword_up/6.png"));
        swordUp.addState(getImage("sprite/sword_up/7.png"));
        swordUp.addState(getImage("sprite/idle/0.png"));

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
        sprite.addAnime("Sword Up", swordUp);

        SpriteAnimation swordDown = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);
        swordDown.addState(getImage("sprite/idle/2.png"));
        swordDown.addState(getImage("sprite/sword_down/0.png"));
        swordDown.addState(getImage("sprite/sword_down/1.png"));
        swordDown.addState(getImage("sprite/sword_down/2.png"));
        swordDown.addState(getImage("sprite/sword_down/3.png"));
        swordDown.addState(getImage("sprite/sword_down/4.png"));
        swordDown.addState(getImage("sprite/sword_down/5.png"));
        swordDown.addState(getImage("sprite/sword_down/6.png"));
        swordDown.addState(getImage("sprite/sword_down/7.png"));
        swordDown.addState(getImage("sprite/idle/2.png"));

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

        sprite.addAnime("Sword Down", swordDown);

        SpriteAnimation bowUp = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);
        bowUp.addState(getImage("sprite/idle/0.png"));
        bowUp.addState(getImage("sprite/bow_up/0.png"));
        bowUp.addState(getImage("sprite/bow_up/1.png"));
        bowUp.addState(getImage("sprite/bow_up/2.png"));
        bowUp.addState(getImage("sprite/bow_up/3.png"));
        bowUp.addState(getImage("sprite/bow_up/4.png"));
        bowUp.addState(getImage("sprite/bow_up/5.png"));
        bowUp.addState(getImage("sprite/idle/0.png"));

        bowUp.alignToDown(1,1);
        bowUp.alignToDown(1,2);
        bowUp.alignToDown(1,3);
        bowUp.alignToDown(1,4);
        bowUp.alignToDown(1,5);
        bowUp.alignToDown(1,6);
        bowUp.alignToDown(1,7);

        bowUp.alignToRight(1,1);
        bowUp.alignToRight(1,2);
        bowUp.alignToRight(1,3);
        bowUp.alignToRight(1,4);
        bowUp.alignToRight(1,5);
        bowUp.alignToRight(1,6);
        bowUp.alignToRight(1,7);

        sprite.addAnime("Bow Up", bowUp);

        SpriteAnimation bowDown = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);
        bowDown.addState(getImage("sprite/idle/2.png"));
        bowDown.addState(getImage("sprite/bow_down/0.png"));
        bowDown.addState(getImage("sprite/bow_down/1.png"));
        bowDown.addState(getImage("sprite/bow_down/2.png"));
        bowDown.addState(getImage("sprite/bow_down/3.png"));
        bowDown.addState(getImage("sprite/bow_down/4.png"));
        bowDown.addState(getImage("sprite/bow_down/5.png"));
        bowDown.addState(getImage("sprite/idle/2.png"));

        bowDown.alignToUp(1,1);
        bowDown.alignToUp(1,2);
        bowDown.alignToUp(1,3);
        bowDown.alignToUp(1,4);
        bowDown.alignToUp(1,5);
        bowDown.alignToUp(1,6);
        bowDown.alignToUp(1,7);

        bowDown.alignToRight(1,1);
        bowDown.alignToRight(1,2);
        bowDown.alignToRight(1,3);
        bowDown.alignToRight(1,4);
        bowDown.alignToRight(1,5);
        bowDown.alignToRight(1,6);
        bowDown.alignToRight(1,7);
        sprite.addAnime("Bow Down", bowDown);

        SpriteAnimation bowLeft = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), 1);
        bowLeft.addState(getImage("sprite/idle/1.png"));
        bowLeft.addState(getImage("sprite/bow_left/0.png"));
        bowLeft.addState(getImage("sprite/bow_left/1.png"));
        bowLeft.addState(getImage("sprite/bow_left/2.png"));
        bowLeft.addState(getImage("sprite/bow_left/3.png"));
        bowLeft.addState(getImage("sprite/bow_left/4.png"));
        bowLeft.addState(getImage("sprite/bow_left/5.png"));
        bowLeft.addState(getImage("sprite/idle/1.png"));

        bowLeft.alignToUp(1,1);
        bowLeft.alignToUp(1,2);
        bowLeft.alignToUp(1,3);
        bowLeft.alignToUp(1,4);
        bowLeft.alignToUp(1,5);
        bowLeft.alignToUp(1,6);
        bowLeft.alignToUp(1,7);

//        bowLeft.alignToLeft(1,1);
//        bowLeft.alignToLeft(1,2);
        sprite.addAnime("Bow Left", bowLeft);

        SpriteAnimation bowRight = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-12,-15), -1);
        bowRight.addState(getImage("sprite/idle/1.png"));
        bowRight.addState(getImage("sprite/bow_left/0.png"));
        bowRight.addState(getImage("sprite/bow_left/1.png"));
        bowRight.addState(getImage("sprite/bow_left/2.png"));
        bowRight.addState(getImage("sprite/bow_left/3.png"));
        bowRight.addState(getImage("sprite/bow_left/4.png"));
        bowRight.addState(getImage("sprite/bow_left/5.png"));
        bowRight.addState(getImage("sprite/idle/1.png"));

        bowRight.alignToUp(1,1);
        bowRight.alignToUp(1,2);
        bowRight.alignToUp(1,3);
        bowRight.alignToUp(1,4);
        bowRight.alignToUp(1,5);
        bowRight.alignToUp(1,6);
        bowRight.alignToUp(1,7);

//        bowRight.alignToLeft(1,1);
//        bowRight.alignToLeft(1,2);
        sprite.addAnime("Bow Right", bowRight);

        faceDown();

        view.addNode(sprite);
    }


    @Override
    public void onDestroyed() {
        level.removeAvatar();
    }

    public void onThreatenedByPit(Pit pit) {
        if (!isHovering()) {
            level.postEvent(AvatarDeathEvent.deathByFalling());
            soundManager.playSoundEffect("Falling");
            soundManager.playSoundEffect("Death");
            onDestroyed();
        }
    }

    public void onThreatenedByBomb(Bomb bomb) {
        if (isRaged()) return;

        level.postEvent(AvatarDeathEvent.deathByExplosion());
        soundManager.playSoundEffect("Death");
        onDestroyed();
    }

    public void onThreatenedByEnemy(Enemy enemy) {
        if (isRaged()) {
            enemy.onDestroyed();
        }
        else {
            level.postEvent(AvatarDeathEvent.deathByAttack());
            soundManager.playSoundEffect("Death");
            onDestroyed();
        }
    }


    @Override
    public void onExploded() {
        onThreatenedByBomb(null); // TODO
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
        faceUp();
        tryMove(Vec2i.NORTH);
    }
    public void moveDown() {
        faceDown();
        tryMove(Vec2i.SOUTH);
    }
    public void moveLeft() {
        faceLeft();
        tryMove(Vec2i.WEST);
    }
    public void moveRight() {
        faceRight();
        tryMove(Vec2i.EAST);
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
        sprite.setState("Face Up");
        setDirection(Vec2i.NORTH);
    }
    public void faceDown() {
        sprite.setState("Face Down");
        setDirection(Vec2i.SOUTH);
    }
    public void faceLeft() {
        sprite.setState("Face Left");
        setDirection(Vec2i.WEST);
    }
    public void faceRight() {
        sprite.setState("Face Right");
        setDirection(Vec2i.EAST);
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
            isRaged.set(false);
    }


    /**
     * The player requested to swing sword
     */
    public void swingSword() {
        // cannot swing if has no sword
        if (sword == null) return;

        //Animation
        System.out.println("SWORD SWING!");
        sprite.playAnimation("Sword", direction, doNothing);
        soundManager.playSoundEffect("Puff");

        // kill the entity in the avatar's direction
        Vec2i target = pos.add(direction);
        if (level.isValidGridPos(target) && level.hasEnemy(target)) {

            // Kill the enemy
            level.getEnemy(target).onDestroyed();
            sword.reduceDurability();
            swordDurability.set(sword.getDurability());
            soundManager.playSoundEffect("Hit");


            // check durability and destroy
            if (sword.isBroken()) {
                sword = null;
                swordView.setVisible(false);
                soundManager.playSoundEffect("Shatter");
            }

            endTurn();
        }
    }

    /**
     * The player requested to shoot an arrow
     */
    public void shootArrow() {
        // cannot shoot if no arrow
        if (numArrows.get() <= 0) return;

        /* TODO: once shot, it should control its own killing logic */

        soundManager.playSoundEffect("Arrow");
        Vec2i arrowPos = new Vec2i(pos).add(direction);
        FlyingArrow arrow = new FlyingArrow(level, arrowPos, pos);

        //Animation
        sprite.playAnimation("Bow", direction, doNothing);

        // kill first enemy in avatar's direction, if the enemy exists and is reachable
        while(level.isValidGridPos(arrowPos)) {

            // enemy hit
            if (level.hasEnemy(arrowPos)) {
                level.getEnemy(arrowPos).onDestroyed();
                break;
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

        soundManager.playSoundEffect("Bomb");
        LitBomb bomb = new LitBomb(level, bombRadius.get());

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
        return key != null;
    }

    /**
     * check if Avatar has sword
     * @return true if has sword
     */
    public boolean hasSword() {
        return sword != null;
    }

    /**
     * player drops their key
     */
    public void dropKey() {
        if (key == null) return;
        soundManager.playSoundEffect("Click");

        if (level.canPlacePickup(pos, key)) {
            level.addPickup(pos, key);
            key = null;
            hasKeyProperty.set(false);
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
        hasKeyProperty.set(false);
        soundManager.playSoundEffect("Unlock");
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
        soundManager.playSoundEffect("Click");

        key = k;
        hasKeyProperty.set(true);
        return true;
    }

    /**
     * Logic when the Avatar picks up a sword
     * @param s sword being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpSword(Sword s) {
        if (sword != null) return false;
        soundManager.playSoundEffect("Click");

        sword = s;
        swordView.setVisible(true);

        swordDurability.set(sword.getDurability());

        return true;
    }


    /**
     * Logic when the Avatar picks up a Hover pot
     * @param p hover pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpHoverPotion(HoverPotion p) {
        isHovering.set(true);
        soundManager.playSoundEffect("Drink");

        return true;
    }

    /**
     * Logic when the Avatar picks up a Invinc pot
     * @param p invinc pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpInvincibilityPotion(InvincibilityPotion p) {
        ragePotion = p;
        isRaged.set(true);
        soundManager.playSoundEffect("Drink");
        return true;
    }

    /**
     * Logic when the Avatar picks up a Invinc pot
     * @param p invinc pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpBombPotion(BombPotion p) {
        bombRadius.set(bombRadius.get() + 1);
        soundManager.playSoundEffect("Drink");
        return true;
    }

    /**
     * Logic when the Avatar picks up a Arrow
     * @param arrow arrow being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpArrow(Arrow arrow) {
        numArrows.setValue(numArrows.get() + 1);
        soundManager.playSoundEffect("Click");
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
        soundManager.playSoundEffect("Treasure");
        return true;
    }



    // Inventory Observer

    public IntegerProperty getSwordDurability() { return swordDurability; }

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

    public BooleanProperty hasKeyProperty() {
        return hasKeyProperty;
    }





    // Avatar Buff Status

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



    // Statistics

    /**
     * Getter for Avatars past moves
     * @return
     */
    public ArrayList<Integer> getPastMoves() {
        return new ArrayList<>(pastMoves); // TODO unmodifiable?
    }





    // Entity methods

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
        System.out.println("On Enter by Enemy");
        onThreatenedByEnemy(enemy);
    }
}
