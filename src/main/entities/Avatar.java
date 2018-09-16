package main.entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.app.Game;
import main.entities.enemies.Enemy;
import main.entities.pickup.*;
import main.entities.prop.FlyingArrow;
import main.entities.prop.LitBomb;
import main.entities.prop.Prop;
import main.entities.terrain.Door;
import main.entities.terrain.Terrain;
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
    private BooleanProperty isOnRage;

    private Vec2i direction;
    private ArrayList<Integer> pastMoves;

    private Line swordView;
    private Circle hoverView;
    private Circle rageView;

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
    }



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
        isOnRage    = new SimpleBooleanProperty(false);

        hoverView.visibleProperty().bind(isHovering);
        rageView.visibleProperty().bind(isOnRage);
        swordView.setVisible(false);
    }


    @Override
    public void onDestroyed() {
        level.removeAvatar();
        Game.world.gameOver(); //TODO
    }

    @Override
    public void onExploded() {
        if (!isOnRage.get()) {
            onDestroyed();
        }
    }




    public void update() {
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
                Game.world.endPlayerTurn(); // TODO
        }
    }


    public void onRoundEnd() {
        if (ragePotion == null) return;

        ragePotion.reduceDuration();

        if (ragePotion.hasExpired())
            onRageEnd();
    }


    /**
     * The player requested to swing sword
     */
    private void swingSword() {
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
        }
    }

    public void onEquipSword(Sword s) {
        sword = s;
        swordView.setVisible(true);
    }

    public void onUnequipSword() {
        sword = null;
        swordView.setVisible(false);
    }


    /**
     * The player requested to shoot an arrow
     */
    private void shootArrow() {
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
    }





    public void placeBomb() {
        if (numBombs.get() <= 0) return;

        LitBomb bomb = new LitBomb(level);

        if (level.canPlaceProp(pos, bomb)) {
            numBombs.set(numBombs.get() - 1);
            Game.world.endPlayerTurn();
        }
    }



    public void dropKey() {
        if (key == null) return;

        if (level.canPlacePickup(pos, key)) {
            key = null;
            Game.world.endPlayerTurn();
        }
    }

    public boolean hasKeyFor(Door door) {
        return key != null && key.isMatchingDoor(door);
    }

    public void useKey() {
        key = null;
    }



    /*
        Pickup functions
     */
    public boolean pickUpKey(Key k) {
        if (key != null) return false;

        key = k;
        return true;
    }

    public boolean pickUpSword(Sword s) {
        if (sword != null) return false;

        onEquipSword(s);
        return true;
    }

    public boolean pickUpHoverPotion(HoverPotion p) { // TODO
        onHoverStart();
        return true;
    }

    public boolean pickUpInvincibilityPotion(InvincibilityPotion p) {
        onRageStart(p);
        return true;
    }

    public boolean pickUpArrow(Arrow arrow) {
        numArrows.setValue(numArrows.get() + 1);
        return true;
    }

    public boolean pickUpBomb(Bomb bomb) {
        numBombs.setValue(numBombs.get() + 1);
        return true;
    }

    public boolean pickUpTreasure(Treasure treasure) {
        numTreasures.setValue(numTreasures.get() + 1);
        return true;
    }






    public void onHoverStart() {
        isHovering.set(true);
    }

    public void onHoverEnd() {
        isHovering.set(false);
    }

    public void onRageStart(InvincibilityPotion p) {
        ragePotion = p;
        isOnRage.set(true);
    }

    public void onRageEnd() {
        isOnRage.set(false);
    }






    public IntegerProperty getNumArrowsProperty() {
        return numArrows;
    }

    public IntegerProperty getNumBombsProperty() {
        return numBombs;
    }

    public IntegerProperty getNumTreasuresProperty() {
        return numTreasures;
    }


    public boolean isHovering() {
        return isHovering.get();
    }

    public boolean isOnRage() {
        return isOnRage.get();
    }

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
        if (isOnRage())
            enemy.onDestroyed();
        else
            onDestroyed();
    }
}
