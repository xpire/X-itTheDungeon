package main.content;


import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleIntegerProperty;
import main.persistence.JsonPersistor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

// Initialiser Class for the Whole Game
public class GameConfig {

    private IntStat intStat = new IntStat();

    public IntStat getIntStat() {
        return intStat;
    }

    public int getMaxLevelCompleted() {
        return intStat.getStat(IntStat.Key.MAX_LEVEL_CONQUERED).get();
    }

    public static GameConfig load(String path) {
        GameConfig config;
        try {
            config = new JsonPersistor().load(path, GameConfig.class, SerialisationProxy.getBuilder().create());
            if (config == null)
                config = new GameConfig();
        }
        catch (Exception e) {
            e.printStackTrace();
            config = new GameConfig();
        }

        return config;
    }

    public static final class SerialisationProxy {

        private final static GsonBuilder builder;

        static {
            builder = IntStat.SerialisationProxy.getBuilder().setPrettyPrinting();
        }
        public static GsonBuilder getBuilder() {
            return builder;
        }
    }
}
