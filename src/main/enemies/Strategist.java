package main.enemies;

import main.behaviour.AIBehaviour;
import main.behaviour.StrategistBehaviour;
import main.maploading.Level;
import main.math.Vec2i;

public class Strategist extends Enemy implements stateDecision  {
    // Note that there are no changes to behaviour
    public Strategist(String name, Level map, Vec2i pos) {
        super(name, map, pos, false);
        super.setCurrBehavior(new StrategistBehaviour());
        super.setManager(null);
    }

    @Override
    public void decideBehaviour(Level map) { }

    @Override
    public boolean IsHunter() {
        return false;
    }
}
