package main.entities.enemies;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.behaviour.CowardBehaviour;
import main.behaviour.HunterBehaviour;
import main.behaviour.StrategistBehaviour;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Strategist extends Enemy {

    {
        symbol = '2';
    }

    public Strategist(Level map) {
        super(map);
        super.setCurrBehavior(new StrategistBehaviour());
        super.setManager(null);
    }

    public Strategist(Level map, Vec2i pos) {
        super(map, pos);
        super.setCurrBehavior(new StrategistBehaviour());
        super.setManager(null);
    }

    @Override
    public void onCreated(){
        Circle hunt = new Circle();

        hunt.setRadius(10);
        hunt.setFill(Color.BLUE);

        view.addNode(hunt);
        view.setCentre(new Vec2d(0, 0));
    }

    @Override
    public void decideBehaviour(Level map) {
        if (level.getAvatar().isRaged()) {
            super.setCurrBehavior(new CowardBehaviour());
        } else {
            super.setCurrBehavior(new StrategistBehaviour());
        }
    }

    @Override
    public boolean IsHunter() {
        return false;
    }
}
