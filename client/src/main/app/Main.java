package main.app;


import com.google.gson.Gson;
import javafx.application.Application;
import javafx.stage.Stage;
import main.app.model.MainScreen;
import main.content.GameConfig;
import main.content.IntStat;
import main.events.EventBus;
import main.init.AchievementInitialiser;
import main.init.StatisticsInitialiser;
import main.trigger.achievement.AchievementSystem;
import main.persistence.JsonPersistor;



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
    public static GameConfig gameConfig;

    @Override
    public void start(Stage s){

        s.setWidth(960);
        s.setHeight(640);

        try {
            gameConfig = new JsonPersistor().load("src/save/localsave.json", GameConfig.class, GameConfig.SerialisationProxy.getBuilder().create());
            if (gameConfig == null) {
                gameConfig = new GameConfig();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            gameConfig = new GameConfig();
        }

//        gameConfig = new JsonPersistor().load("src/save/localsave.json", GameConfig.class, GameConfig.SerialisationProxy.getBuilder().create());
//        if (gameConfig == null) {
//            System.out.println("NULL ERROR!");
//            gameConfig = new GameConfig();
//        }

//        gameConfig = new GameConfig();
//        Gson gson = GameConfig.SerialisationProxy.getBuilder().create();

        AchievementSystem achievementSystem = new AchievementSystem();

        StatisticsInitialiser statInit = new StatisticsInitialiser(playModeBus);
        statInit.init(gameConfig.getIntStat());

        AchievementInitialiser achieveInit = new AchievementInitialiser(achievementSystem, gameConfig.getIntStat());
        achieveInit.init();

//        System.out.println("AFTER SETUP");
//        String data = gson.toJson(gameConfig);
//        System.out.println(data);
//
//        GameConfig config2 = gson.fromJson(data, GameConfig.class);
//        System.out.println(config2);
//
//        System.out.println(GameConfig.SerialisationProxy.getBuilder().create().toJson(gameConfig));

        MainScreen sc = new MainScreen(s);
        sc.start();
    }


    @Override
    public void stop() {
//        System.out.println(GameConfig.SerialisationProxy.getBuilder().create().toJson(gameConfig));
        new JsonPersistor().save("src/save/localsave.json", gameConfig, GameConfig.SerialisationProxy.getBuilder().create());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
