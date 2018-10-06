package main.app;


import javafx.application.Application;
import javafx.stage.Stage;
import main.app.model.MainScreen;
import main.events.EventBus;
import main.init.AchievementInitialiser;
import main.init.StatisticsInitialiser;
import main.stat.Statistics;
import main.trigger.achievement.AchievementSystem;

public class Main extends Application {

    public static EventBus playModeBus = new EventBus();

    @Override
    public void start(Stage s) throws Exception{

        s.setWidth(960);
        s.setHeight(640);

        AchievementSystem achievementSystem = new AchievementSystem();
        Statistics stats = new Statistics();

        StatisticsInitialiser statInit = new StatisticsInitialiser(playModeBus);
        statInit.init(stats);

        AchievementInitialiser achieveInit = new AchievementInitialiser(achievementSystem, stats);
        achieveInit.init();

        MainScreen sc = new MainScreen(s);
        sc.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
