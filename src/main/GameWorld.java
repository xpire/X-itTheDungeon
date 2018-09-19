package main;

import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import main.achivement.AllSwitchesOnAchievement;
import main.achivement.CollectAllTreasuresAchievement;
import main.app.engine.Game;
import main.app.engine.GameLoop;
import main.app.engine.Input;
import main.app.engine.UserAction;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.*;
import main.entities.pickup.*;
import main.entities.prop.Boulder;
import main.entities.terrain.*;
import main.events.AvatarEvent;
import main.math.Vec2i;

public class GameWorld implements Game {

    private GameLoop gameLoop;
    private Input input;
    private Level level;
    private Avatar avatar;

    private boolean isRunning = true;
    private boolean isPlayerTurn = true;

    private EnemyManager enemyManager;
    private ViewComponent view;

    public GameWorld(Scene scene) {
        gameLoop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));

        level = new Level(16, 16, 30, "GameWorld");

        // View
        Group gridView = new Group();
        gridView.setTranslateX(120);
        gridView.setTranslateY(20);
        gridView.getChildren().add(level.getView());
        view = new ViewComponent(gridView);

        initObjectives();
        initEvents();
        initEntities();
        initUi();
        initInput(scene);

        enemyManager = new EnemyManager(level);
    }

    public void startGame() {
        gameLoop.start();
    }

    public void endGame() {
        gameLoop.stop();
    }

    private void initEntities() {
        avatar = new Avatar(level);

        level.addAvatar(new Vec2i(7, 7), avatar);
        level.addProp(new Vec2i(7, 8), new Boulder(level));
        level.addProp(new Vec2i(4, 5), new Boulder(level));
        level.addProp(new Vec2i(8, 9), new Boulder(level));

        level.addTerrain(new Vec2i(10, 9), new Wall(level));

        level.addTerrain(new Vec2i(6, 6), new Switch(level));
        level.addTerrain(new Vec2i(5, 6), new Switch(level));
        level.addTerrain(new Vec2i(3, 4), new Switch(level));

        level.addTerrain(new Vec2i(2, 3), new Pit(level));
        level.addTerrain(new Vec2i(1, 4), new Pit(level));

        level.addPickup(new Vec2i(8, 4), new Arrow(level));
        level.addPickup(new Vec2i(8, 6), new Arrow(level));

        level.addPickup(new Vec2i(7, 12), new Sword(level));
        level.addPickup(new Vec2i(7, 14), new Sword(level));

        level.addPickup(new Vec2i(8, 13), new Bomb(level));
        level.addPickup(new Vec2i(8, 13), new Bomb(level));
        level.addPickup(new Vec2i(10, 13), new Bomb(level));
        level.addPickup(new Vec2i(1, 8), new Treasure(level));
        level.addPickup(new Vec2i(2, 5), new Treasure(level));
        level.addPickup(new Vec2i(3, 9), new Treasure(level));

        level.addPickup(new Vec2i(5, 5), new HoverPotion(level));
        level.addPickup(new Vec2i(7, 10), new HoverPotion(level));

        level.addPickup(new Vec2i(7, 11), new InvincibilityPotion(level));
        level.addPickup(new Vec2i(8, 14), new InvincibilityPotion(level));

        level.addEnemy(new Vec2i(0, 3), new Hunter(level));
        level.addEnemy(new Vec2i(12, 12), new Hound(level));
        level.addEnemy(new Vec2i(0, 0), new Coward(level));
        level.addEnemy(new Vec2i(12, 11), new Strategist(level));

        level.addTerrain(new Vec2i(14, 14), new Exit(level));
        level.addTerrain(new Vec2i(15, 15), new Exit(level));

        Key key1 = new Key(level);
        Key key2 = new Key(level);
        Door door1 = new Door(level);
        Door door2 = new Door(level);
        key1.setMatchingDoor(door1);
        key2.setMatchingDoor(door2);

        level.addPickup(new Vec2i(11, 11), key1);
        level.addPickup(new Vec2i(4, 14), key2);
        level.addTerrain(new Vec2i(7, 2), door1);
        level.addTerrain(new Vec2i(14, 12), door2);
    }
    private void initObjectives() {
        //        level.addObjectives(new ExitDungeonAchievement());
        level.addObjectives(new AllSwitchesOnAchievement());
        level.addObjectives(new CollectAllTreasuresAchievement());
//        level.addObjectives(new KillAllEnemiesAchievement());
    }
    private void initUi() {
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
    private void initInput(Scene scene) {
        input = new Input();
        scene.addEventHandler(KeyEvent.ANY, evt -> input.onKeyEvent(evt));
        input.addBinding(KeyCode.W, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.faceUp());
            }
        });
        input.addBinding(KeyCode.S, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.faceDown());
            }
        });
        input.addBinding(KeyCode.A, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.faceLeft());
            }
        });
        input.addBinding(KeyCode.D, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.faceRight());
            }
        });
        input.addBinding(KeyCode.UP, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.moveUp());
            }
        });
        input.addBinding(KeyCode.DOWN, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.moveDown());
            }
        });
        input.addBinding(KeyCode.LEFT, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.moveLeft());
            }
        });
        input.addBinding(KeyCode.RIGHT, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.moveRight());
            }
        });
        input.addBinding(KeyCode.Z, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.dropKey());
            }
        });
        input.addBinding(KeyCode.X, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.placeBomb());
            }
        });
        input.addBinding(KeyCode.C, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.shootArrow());
            }
        });
        input.addBinding(KeyCode.V, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(() -> avatar.swingSword());
            }
        });
    }

    private void initEvents() {
        level.addEventHandler(AvatarEvent.AVATAR_TURN_ENDED, event -> endPlayerTurn());
        level.addEventHandler(AvatarEvent.AVATAR_DIED, event -> gameOver());
    }

    @Override
    public void onStart() {
        input.startListening();
    }

    @Override
    public void onUpdateBegin() {
        input.update();
    }

    @Override
    public void onUpdate() {
        if (!isRunning) return;

        if (!level.getEnemies().isEmpty()) {
            if (isPlayerTurn) {
                onPlayerTurn();
            } else {
                onEnemyTurn();
                onRoundEnd();
            }
        } else {
            onPlayerTurn();

            if (!isPlayerTurn) {
                onRoundEnd();
            }
        }
    }

    @Override
    public void onUpdateEnd() {

    }

    @Override
    public void onStop() {

    }

    public void onPlayerTurn() {
        avatar.update();
    }

    public void endPlayerTurn() {
        isPlayerTurn = false;
    }

    public void onEnemyTurn() {
        enemyManager.Update();
    }

    public void onRoundEnd() {
        isPlayerTurn = true;
        level.getTerrainIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPropIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPickupIterator().forEachRemaining(Entity::onTurnUpdate);
        avatar.onRoundEnd();

        if (level.checkAchievedAllObjectives()) {
            gameWin();
        }
    }

    public void gameOver() {
        Label lblGameOver = new Label("GAME OVER");
        lblGameOver.setTranslateX(250);
        lblGameOver.setTranslateY(220);
        lblGameOver.fontProperty().set(Font.font(40));
        view.addNode(lblGameOver);
        isRunning = false;
    }

    public void gameWin() {
        Label lblGameWin = new Label("GAME WON");
        lblGameWin.setTranslateX(250);
        lblGameWin.setTranslateY(220);
        lblGameWin.fontProperty().set(Font.font(40));
        view.addNode(lblGameWin);
        isRunning = false;
    }

    public Node getView() {
        return view.getView();
    }
}
