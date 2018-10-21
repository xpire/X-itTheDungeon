package main;

import
        main.app.engine.Game;
import main.app.engine.GameLoop;
import main.app.engine.Input;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class GameScheduler implements Game {

    private GameLoop gameLoop;
    private Input input;

    private Runnable onPlayerTurn;
    private Runnable onEnemyTurn;
    private Runnable onRoundEnd;
    private Supplier<Boolean> hasEnemies;

    private boolean isRunning = true;
    private boolean isPlayerTurn = true;

    public GameScheduler(Runnable onPlayerTurn, Runnable onEnemyTurn, Runnable onRoundEnd, Supplier<Boolean> hasEnemies) {
        this.onPlayerTurn   = onPlayerTurn;
        this.onEnemyTurn    = onEnemyTurn;
        this.onRoundEnd     = onRoundEnd;
        this.hasEnemies     = hasEnemies;

        input = new Input();
        gameLoop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
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

        if (hasEnemies.get()) {
            if (isPlayerTurn) {
                onPlayerTurn.run();

            } else {
                onEnemyTurn.run();
                onRoundEnd.run();
            }
        } else {
            onPlayerTurn.run();

            if (!isPlayerTurn && isRunning) {
                onRoundEnd.run();
            }
        }
    }

    @Override
    public void onUpdateEnd() {}

    @Override
    public void onStop() {}

    public void startPlayerTurn() {
        isPlayerTurn = true;
    }

    public void endPlayerTurn() {
        isPlayerTurn = false;
    }

    public void start() {
        if (gameLoop == null) return;
        isRunning = true;
        gameLoop.start();
        input.startListening();
    }

    public void stop() {
        if (gameLoop == null) return;
        isRunning = false;
        gameLoop.stop();
    }

    public Input getInput() {
        return input;
    }
}
