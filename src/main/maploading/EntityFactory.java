package main.maploading;

import main.entities.Entity;

import java.util.HashMap;
import java.util.function.Supplier;

public class EntityFactory<T extends Entity> {

    private HashMap<Character, Supplier<T>> suppliers = new HashMap<>();

    public boolean canMakeEntity(char entityCode) {
        return suppliers.containsKey(entityCode);
    }

    public T getEntity(char entityCode) throws InvalidMapException {
        if (!canMakeEntity(entityCode))
            throw new InvalidMapException("Unrecognised entity code: " + entityCode);

        return suppliers.get(entityCode).get();
    }

    public void addSupplier(char entityCode, Supplier<T> supplier) {
        suppliers.put(entityCode, supplier);
    }
}
