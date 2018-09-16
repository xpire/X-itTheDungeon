package main.entities.enemies;

import main.maploading.Level;

public interface StateDecision {
    public void decideBehaviour(Level map);
}
