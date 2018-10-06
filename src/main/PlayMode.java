package main;

import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import main.app.engine.Game;
import main.app.engine.GameLoop;
import main.app.engine.Input;
import main.app.engine.UserAction;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.EnemyManager;
import main.events.AvatarEvent;

public class PlayMode implements Game {

    private Level level;

    private GameLoop gameLoop;
    private Input input;

    private boolean isRunning = true;
    private boolean isPlayerTurn = true;

    private Avatar avatar;

    private EnemyManager enemyManager;
    private ViewComponent view;

    public PlayMode(Scene scene, Level level) {
        this.level = level;
        avatar = level.getAvatar();

        gameLoop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
        input    = new Input();

        // View
        Group gridView = new Group();
        gridView.setTranslateX(120);
        gridView.setTranslateY(20);
        gridView.getChildren().add(level.getView());
        view = new ViewComponent(gridView);

        initEvents();
        initUi();
        initInput(scene);

        enemyManager = new EnemyManager(level);
    }

    public void startGame() {
        gameLoop.start();
        input.startListening();
    }

    public void endGame() {
        gameLoop.stop();
    }

    private void initEvents() {
        level.addEventHandler(AvatarEvent.AVATAR_TURN_ENDED, event -> endPlayerTurn());
        level.addEventHandler(AvatarEvent.AVATAR_DIED, event -> gameOver());
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
        lblGameOver.setTranslateX(250);
        lblGameOver.setTranslateY(220);
        lblGameOver.fontProperty().set(Font.font(40));
        view.addNode(lblGameOver);
        isRunning = false;
    }

    private void gameWin() {
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
