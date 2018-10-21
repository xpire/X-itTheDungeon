package main.app.engine;

/**
 * Interface of the main game
 */
public interface Game {

    /**
     * Actions to occur when the game begins
     */
    void onStart();

    /**
     * Actions to occur during the beginning of a tick
     */
    void onUpdateBegin();

    /**
     * Actions to occur during the game tick
     */
    void onUpdate();

    /**
     * Actions to occur at the end of a game tick
     */
    void onUpdateEnd();

    /**
     * Actions to occur when the game ends
     */
    void onStop();
}



