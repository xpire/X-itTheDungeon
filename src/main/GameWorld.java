package main;

import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import main.achivement.ExitDungeonAchievement;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.*;
import main.entities.pickup.*;
import main.entities.prop.Boulder;
import main.entities.terrain.*;
import main.maploading.Level;
import main.math.Vec2i;

public class GameWorld {

    private Level level;
    private Avatar avatar;
    private boolean isPlayerTurn = true;
    private EnemyManager manager;

    private ViewComponent view;

    public GameWorld() {
        this.level = new Level(16, 16, 30, "GameWorld");;

        // Grids
        Group gridView = new Group();
        gridView.setTranslateX(120);
        gridView.setTranslateY(20);

        avatar = new Avatar(level);

        gridView.getChildren().add(level.getView());
        view = new ViewComponent(gridView);

        level.addObjectives(new ExitDungeonAchievement());
//        level.addObjectives(new AllSwitchesOnAchievement());
//        level.addObjectives(new CollectAllTreasuresAchievement());
//        level.addObjectives(new KillAllEnemiesAchievement());

        initWorld();
        initViews();

        this.manager = new EnemyManager(level);
    }

    private void initWorld() {
        level.addAvatar(new Vec2i(7, 7), avatar);
        level.addProp(new Vec2i(7,8), new Boulder(level));
        level.addProp(new Vec2i(4,5), new Boulder(level));
        level.addProp(new Vec2i(8,9), new Boulder(level));

        level.addTerrain(new Vec2i(10,9), new Wall(level));

        level.addTerrain(new Vec2i(6,6), new Switch(level));
        level.addTerrain(new Vec2i(5,6), new Switch(level));
        level.addTerrain(new Vec2i(3,4), new Switch(level));

        level.addTerrain(new Vec2i(2,3), new Pit(level));
        level.addTerrain(new Vec2i(1,4), new Pit(level));

        level.addPickup(new Vec2i(8,4), new Arrow(level));
        level.addPickup(new Vec2i(8,6), new Arrow(level));

        level.addPickup(new Vec2i(7,12), new Sword(level));
        level.addPickup(new Vec2i(7,14), new Sword(level));

        level.addPickup(new Vec2i(8,13), new Bomb(level));
        level.addPickup(new Vec2i(8,13), new Bomb(level));
        level.addPickup(new Vec2i(10,13), new Bomb(level));

        level.addPickup(new Vec2i(5,5), new HoverPotion(level));
        level.addPickup(new Vec2i(7,10), new HoverPotion(level));

        level.addPickup(new Vec2i(7,11), new InvincibilityPotion(level));
        level.addPickup(new Vec2i(8,14), new InvincibilityPotion(level));

//        level.addEnemy(new Vec2i(0, 3), new Hunter(level));
//        level.addEnemy(new Vec2i(12, 12), new Hound(level));
//        level.addEnemy(new Vec2i(0, 0), new Coward(level));
        level.addEnemy(new Vec2i(12, 11), new Strategist(level));



        level.addTerrain(new Vec2i(14, 14), new Exit(level));
        level.addTerrain(new Vec2i(15, 15), new Exit(level));

        Key key1 = new Key(level);
        Key key2 = new Key(level);
        Door door1 = new Door(level);
        Door door2 = new Door(level);
        key1.setMatchingDoor(door1);
        key2.setMatchingDoor(door2);

        level.addPickup(new Vec2i(11,11), key1);
        level.addPickup(new Vec2i(4,14), key2);
        level.addTerrain(new Vec2i(7,2), door1);
        level.addTerrain(new Vec2i(14,12), door2);
    }


    private void initViews() {
        Label lblNumArrows = new Label();
        lblNumArrows.textProperty().bind(Bindings.format("Arrows: %d", avatar.getNumArrowsProperty()));

        Label lblNumBombs = new Label();
        lblNumBombs.textProperty().bind(Bindings.format("Bombs: %d", avatar.getNumBombsProperty()));
        lblNumBombs.setTranslateY(15);

        Label lblNumTreasures = new Label();
        lblNumTreasures.textProperty().bind(Bindings.format("Treasures: %d", avatar.getNumTreasuresProperty()));
        lblNumTreasures.setTranslateY(30);

        view.addNode(lblNumArrows);
        view.addNode(lblNumBombs);
        view.addNode(lblNumTreasures);
    }



    public void update() {
        if (level.getEnemies().size() != 0) {
            if (isPlayerTurn) {
                onPlayerTurn();
            }
            else {
                onEnemyTurn();
                onRoundEnd();
            }
        }
        else {
            onPlayerTurn();
            onRoundEnd();
        }
    }


    public void onPlayerTurn() {
        avatar.update();
    }

    public void endPlayerTurn() {
        isPlayerTurn = false;
    }

    public void onEnemyTurn() { manager.Update(); }

    public void onRoundEnd() {
        isPlayerTurn = true;
        level.getTerrainIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPropIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPickupIterator().forEachRemaining(Entity::onTurnUpdate);
        avatar.onRoundEnd();

        if (level.checkAchievedAllObjectives()) {
            System.out.println("COMPLETED!");
        }
    }



    public void gameOver() {
        Label lblGameOver = new Label("GAME OVER");
        lblGameOver.setTranslateX(250);
        lblGameOver.setTranslateY(220);
        lblGameOver.fontProperty().set(Font.font(40));
        view.addNode(lblGameOver);
    }


    public Node getView() {
        return view.getView();
    }

    public Level getLevel() {
        return level;
    }



    /*A : Exit the Dungeon
    B : Collect all Treasure
    C : Kill all Enemies
    D : Activate all Switches*/
//    public void checkObjective() {
//        if (map.getObjectives().contains("A")) {
//            for (Entity e : map.getExits()) {
//                if (avatar.getX() == e.getX() && avatar.getY() == e.getY()) {
//                    //PLAYER ON EXIT
//                    gameWin();
//                }
//            }
//        } else {
//            Boolean failure = false;
//            for (String o : map.getObjectives()) {
//                if (o.equals("B")) {
//                    if (avatar.getNumTreasuresProperty().getValue() == map.getTreasures().size()) {
//                        //successful
//                        continue;
//                    } else {
//                        failure = true;
//                    }
//                } else if (o.equals("C")) {
//                    if (map.getEnemies().size() == 0) {
//                        //successful
//                        continue;
//                    } else {
//                        failure = true;
//                    }
//                } else if (o.equals("D")) {
//                    for (Entity s : map.getSwitches()) {
//                        if (((Switch) s).getIsOn()) {
//                            //so far successful
//                            continue;
//                        } else {
//                            failure = true;
//                        }
//                    }
////                    if (!failure) {
////                        continue;
////                    }
//                }
//            }
//            if (!failure) {
//                gameWin();
//            }
//        }
//
//    }
//
//
//    public void gameWin() {
//        Label lblGameWin = new Label("GAME WON");
//        lblGameWin.setTranslateX(250);
//        lblGameWin.setTranslateY(220);
//        lblGameWin.fontProperty().set(Font.font(40));
//        rootView.getChildren().add(lblGameWin);
//    }
}
