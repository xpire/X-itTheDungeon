package main.events;

import javafx.event.Event;
import javafx.event.EventType;


public class AnimationEvent extends GameEvent {

    public static final EventType<AnimationEvent> ANY
            = new EventType<>(GameEvent.ANY,"ANIMATION");

    public static final EventType<AnimationEvent> ANIMATION_STARTED
            = new EventType<>(AnimationEvent.ANY,"ANIMATION_STARTED");

    public static final EventType<AnimationEvent> ANIMATION_STOPPED
            = new EventType<>(AnimationEvent.ANY,"ANIMATION_STOPPED");

    public AnimationEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
