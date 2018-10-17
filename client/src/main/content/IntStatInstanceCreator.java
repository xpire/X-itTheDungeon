package main.content;


import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleIntegerProperty;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.function.Consumer;

public class IntStatInstanceCreator implements InstanceCreator<IntStat> {

    @Override
    public IntStat createInstance(final Type type) {
            return new IntStat();
        }
}
