package main.entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Level;
import main.entities.enemies.Enemy;
import main.entities.pickup.*;
import main.entities.prop.FlyingArrow;
import main.entities.prop.LitBomb;
import main.entities.terrain.Door;
import main.entities.terrain.Pit;
import main.events.AvatarDeathEvent;
import main.events.AvatarEvent;
import main.math.Vec2i;

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
        Circle circle = new Circle(10, Color.AQUA);
        view.addNode(circle);

        hoverView   = new Circle(8, Color.LIMEGREEN);
        rageView    = new Circle(4, Color.TOMATO);
        swordView   = new Line(-10, 0, 10, 0);

        view.addNode(hoverView);
        view.addNode(rageView);
        view.addNode(swordView);

        isHovering  = new SimpleBooleanProperty(false);
        isRaged     = new SimpleBooleanProperty(false);
        bombRadius  = new SimpleIntegerProperty(1);


        hoverView.visibleProperty().bind(isHovering);
        rageView.visibleProperty().bind(isRaged);
        swordView.setVisible(false);
    }


    @Override
    public void onDestroyed() {
        level.removeAvatar();
    }

    public void onThreatenedByPit(Pit pit) {
        if (!isHovering()) {
            level.postEvent(AvatarDeathEvent.deathByFalling());
            onDestroyed();
        }
    }

    public void onThreatenedByBomb(Bomb bomb) {
        if (isRaged()) return;

        level.postEvent(AvatarDeathEvent.deathByExplosion());
        onDestroyed();
    }

    public void onThreatenedByEnemy(Enemy enemy) {
        if (isRaged()) {
            enemy.onDestroyed();
        }
        else {
            level.postEvent(AvatarDeathEvent.deathByAttack());
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
        tryMove(Vec2i.NORTH);
    }
    public void moveDown() {
        tryMove(Vec2i.SOUTH);
    }
    public void moveLeft() {
        tryMove(Vec2i.WEST);
    }
    public void moveRight() {
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
        setDirection(Vec2i.NORTH);
    }
    public void faceDown() {
        setDirection(Vec2i.SOUTH);
    }
    public void faceLeft() {
        setDirection(Vec2i.WEST);
    }
    public void faceRight() {
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

        // kill the entity in the avatar's direction
        Vec2i target = pos.add(direction);
        if (level.isValidGridPos(target) && level.hasEnemy(target)) {

            // Kill the enemy
            level.getEnemy(target).onDestroyed();
            sword.reduceDurability();
            swordDurability.set(sword.getDurability());

            // check durability and destroy
            if (sword.isBroken()) {
                sword = null;
                swordView.setVisible(false);
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

        FlyingArrow arrow = new FlyingArrow(level);
        Vec2i arrowPos = new Vec2i(pos).add(direction);

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
        return true;
    }

    /**
     * Logic when the Avatar picks up a Invinc pot
     * @param p invinc pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpBombPotion(BombPotion p) {
        bombRadius.set(bombRadius.get() + 1);
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
