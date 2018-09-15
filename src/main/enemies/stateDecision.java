package main.enemies;

import main.behaviour.AIBehaviour;
import main.maploading.Level;

public interface stateDecision {
    public void decideBehaviour(Level map);
}
