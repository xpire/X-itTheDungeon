package main.entities.enemies;

/**
 * Interface to decide AI behaviour
 */

public interface StateDecision {
    /**
     * Tells an AI which behaviour it should follow
     */
    void decideBehaviour();
}
