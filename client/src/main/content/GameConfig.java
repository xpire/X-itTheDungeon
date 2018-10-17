package main.content;


import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

// Initialiser Class for the Whole Game
public class GameConfig {

    private ArrayList<LevelConfig> levelConfigs;
    private IntStat intStat = new IntStat();

    public GameConfig() {
        levelConfigs = new ArrayList<>();
        levelConfigs.add(new LevelConfig("Level 1", "level01", 1));
        levelConfigs.add(new LevelConfig("Level 2", "level02", 2));
        levelConfigs.add(new LevelConfig("Level 3", "level03", 3));

        levelConfigs.get(0).unlock();
    }

    public IntStat getIntStat() {
        return intStat;
    }

    public int getNumLevels() {
        return levelConfigs.size();
    }

    public Iterator<LevelConfig> levels() {
        return levelConfigs.iterator();
    }

    public boolean isCompleted(int level) {
        return levelConfigs.get(level).isCompleted();
    }

    public boolean isLocked(int level) {
        return levelConfigs.get(level).isLocked();
    }


    public static final class SerialisationProxy {

        private final static GsonBuilder builder;

        static {
            builder = IntStat.SerialisationProxy.getBuilder().setPrettyPrinting();
//                    .registerTypeAdapter(
//                            new TypeToken<ArrayList<LevelConfig>>(){}.getType(),
//                            type -> new ArrayList<>());
        }

        public static GsonBuilder getBuilder() {
            return builder;
        }
    }
}
