package main.persistence;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;

/**
 * Saves and loads data in json format
 */
public class JsonPersistor {

    /**
     * save function
     * @param filename : name of file
     * @param obj : object to be saved
     * @param gson : Gson instance
     * @param <T> : generic
     */
    public <T> void save(String filename, T obj, Gson gson) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(obj, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Load function
     * @param filename name of file
     * @param clazz class of the object
     * @param gson : Gson instance
     * @param <T> : generic
     * @return The loaded object
     */
    public <T> T load(String filename, Class<T> clazz, Gson gson) {
        try (JsonReader reader = new JsonReader(new FileReader(filename))) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
