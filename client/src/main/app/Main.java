package main.app;

import javafx.application.Application;
import javafx.stage.Stage;
import main.app.model.MainScreen;
import main.client.Client;
import main.client.util.LocalManager;
import main.content.GameConfig;
import main.sound.SoundManager;
import main.content.AchievementInit;
import main.content.StatInit;
import main.trigger.achievement.AchievementSystem;
import main.persistence.JsonPersistor;

/**
 * Class which begins the instance of the Game
 */
public class Main extends Application {

    private final int WIDTH = 960;
    private final int HEIGHT = 640;

    public SoundManager soundManager = SoundManager.getInstance(5);

    public static GameConfig gameConfig;
    public static Client currClient = new Client();

    public static Stage primaryStage;

    public static AchievementSystem achievementSystem = new AchievementSystem();

    @Override
    public void start(Stage s){
        primaryStage = s;


        s.setWidth(WIDTH);
        s.setHeight(HEIGHT);

        LocalManager.loadConfig();

        new StatInit(achievementSystem.getBus(), gameConfig.getIntStat()).init();
        new AchievementInit(achievementSystem, gameConfig.getIntStat()).init();
        new MainScreen(s).start();

    }


    @Override
    public void stop() {
        LocalManager.saveConfig();
        if (currClient.isLoggedin()) {
            LocalManager.uploadConfig();
            currClient.attemptLogout();
        }
        soundManager.shutDown();
    }

    /**
     * Main function which begins the application
     * @param args : system parameters
     */
    public static void main(String[] args) {
        launch(args);
    }
}
