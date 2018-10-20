package main;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.app.controller.PlayLevelController;
import main.app.engine.Game;
import main.app.engine.GameLoop;
import main.app.engine.Input;
import main.app.engine.UserAction;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.EnemyManager;
import main.events.DeathEvent;
import main.events.AvatarEvent;
import main.events.LevelEvent;
import main.maploading.MapLoader;
import main.sound.SoundManager;


/*
TODO
 - Achievements page, notification
 - More achievements
 - Help Manual
 - Settings
 - Inventory View
 */

public class PlayMode implements Game {

    private Level level;
    private int levelNum = -1;

    private GameLoop gameLoop;
    public static Input input;

    private boolean isRunning = true;
    private boolean isPlayerTurn = true;

    private Avatar avatar;

    private EnemyManager enemyManager;
    private ViewComponent view;
    private SoundManager soundManager = SoundManager.getInstance(5);

    private StackPane pane = new StackPane();
    private boolean isGameOver = false;

    // TAKE SCENE OUT!
    public PlayMode(Scene scene, String levelName, String levelPath) {

        level = new MapLoader().loadLevel(levelName, levelPath);
        avatar = level.getAvatar();

        pane.getChildren().add(level.getView());
        view = new ViewComponent(pane);
        input = new Input();

        if (avatar == null) {
            gameLoop = new GameLoop(new Game() {
                @Override
                public void onStart() {
                }

                @Override
                public void onUpdateBegin() {

                }

                @Override
                public void onUpdate() {

                }

                @Override
                public void onUpdateEnd() {

                }

                @Override
                public void onStop() {

                }
            }, fps -> System.out.println("FPS: " + fps));
            return;
        }

        gameLoop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));

        initEvents();
        initUi();
        initInput(scene);

        enemyManager = new EnemyManager(level);
    }

    public void startGame() {
        gameLoop.start();
        input.startListening();
    }

    public void pauseGame() {
        gameLoop.stop();
        input.stopListening();
    }

    public void endGame() {
        gameLoop.stop();
    }

    private void initEvents() {
        level.addEventHandler(AvatarEvent.AVATAR_TURN_ENDED, event -> endPlayerTurn());
        level.addEventHandler(DeathEvent.ANY, e -> {
            System.out.println("DIED! " + e.isAvatar());

            if (e.isAvatar())
                gameOver();
        });
    }

    private void initUi() {
        PlayModeUILocator locator = PlayLevelController.locator;

        VBox objectives = locator.getObjectivesPanel();
        objectives.getChildren().clear();
        level.getObjectiveViews().forEachRemaining( o -> objectives.getChildren().add(o.getCheckBox()));

        Label lblNumArrows = new Label();
        locator.getInvArrow().getChildren().clear();
        locator.getInvArrow().getChildren().add(lblNumArrows);
        lblNumArrows.textProperty().bind(Bindings.format("%d", avatar.getNumArrowsProperty()));

        Label lblNumBombs = new Label();
        locator.getInvBomb().getChildren().clear();
        locator.getInvBomb().getChildren().add(lblNumBombs);
        lblNumBombs.textProperty().bind(Bindings.format("%d", avatar.getNumBombsProperty()));

        Label lblSwordDurability = new Label();
        locator.getInvSword().getChildren().clear();
        locator.getInvSword().getChildren().add(lblSwordDurability);
        lblSwordDurability.textProperty().bind(Bindings.format("%d", avatar.getSwordDurability()));

        Label lblNumTreasures = new Label();
        locator.getInvGold().getChildren().clear();
        locator.getInvGold().getChildren().add(lblNumTreasures);
        lblNumTreasures.textProperty().bind(Bindings.format("%d", avatar.getNumTreasuresProperty()));

        Label lblHasKey = new Label();
        locator.getInvGold().getChildren().clear();
        locator.getInvKey().getChildren().add(lblHasKey);
        lblHasKey.textProperty().bind(Bindings.format("%s", avatar.hasKeyProperty()));
    }


    private void initInput(Scene scene) {
        scene.addEventHandler(KeyEvent.ANY, evt -> input.onKeyEvent(evt));

        addAction(KeyCode.W,       () -> avatar.faceUp());
        addAction(KeyCode.S,       () -> avatar.faceDown());
        addAction(KeyCode.A,       () -> avatar.faceLeft());
        addAction(KeyCode.D,       () -> avatar.faceRight());
        addAction(KeyCode.UP,      () -> avatar.moveUp());
        addAction(KeyCode.DOWN,    () -> avatar.moveDown());
        addAction(KeyCode.LEFT,    () -> avatar.moveLeft());
        addAction(KeyCode.RIGHT,   () -> avatar.moveRight());
        addAction(KeyCode.Z,       () -> avatar.dropKey());
        addAction(KeyCode.X,       () -> avatar.placeBomb());
        addAction(KeyCode.C,       () -> avatar.shootArrow());
        addAction(KeyCode.V,       () -> avatar.swingSword());
    }


    private void addAction(KeyCode code, Runnable action) {
        input.addBinding(code, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(action);
            }
        });
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

    private void onPlayerTurn() {
        avatar.update();
    }

    private void endPlayerTurn() {
        isPlayerTurn = false;
    }

    private void onEnemyTurn() {
        enemyManager.update();
    }

    private void onRoundEnd() {
        isPlayerTurn = true;
        level.getTerrainIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPropIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPickupIterator().forEachRemaining(Entity::onTurnUpdate);
        avatar.onRoundEnd();

        if (level.checkAchievedAllObjectives()) {
            gameWin();
        }
    }

    private void gameOver() {
        isGameOver = true;
        Label lblGameOver = new Label("GAME OVER");
        lblGameOver.fontProperty().set(Font.font(40));
        pane.getChildren().add(lblGameOver);
        isRunning = false;
        level.postEvent(new LevelEvent(LevelEvent.LEVEL_FAILED, levelNum));
        soundManager.playSoundEffect("Failure");
    }

    private void gameWin() {
        if (isGameOver) return;
        Label lblGameWin = new Label("GAME WON");
        lblGameWin.fontProperty().set(Font.font(40));
        pane.getChildren().add(lblGameWin);
        isRunning = false;
        soundManager.playSoundEffect("Success");

        level.postEvent(new LevelEvent(LevelEvent.LEVEL_PASSED, levelNum));
    }

    public Node getView() {
        return view.getView();
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }
}
