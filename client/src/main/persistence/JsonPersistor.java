package main.persistence;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class JsonPersistor {

    public <T> void save(String filename, T obj, Gson gson) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(obj, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public <T> T load(String filename, Class<T> clazz, Gson gson) {
        try (JsonReader reader = new JsonReader(new FileReader(filename))) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
