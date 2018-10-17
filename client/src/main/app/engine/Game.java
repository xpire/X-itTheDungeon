package main.app.engine;


public interface Game {

    void onStart();

    void onUpdateBegin();

    void onUpdate();

    void onUpdateEnd();

    void onStop();
}



