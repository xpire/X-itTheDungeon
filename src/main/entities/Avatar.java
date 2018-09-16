package main.entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Game;
import main.entities.enemies.Enemy;
import main.entities.pickup.*;
import main.entities.prop.LitBomb;
import main.entities.prop.Prop;
import main.entities.terrain.Door;
import main.entities.terrain.Terrain;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;


public class Avatar extends Entity {

    private Key key = null;

    private Sword sword = null;
    private Line swordEquipView;

    private BooleanProperty isHovering;
    private Circle hoveringView;

    private InvincibilityPotion ragePot;
    private BooleanProperty isInvincible;
    private Circle rageView;

    private Vec2i direction;
    {
        direction = new Vec2i(1,0);
    }

    private ArrayList<Integer> pastMoves;

    private IntegerProperty numArrows = new SimpleIntegerProperty(0);
    private IntegerProperty numBombs = new SimpleIntegerProperty(0);
    private IntegerProperty numTreasures = new SimpleIntegerProperty(0);

    {
        symbol = 'P';
        pastMoves = new ArrayList<>();
    }

    public Avatar(Level level) {
        super(level);
    }


    public Avatar(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated() {
        Circle circle = new Circle();
        circle.setRadius(10);
        circle.setFill(Color.AQUA);

        view.addNode(circle);
        view.setCentre(new Vec2d(0, 0));

        hoveringView = new Circle(8, Color.LIMEGREEN);
        rageView = new Circle(4, Color.TOMATO);
        swordEquipView = new Line(-10, 0, 10, 0);

        view.addNode(hoveringView);
        view.addNode(rageView);
        view.addNode(swordEquipView);

        isHovering = new SimpleBooleanProperty(false);
        isInvincible = new SimpleBooleanProperty(false);

        hoveringView.visibleProperty().bind(isHovering);
        rageView.visibleProperty().bind(isInvincible);
        swordEquipView.setVisible(false);
    }

    @Override
    public void onDestroyed() {
        level.removeAvatar();
        Game.world.gameOver();
    }

    @Override
    public void onExploded() {
        if (!isInvincible.get()) {
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

            if (pos.equals(getGridPos())) {
                Game.world.endPlayerTurn();
            }
        }
    }

    /**
     * The player requested to swing sword
     */
    private void swingSword() {
        // Can swing sword ?
        if (!(sword == null)) {
            // Kill the entity in direction of user facing
            if (level.isValidGridPos(pos.add(direction)) && level.hasEnemy(pos.add(direction))) {
                // Kill the enemy
                level.getEnemy(pos.add(direction)).onDestroyed();

                sword.reduceDurability();
                // Durability and destroy
                if (sword.getDurability() == 0) { sword.onDestroyed(); }
            }
        }

    }

    /**
     * The player requested to shoot an arrow
     */
    private void shootArrow() {

        // Can shoot arrow ?
        if (numArrows.get() > 0) {
            Vec2i arrowPosition = new Vec2i(pos);
            arrowPosition = arrowPosition.add(direction);

            // Go through and see if there r enemies to kill
            while(level.isValidGridPos(arrowPosition)) {
                if (level.hasEnemy(arrowPosition)) {
                    level.getEnemy(arrowPosition).onDestroyed();
                }
                // Destroys arrow if it hit anything at all
                else if (!level.isPassableForProp(arrowPosition, null)) {
                    break;
                }
                arrowPosition._add(direction);
            }
            // -1 arrow if shot
            numArrows.set(numArrows.get() - 1);
        }
    }



    /*
    Visitor Pattern
     */
//    public boolean pickUp(Pickup pickup) {
//        System.out.println("PICKUP!");
//        return false;
//    }

    public boolean pickUpKey(Key k) {
        if (key != null)
            return false;

        key = k;
        return true;
    }

    public boolean pickUpSword(Sword s) {
        if (sword != null)
            return false;

        onEquipSword(s);
        return true;
    }

    public boolean pickUpHoverPotion(HoverPotion p) {
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
        ragePot = p;
        isInvincible.set(true);
    }

    public void onRageEnd() {
        isInvincible.set(false);
    }


    /*
    Key Methods
     */
    public boolean hasKeyFor(Door door) {
        return key != null && key.getMatchingDoor().equals(door);
    }

    public void useKey() {
        key = null;
    }


    /*
    Sword methods
     */
//    public void swingSword() {
//        if (sword == null) return;
//
//        if (true) { // If attack successful
//            sword.reduceDurability();
//
//            if (sword.isBroken()) {
//                onUnequipSword();
//            }
//        }
//    }

    public void onEquipSword(Sword s) {
        sword = s;
        swordEquipView.setVisible(true);
    }

    public void onUnequipSword() {
        sword = null;
        swordEquipView.setVisible(false);
    }


    /*
    Arrow Methods
     */

    public IntegerProperty getNumArrowsProperty() {
        return numArrows;
    }

    public IntegerProperty getNumBombsProperty() {
        return numBombs;
    }

    public IntegerProperty getNumTreasuresProperty() {
        return numTreasures;
    }



    /*
    Bomb Methods
     */
    public void placeBomb() {
        if (numBombs.get() <= 0) return;

        level.addProp(pos, new LitBomb(level));
        numBombs.set(numBombs.get() - 1);

//        if (world.onPlace(bomb, pos)) {
//            numBombs.set(numBombs.get() - 1);
//            bomb.onLit();
//        }

        Game.world.endPlayerTurn();
    }

    public void dropKey() {

        if (key == null) return;

        level.addPickup(pos, key);
        key = null;

        Game.world.endPlayerTurn();

//        if (level.addPickup(pos, key)) {
//            key = null;
//            Game.world.onPlayerTurnEnded();
//        }
    }


    public boolean isHovering() {
        return isHovering.get();
    }

    public void onRoundEnd() {
        if (ragePot == null) return;
        ragePot.reduceDuration();

        if (ragePot.hasExpired()) {
            onRageEnd();
        }
    }

    public boolean isRaged() {
        return isInvincible.get();
    }

    public ArrayList<Integer> getPastmoves() {
        return this.pastMoves;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackForTerrain(Terrain terrain) {
        return canStackFor(terrain);
    }

    @Override
    public boolean canStackForProp(Prop prop) {
        return canStackFor(prop);
    }

    @Override
    public boolean canStackForPickup(Pickup pickup) {
        return canStackFor(pickup);
    }

    @Override
    public boolean canStackForEnemy(Enemy enemy) {
        return canStackFor(enemy);
    }

    @Override
    public boolean canStackForAvatar(Avatar avatar) {
        return canStackFor(avatar);
    }

}
