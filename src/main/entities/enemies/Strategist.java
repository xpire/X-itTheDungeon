package main.entities.enemies;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.behaviour.CowardBehaviour;
import main.behaviour.StrategistBehaviour;
import main.maploading.Level;
import main.math.Vec2i;

public class Strategist extends Enemy {

    {
        symbol      = '2';
        isHunter    = false;
    }

    public Strategist(Level map) {
        super(map);
    }

    public Strategist(Level map, Vec2i pos) {
        super(map, pos);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        view.addNode(new Circle(10, Color.BLUE));
        setCurrBehaviour(new StrategistBehaviour());
    }

    @Override
    public void decideBehaviour() {
        if (level.getAvatar().isOnRage())
            setCurrBehaviour(new CowardBehaviour());
        else
            setCurrBehaviour(new StrategistBehaviour());
    }
}
