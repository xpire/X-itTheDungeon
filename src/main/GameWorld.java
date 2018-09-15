package main;

import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import main.avatar.Avatar;
import main.entities.*;
import main.maploading.Level;
import main.math.Vec2i;
import main.systems.GridMovementSystem;
import main.systems.PushSystem;

import java.util.ArrayList;
import java.util.Iterator;

public class GameWorld {

    private Level map;
    private Avatar avatar;

    private Group rootView;

    private PushSystem pushSystem;
    private GridMovementSystem moveSystem;


    public GameWorld(Level map) {
        this.map = map;

        rootView = new Group();
        Group gridView = new Group();

        gridView.setTranslateX(120);
        gridView.setTranslateY(20);

        pushSystem = new PushSystem(this);
        moveSystem = new GridMovementSystem(this, map);

        avatar = new Avatar(this);


        Door door1 = new Door();
        Door door2 = new Door();

        Key key1 = new Key();
        Key key2 = new Key();
        key1.setMatchingDoor(door1);
        key2.setMatchingDoor(door2);

        Switch sw = new Switch();
        addNewEntity(3, 3, sw);

        addNewEntity(5, 5, new Wall());
        addNewEntity(2, 8, new Wall());
        addNewEntity(2, 0, new Wall());
        addNewEntity(4, 3, new Boulder());
        addNewEntity(7, 6, new Boulder());

        addNewEntity(7, 7, key2);
        addNewEntity(5, 3, key1);
        addNewEntity(8, 3, door2);
        addNewEntity(1, 3, door1);

        addNewEntity(1, 2, new Arrow());
        addNewEntity(4, 4, new Arrow());
        addNewEntity(8, 10, new Bomb());
        addNewEntity(12, 6, new Bomb());
        addNewEntity(14, 6, new Treasure());
        addNewEntity(2, 11, new Treasure());
        addNewEntity(10, 13, new Treasure());
        addNewEntity(10, 5, new Sword());
        addNewEntity(10, 7, new Sword());
        addNewEntity(9, 9, new HoverPot());
        addNewEntity(10, 10, new InvincibilityPot());
        addNewEntity(14, 14, new Pit());

        addNewEntity(8, 8, avatar);

        Label lblNumArrows = new Label();
        lblNumArrows.textProperty().bind(Bindings.format("Arrows: %d", avatar.getNumArrowsProperty()));

        Label lblNumBombs = new Label();
        lblNumBombs.textProperty().bind(Bindings.format("Bombs: %d", avatar.getNumBombsProperty()));
        lblNumBombs.setTranslateY(15);

        Label lblNumTreasures = new Label();
        lblNumTreasures.textProperty().bind(Bindings.format("Treasures: %d", avatar.getNumTreasuresProperty()));
        lblNumTreasures.setTranslateY(30);

        Iterator<Entity> it = map.getEntities(new Vec2i(5, 5));

        gridView.getChildren().add(map.getView());

        rootView.getChildren().add(gridView);
        rootView.getChildren().add(lblNumArrows);
        rootView.getChildren().add(lblNumBombs);
        rootView.getChildren().add(lblNumTreasures);
    }




    public void addEntity(int x, int y, Entity e) {
        map.addEntity(x, y, e);
    }



    public void addNewEntity(int x, int y, Entity e) {

        if (e instanceof  Boulder) {
            ((Boulder) e).attachPushSystem(pushSystem);
        }
        map.addNewEntity(x, y, e);
    }



    private boolean isPlayerTurn = true;

    public void update(double delta) {

        if (isPlayerTurn) { // || noEnemies();
            onPlayerTurn();
        }
        else {
            onEnemyTurn();
            onRoundEnd();
        }
    }


    public void onPlayerTurn() {
        avatar.update(0);
    }

    public void onPlayerTurnEnded() {
        isPlayerTurn = false;
    }

    public void onEnemyTurn() {
        isPlayerTurn = true;
    }

    public void onRoundEnd() {
        avatar.onRoundEnd();
        for (Bomb bomb : new ArrayList<>(bombs)) {
            bomb.onTurnUpdate();
        }
        checkObjective();
    }

    /*A : Exit the Dungeon
    B : Collect all Treasure
    C : Kill all Enemies
    D : Activate all Switches*/
    public void checkObjective() {
        if (map.getObjectives().contains("A")) {
            for (Entity e : map.getExits()) {
                if (avatar.getX() == e.getX() && avatar.getY() == e.getY()) {
                    //PLAYER ON EXIT
                    gameWin();
                }
            }
        } else {
            Boolean failure = false;
            for (String o : map.getObjectives()) {
                if (o.equals("B")) {
                    if (avatar.getNumTreasuresProperty().getValue() == map.getTreasures().size()) {
                        //successful
                        continue;
                    } else {
                        failure = true;
                    }
                } else if (o.equals("C")) {
                    if (map.getEnemies().size() == 0) {
                        //successful
                        continue;
                    } else {
                        failure = true;
                    }
                } else if (o.equals("D")) {
                    for (Entity s : map.getSwitches()) {
                        if (((Switch) s).getIsOn()) {
                            //so far successful
                            continue;
                        } else {
                            failure = true;
                        }
                    }
//                    if (!failure) {
//                        continue;
//                    }
                }
            }
            if (!failure) {
                gameWin();
            }
        }

    }

    public void render() {
//        avatar.render();
    }

    public Node getView() {
        return rootView;
    }



    public boolean isPassable(Vec2i pos) {
        if (!map.isValidGridPos(pos)) return false;
        return map.isPassable(pos);
    }


    // WILL BE REMOVED
//    public Boulder getBoulder(Vec2i pos) {
//        if (!map.isValidGridPos(pos)) return null;
//        return map.getTile(pos).getBoulder();
//    }

    public Level getMap() {
        return map;
    }


    private ArrayList<Bomb> bombs = new ArrayList<>();
    public boolean onPlace(Entity entity, Vec2i pos) {
        Iterator<Entity> it = map.getEntities(pos);
        while(it.hasNext()) {
            Entity e = it.next();
            if(!e.onEntityPush(entity)) {
                return false;
            }
        }

        if (entity instanceof  Bomb) {
            bombs.add((Bomb) entity);
        }

        map.addNewEntity(pos.getX(), pos.getY(), entity);
        return true;
    }


    // ambiguous name
    public void moveEntity(Entity e, Vec2i pos) {
        if (!map.isValidGridPos(pos)) return;

        moveSystem.onTileMovement(e, pos);
    }


    public void gameOver() {
        Label lblGameOver = new Label("GAME OVER");
        lblGameOver.setTranslateX(250);
        lblGameOver.setTranslateY(220);
        lblGameOver.fontProperty().set(Font.font(40));
        rootView.getChildren().add(lblGameOver);
    }

    public void gameWin() {
        Label lblGameWin = new Label("GAME WON");
        lblGameWin.setTranslateX(250);
        lblGameWin.setTranslateY(220);
        lblGameWin.fontProperty().set(Font.font(40));
        rootView.getChildren().add(lblGameWin);
    }

    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
    }

    public Entity getAvatar() {return avatar;}
}
