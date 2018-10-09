package main.app;


import javafx.application.Application;
import javafx.stage.Stage;
import main.app.model.MainScreen;
import main.events.EventBus;
import main.init.AchievementInitialiser;
import main.init.StatisticsInitialiser;
import main.stat.GameState;
import main.stat.Statistics;
import main.trigger.achievement.AchievementSystem;


/*
Todo:
1. local storage
2. unlocking next level
3. stars
4. unlocking boss level
5. entities: fire spikes, bomb radius buff potion,

 */

public class Main extends Application {

    public static EventBus playModeBus = new EventBus();
    public static Statistics stats;
    public static GameState state;

    @Override
    public void start(Stage s) throws Exception{

        s.setWidth(960);
        s.setHeight(640);

        AchievementSystem achievementSystem = new AchievementSystem();
        stats = new Statistics();

        StatisticsInitialiser statInit = new StatisticsInitialiser(playModeBus);
        statInit.init(stats);

        AchievementInitialiser achieveInit = new AchievementInitialiser(achievementSystem, stats);
        achieveInit.init();

        state = new GameState();

        MainScreen sc = new MainScreen(s);
        sc.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
