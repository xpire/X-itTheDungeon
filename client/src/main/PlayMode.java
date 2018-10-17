package main;

import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
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
import main.events.AvatarDeathEvent;
import main.events.AvatarEvent;
import main.events.LevelEvent;
import main.maploading.MapLoader;

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

    private StackPane pane = new StackPane();


    // TAKE SCENE OUT!
    public PlayMode(Scene scene, String levelName, String levelPath) {

        level = new MapLoader().loadLevel(levelName, levelPath);
        avatar = level.getAvatar();

        gameLoop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
        input = new Input();

        pane.getChildren().add(level.getView());
        view = new ViewComponent(pane);

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
        level.addEventHandler(AvatarDeathEvent.AVATAR_DEATH, event -> gameOver());
    }

    private void initUi() {

        Label lblNumArrows = new Label();
        PlayLevelController.locator.getInvArrow().getChildren().add(lblNumArrows);
        lblNumArrows.textProperty().bind(Bindings.format("%d", avatar.getNumArrowsProperty()));

        Label lblNumBombs = new Label();
        PlayLevelController.locator.getInvBomb().getChildren().add(lblNumBombs);
        lblNumBombs.textProperty().bind(Bindings.format("%d", avatar.getNumBombsProperty()));

        Label lblSwordDurability = new Label();
        PlayLevelController.locator.getInvSword().getChildren().add(lblSwordDurability);
        lblSwordDurability.textProperty().bind(Bindings.format("%d", avatar.getSwordDurability()));

        Label lblNumTreasures = new Label();
        PlayLevelController.locator.getInvGold().getChildren().add(lblNumTreasures);
        lblNumTreasures.textProperty().bind(Bindings.format("%d", avatar.getNumTreasuresProperty()));

        Label lblHasKey = new Label();
        PlayLevelController.locator.getInvKey().getChildren().add(lblHasKey);
        lblHasKey.textProperty().bind(Bindings.format("%s", avatar.hasKeyProperty()));
    }


    private void initInput(Scene scene) {
        scene.addEventHandler(KeyEvent.ANY, evt -> input.onKeyEvent(evt));

        addAvatarActionBinding(KeyCode.W,       () -> avatar.faceUp());
        addAvatarActionBinding(KeyCode.S,       () -> avatar.faceDown());
        addAvatarActionBinding(KeyCode.A,       () -> avatar.faceLeft());
        addAvatarActionBinding(KeyCode.D,       () -> avatar.faceRight());
        addAvatarActionBinding(KeyCode.UP,      () -> avatar.moveUp());
        addAvatarActionBinding(KeyCode.DOWN,    () -> avatar.moveDown());
        addAvatarActionBinding(KeyCode.LEFT,    () -> avatar.moveLeft());
        addAvatarActionBinding(KeyCode.RIGHT,   () -> avatar.moveRight());
        addAvatarActionBinding(KeyCode.Z,       () -> avatar.dropKey());
        addAvatarActionBinding(KeyCode.X,       () -> avatar.placeBomb());
        addAvatarActionBinding(KeyCode.C,       () -> avatar.shootArrow());
        addAvatarActionBinding(KeyCode.V,       () -> avatar.swingSword());
    }


    private void addAvatarActionBinding(KeyCode code, Runnable action) {
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
        Label lblGameOver = new Label("GAME OVER");
        lblGameOver.fontProperty().set(Font.font(40));
        pane.getChildren().add(lblGameOver);
        isRunning = false;
    }

    private void gameWin() {
        Label lblGameWin = new Label("GAME WON");
        lblGameWin.fontProperty().set(Font.font(40));
        pane.getChildren().add(lblGameWin);
        isRunning = false;

        level.postEvent(new LevelEvent(LevelEvent.LEVEL_PASSED, levelNum));
    }

    public Node getView() {
        return view.getView();
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }
}
