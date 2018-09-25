package main.entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.app.engine.Game;
import main.entities.enemies.Enemy;
import main.entities.pickup.*;
import main.entities.prop.FlyingArrow;
import main.entities.prop.LitBomb;
import main.entities.prop.Prop;
import main.entities.terrain.Door;
import main.entities.terrain.Terrain;
import main.events.AvatarEvent;
import main.maploading.Level;
import main.math.Vec2i;

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
        isRaged = new SimpleBooleanProperty(false);

        hoverView.visibleProperty().bind(isHovering);
        rageView.visibleProperty().bind(isRaged);
        swordView.setVisible(false);
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

        Vec2i pos = new Vec2i(getGridPos());

        if (Game.input.isDown(KeyCode.UP)) {
            pos._add(0, -1);
            pastMoves.add(0);
        }
        else if (Game.input.isDown(KeyCode.DOWN)) {
            pos._add(0, 1);
            pastMoves.add(1);
        }
        else if (Game.input.isDown(KeyCode.LEFT)) {
            pos._add(-1, 0);
            pastMoves.add(2);
        }
        else if (Game.input.isDown(KeyCode.RIGHT)) {
            pos._add(1, 0);
            pastMoves.add(3);
        }
        else if (Game.input.isDown(KeyCode.Z)) {
            dropKey();
        }
        else if (Game.input.isDown(KeyCode.X)) {
            placeBomb();
        }
        else if (Game.input.isDown(KeyCode.C)) {
            shootArrow();
        }
        else if (Game.input.isDown(KeyCode.V)) {
            swingSword();
        }
        else if (Game.input.isDown(KeyCode.W)) {
            direction = new Vec2i(0, -1);
        }
        else if (Game.input.isDown(KeyCode.A)) {
            direction = new Vec2i(-1, 0);
        }
        else if (Game.input.isDown(KeyCode.S)) {
            direction = new Vec2i(0, 1);
        }
        else if (Game.input.isDown(KeyCode.D)) {
            direction = new Vec2i(1, 0);
        }

        // TODO refactor
        if ( !pos.equals(getGridPos()) ) {
            if (level.isPassableForAvatar(pos, this)) {
                level.moveAvatar(pos);
            }
            else if (level.hasProp(pos)){
                Prop prop = level.getProp(pos);

                if (prop.onPush(this)) {
                    level.moveAvatar(pos);
                }
            }
            else if (level.hasTerrain(pos)) {
                Terrain terrain = level.getTerrain(pos);

                if (terrain.onPush(this)) {
                    level.moveAvatar(pos);
                }
            }

            if (pos.equals(getGridPos()))
                endTurn();
        }
        nextAction = null;
    }

    public void moveUp() {

    }


    public void setNextAction(Runnable action) {
        nextAction = action;
    }

    private void endTurn() {
        level.postEvent(new AvatarEvent(AvatarEvent.AVATAR_TURN_ENDED));
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

        // kill the entity in the avatar's direction
        Vec2i target = pos.add(direction);
        if (level.isValidGridPos(target) && level.hasEnemy(target)) {

            // Kill the enemy
            level.getEnemy(target).onDestroyed();
            sword.reduceDurability();

            // check durability and destroy
            if (sword.isBroken())
                onUnequipSword();

            endTurn();
        }
    }

    /**
     * when the player picks up a sword
     * @param s
     */
    public void onEquipSword(Sword s) {
        sword = s;
        swordView.setVisible(true);
    }

    /**
     * when the player loses their sword
     */
    public void onUnequipSword() {
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

            arrowPos._add(direction);
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

        onEquipSword(s);
        return true;
    }


    /**
     * Logic when the Avatar picks up a Hover pot
     * @param p hover pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpHoverPotion(HoverPotion p) {
        onHoverStart();
        return true;
    }

    /**
     * Logic when the Avatar picks up a Invinc pot
     * @param p invinc pot being picked up
     * @return true if pickup successful, else false
     */
    public boolean pickUpInvincibilityPotion(InvincibilityPotion p) {
        onRageStart(p);
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
    public void onHoverStart() {
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
    public void onRageStart(InvincibilityPotion p) {
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
