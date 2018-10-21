package main;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.app.controller.PlayLevelController;
import main.app.engine.UserAction;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.EnemyCommander;
import main.events.*;
import main.maploading.MapLoader;
import main.sound.SoundManager;


public class LevelPlayer {

    private Level level;
    private String title;
    private String subtitle;
    private int levelNum;

    private ViewComponent view;

    private EnemyCommander enemyCommander;
    private SoundManager soundManager = SoundManager.getInstance(5);

    private StackPane messagePane = new StackPane();
    private boolean isGameOver = false;

    private GameScheduler scheduler;

    private LevelPlayer(Builder builder) {
        this.level      = builder.level;
        this.title      = builder.title;
        this.subtitle   = builder.subtitle;
        this.levelNum   = builder.levelNum;

        messagePane.getChildren().add(level.getView());
        view = new ViewComponent(messagePane);

        if (level.getAvatar() == null) {
            scheduler = new GameScheduler(() -> {}, () -> {}, () -> {}, () -> false);
        }
        else {
            scheduler = new GameScheduler(
                    () -> level.getAvatar().update(),
                    () -> enemyCommander.update(),
                    this::onRoundEnd,
                    () -> !level.getEnemies().isEmpty());
        }

        enemyCommander = new EnemyCommander(level);

        level.addEventHandler(AnimationEvent.ANIMATION_STARTED, e -> scheduler.getInput().stopListening());
        level.addEventHandler(AnimationEvent.ANIMATION_STOPPED, e -> scheduler.getInput().startListening());

        builder.inputNode.addEventHandler(KeyEvent.ANY, scheduler.getInput()::onKeyEvent);

        initEvents();
        initTitleView();
        initObjectivesView();
        initInventoryView(level.getAvatar());
        initInput(level.getAvatar());

    }

    public void startGame() {
        scheduler.start();
        soundManager.playSoundEffect("Start");
    }

    public void endGame() {
        scheduler.stop();
    }

    private void initTitleView() {
        PlayModeUILocator locator = PlayLevelController.locator;
        locator.getLblTitle().setText(title);
        locator.getLblSubtitle().setText(subtitle);
    }

    private void initEvents() {
        level.addEventHandler(AvatarEvent.AVATAR_TURN_ENDED, event -> scheduler.endPlayerTurn());
        level.addEventHandler(DeathEvent.ANY, e -> {
            if (e.isAvatar())
                gameOver();
        });
    }

    private void initObjectivesView() {
        PlayModeUILocator locator = PlayLevelController.locator;
        VBox objectives = locator.getObjectivesPanel();
        objectives.getChildren().clear();
        level.getObjectiveViews().forEachRemaining( o -> objectives.getChildren().add(o.getCheckBox()));
    }

    private void initInventoryView(Avatar avatar) {
        PlayModeUILocator locator = PlayLevelController.locator;
        if(avatar == null) return;
        locator.getInvBomb().bindInteger(avatar.getNumBombsProperty().asObject());
        locator.getInvArrow().bindInteger(avatar.getNumArrowsProperty().asObject());
        locator.getInvSword().bindInteger(avatar.getSwordDurability().asObject());
        locator.getInvGold().bindCountText(avatar.getNumTreasuresProperty().asObject());
        locator.getInvKey().bindBoolean(avatar.hasKeyProperty());
    }

    private void initInput(Avatar avatar) {
        if (avatar == null) return;
        addAction(avatar, KeyCode.W,        avatar::faceUp);
        addAction(avatar, KeyCode.S,        avatar::faceDown);
        addAction(avatar, KeyCode.A,        avatar::faceLeft);
        addAction(avatar, KeyCode.D,        avatar::faceRight);
        addAction(avatar, KeyCode.UP,       avatar::moveUp);
        addAction(avatar, KeyCode.DOWN,     avatar::moveDown);
        addAction(avatar, KeyCode.LEFT,     avatar::moveLeft);
        addAction(avatar, KeyCode.RIGHT,    avatar::moveRight);
        addAction(avatar, KeyCode.Z,        avatar::dropKey);
        addAction(avatar, KeyCode.X,        avatar::placeBomb);
        addAction(avatar, KeyCode.C,        avatar::shootArrow);
        addAction(avatar, KeyCode.V,        avatar::swingSword);
    }

    private void addAction(Avatar avatar, KeyCode code, Runnable action) {
        scheduler.getInput().addBinding(code, new UserAction() {
            @Override
            protected void onActionBegin() {
                avatar.setNextAction(action);
            }
        });
    }

    private void onRoundEnd() {
        scheduler.startPlayerTurn();
        level.getTerrainIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPropIterator().forEachRemaining(Entity::onTurnUpdate);
        level.getPickupIterator().forEachRemaining(Entity::onTurnUpdate);
        if (level.getAvatar() != null)
            level.getAvatar().onRoundEnd();

        if (level.checkAchievedAllObjectives())
            gameWin();
    }

    private void gameOver() {
        isGameOver = true;
        Label lblGameOver = new Label("GAME OVER");
        lblGameOver.fontProperty().set(Font.font(40));
        messagePane.getChildren().add(lblGameOver);

        endGame();
        level.postEvent(new LevelEvent(LevelEvent.LEVEL_FAILED, levelNum));
        soundManager.playSoundEffect("Failure");
    }

    private void gameWin() {
        if (isGameOver) return;

        Label lblGameWin = new Label("GAME WON");
        lblGameWin.fontProperty().set(Font.font(40));
        messagePane.getChildren().add(lblGameWin);

        endGame();
        soundManager.playSoundEffect("Success");
        level.postEvent(new LevelEvent(LevelEvent.LEVEL_PASSED, levelNum));
    }

    public Node getView() {
        return view.getView();
    }

    public static class Builder {
        private Level  level;
        private String title;
        private String subtitle  = "";
        private Scene inputNode = null;
        private int    levelNum  = -1;

//        private boolean isPlaytest     = false;
//        private boolean isCreativeMode = false;

        public Builder(String levelName, String levelPath) {
            this(levelName, levelPath, false);
        }

        public Builder(String levelName, String levelPath, boolean isCreativeMode) {
            title = levelName;
            level = new MapLoader().loadLevel(levelName, levelPath, isCreativeMode);
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder levelNum(int levelNum) {
            this.levelNum = levelNum;
            this.subtitle = "Level " + levelNum;
            return this;
        }

        public Builder addBus(EventBus bus) {
            level.addBus(bus);
            return this;
        }

        public Builder input(Scene inputNode) {
            this.inputNode = inputNode;
            return this;
        }

        public LevelPlayer build() {
            return new LevelPlayer(this);
        }
    }


    public <T extends Event> void addEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        level.addEventHandler(type, handler);
    }

    public boolean checkImmediateCompletion() {
        return level.checkAchievedAllObjectives();
    }

    public Level getLevel() { return level; }
}


