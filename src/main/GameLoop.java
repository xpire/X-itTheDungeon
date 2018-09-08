package main;

import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

public class GameLoop extends AnimationTimer {

    private final float NANO_PER_SEC = 1000000000f;
    private final float SEC_PER_TICK = 1 / 60f;
    private final float MAX_TICKS_PER_FRAME = 5;
    private final int LAG_CAP = 100; // 100 seconds

    private long prevTime = 0;
    private float lag = 0;

    private float secondsElapsed = 0.0f;
    private int nFrames = 0;

    private Game game;
    private Consumer<Integer> fpsReporter;


//    private Runnable preUpdater;
//    private Consumer<Float> updater;
//    private Runnable postUpdater;


//    public main.GameLoop(Runnable preUpdater, Consumer<Float> updater, Runnable postUpdater, Consumer<Integer> fpsReporter) {
//        this.preUpdater     = preUpdater;
//        this.updater        = updater;
//        this.postUpdater    = postUpdater;
//        this.fpsReporter    = fpsReporter;
//    }

    public GameLoop(Game game, Consumer<Integer> fpsReporter) {
        this.game = game;
        this.fpsReporter = fpsReporter;
    }


    @Override
    public void handle(long currTime)
    {
        if (prevTime == 0) {
            prevTime = currTime;
            return;
        }

        lag += (currTime - prevTime) / NANO_PER_SEC;

        // main.Input Processing
        game.onBeforeUpdate();

        // Update Loop
        int count = 0;
        while (lag >= SEC_PER_TICK && count < MAX_TICKS_PER_FRAME) {
            game.onUpdate(SEC_PER_TICK);
            lag -= SEC_PER_TICK;
            count++;
        }

        // Rendering
        game.onAfterUpdate();

        // Float overflow handling
        if (lag > LAG_CAP) {
            lag = LAG_CAP / 10.0f;
        }

        // FPS reporting
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
    }
}