package main.avatar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Game;
import main.GameWorld;
import main.entities.*;
import main.math.Vec2d;
import main.math.Vec2i;


public class Avatar extends Entity {

    protected GameWorld world;
    private Key key = null;

    private Sword sword = null;
    private Line swordEquipView;

    private BooleanProperty isHovering;
    private Circle hoveringView;

    private BooleanProperty isInvincible;
    private Circle rageView;


    private IntegerProperty numArrows = new SimpleIntegerProperty(0);
    private IntegerProperty numBombs = new SimpleIntegerProperty(0);
    private IntegerProperty numTreasures = new SimpleIntegerProperty(0);


    public Avatar(GameWorld world) {
        super("Avatar");
        this.world = world;
    }


    @Override
    public void onCreated() {
        Circle circle = new Circle();
        circle.setRadius(10);
        circle.setFill(Color.AQUA);

//        swordEquipView.setTranslateY(5);

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


    public void update(double delta) {
        Vec2i pos = new Vec2i(getGridPos());

        if (Game.input.isDown(KeyCode.UP)) {
            pos._add(0, -1);
        }
        else if (Game.input.isDown(KeyCode.DOWN)) {
            pos._add(0, 1);
        }
        else if (Game.input.isDown(KeyCode.LEFT)) {
            pos._add(-1, 0);
        }
        else if (Game.input.isDown(KeyCode.RIGHT)) {
            pos._add(1, 0);
        }
        else if (Game.input.isDown(KeyCode.Z)) {
            dropKey();
        }


        if ( !pos.equals(getGridPos()) ) {
//            if (world.isPassable(pos)) {
//                moveTo(pos);
//            }
////            else {
////                world.push(this, pos);
////            }

            world.moveEntity(this, pos);
        }
    }


    public void render() {
        // should pull it back out
//        Vec2d pos = world.gridPosToWorldPosCentre(gridPos);
//        view.setTranslateX(pos.getX());
//        view.setTranslateY(pos.getY());
    }



    /*
    Visitor Pattern
     */
    public boolean pickUp(Key k) {

        // Already has a key
        if (key != null)
            return false;

        // Pickup key
        key = k;
        return true;
    }

    public boolean pickUp(Sword s) {

        // Already has a key
        if (sword != null)
            return false;

        // Pickup key
        onEquipSword(s);
        return true;
    }

    public boolean pickUp(Arrow arrow) {
        numArrows.setValue(numArrows.get() + 1);
        return true;
    }

    public boolean pickUp(Bomb bomb) {
        numBombs.setValue(numBombs.get() + 1);
        return true;
    }

    public boolean pickUp(Treasure treasure) {
        numTreasures.setValue(numTreasures.get() + 1);
        return true;
    }

    // Infinite time -> no timer callback
    // Limited time -> timer callback setup
    public void onHoverStart() {
        isHovering.set(true);
    }

    public void onHoverEnd() {
        isHovering.set(false);
    }

    public void onRageStart() {
        isInvincible.set(true);
    }

    public void onRageEnd() {
        isInvincible.set(false);
    }

    public void onDeath() {
        onRemovedFromMap();
        world.gameOver();
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

    public void dropKey() {

        if (key == null) return;

        if (world.onPlace(key, pos)) {
            key = null;
        }
    }



    /*
    Sword methods
     */
    public void swingSword() {
        if (sword == null) return;

        if (true) { // If attack successful
            sword.reduceDurability();

            if (sword.isBroken()) {
                onUnequipSword();
            }
        }
    }

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

        Bomb bomb = new Bomb();
        if (world.onPlace(bomb, pos)) {
            bomb.onLit();
        }
    }


    public boolean isHovering() {
        return isHovering.get();
    }
}
