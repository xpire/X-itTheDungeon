package main.app;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import main.app.model.MainScreen;
import main.content.EnumMapInstanceCreator;
import main.content.IntStat;
import main.events.EventBus;
import main.init.AchievementInitialiser;
import main.init.StatisticsInitialiser;
import main.trigger.achievement.AchievementSystem;

import java.util.EnumMap;


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
    public static IntStat stats;

    @Override
    public void start(Stage s) throws Exception{

        s.setWidth(960);
        s.setHeight(640);

        AchievementSystem achievementSystem = new AchievementSystem();
        stats = new IntStat();

        StatisticsInitialiser statInit = new StatisticsInitialiser(playModeBus);
        statInit.init(stats);

        AchievementInitialiser achieveInit = new AchievementInitialiser(achievementSystem, stats);
        achieveInit.init();



//        IntegerStatistics<IntStatKey> z = new IntegerStatistics<>(IntStatKey.class, IntStatKey.values());
//        z.set(IntStatKey.NUM_DEATHS, 100);
//        z.set(IntStatKey.NUM_COLLECTED, 10);
//        z.increment(IntStatKey.NUM_COLLECTED);
//
//        System.out.println(z.get(IntStatKey.NUM_DEATHS));
//        System.out.println(z.get(IntStatKey.NUM_COLLECTED));

//        Type type = new TypeToken<IntStat>(){}.getType();

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(
                    new TypeToken<EnumMap<IntStat.Key, SimpleIntegerProperty>>(){}.getType(),
                    new EnumMapInstanceCreator<IntStat.Key, SimpleIntegerProperty>(IntStat.Key.class));

        IntStat stats = new IntStat();
        Gson gson = builder.create();
        System.out.println(gson.toJson(stats));
        IntStat newStats = gson.fromJson(gson.toJson(stats), IntStat.class);
        newStats.increment(IntStat.Key.NUM_PIT_DEATHS);

        System.out.println(gson.toJson(newStats));

        MainScreen sc = new MainScreen(s);
        sc.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
