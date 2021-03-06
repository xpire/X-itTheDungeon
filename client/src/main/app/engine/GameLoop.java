package main.app.engine;

import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

/**
 * Class which runs the game loop of the application
 */
public class GameLoop extends AnimationTimer {

    private final float NANO_PER_SEC = 1000000000f;
    private final float SEC_PER_TICK = 1 / 60f;
    private final float MAX_TICKS_PER_FRAME = 5;
    private final int LAG_CAP = 100;

    private long prevTime = 0;
    private float lag = 0;

    private float secondsElapsed = 0.0f;
    private int nFrames = 0;

    private Game game;
    private Consumer<Integer> fpsReporter;

    /**
     * Basic constructor
     * @param game : instance of the game
     * @param fpsReporter : an fps counter for the game
     */
    public GameLoop(Game game, Consumer<Integer> fpsReporter) {
        this.game = game;
        this.fpsReporter = fpsReporter;
    }

    @Override
    public void start() {
        super.start();
        game.onStart();
    }

    @Override
    public void handle(long currTime)
    {
        if (prevTime == 0) {
            prevTime = currTime;
            return;
        }

        lag += (currTime - prevTime) / NANO_PER_SEC;

        game.onUpdateBegin();

        int count = 0;
        while (lag >= SEC_PER_TICK && count < MAX_TICKS_PER_FRAME) {
            game.onUpdate();
            lag -= SEC_PER_TICK;
            count++;
        }

        game.onUpdateEnd();

        if (lag > LAG_CAP) {
            lag = LAG_CAP / 10.0f;
        }

        nFrames++;
        secondsElapsed += (currTime - prevTime) / NANO_PER_SEC;
        if (secondsElapsed >= 2.0f) {
            fpsReporter.accept(Math.round(nFrames / secondsElapsed));
            nFrames = 0;
            secondsElapsed = 0.0f;
        }

        prevTime = currTime;
    }


    @Override
    public void stop()
    {
        super.stop();
        prevTime = 0;
        lag = 0.0f;
        nFrames = 0;
        secondsElapsed = 0.0f;

        game.onStop();
    }
}