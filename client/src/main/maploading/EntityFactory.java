package main.maploading;

import main.entities.Entity;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * class which represents entity generation
 * @param <T> the entity
 */
public class EntityFactory<T extends Entity> {

    private HashMap<Character, Supplier<T>> suppliers = new HashMap<>();

    /**
     * checks if entity can be made
     * @param entityCode : entity symbol
     * @return true if can make
     */
    public boolean canMakeEntity(char entityCode) {
        return suppliers.containsKey(entityCode);
    }

    /**
     * Getter for an entity
     * @param entityCode : symbol for the entity
     * @return the entity requested
     * @throws InvalidMapException : bad symbol
     */
    public T getEntity(char entityCode) throws InvalidMapException {
        if (!canMakeEntity(entityCode))
            throw new InvalidMapException("Unrecognised entity code: " + entityCode);

        return suppliers.get(entityCode).get();
    }

    /**
     * adds a supplier to an entity
     * @param entityCode : the entity symbol
     * @param supplier : the supplier
     */
    public void addSupplier(char entityCode, Supplier<T> supplier) {
        suppliers.put(entityCode, supplier);
    }
}
