package main;

import main.trigger.achievement.Achievement;
import main.trigger.achievement.AchievementSystem;

public class ServiceLocator {

    private final AchievementSystem achievementSystem;

    private ServiceLocator(ServiceLocatorBuilder builder) {
        achievementSystem = builder.achievementSystem;
    }


    public AchievementSystem getAchievementSystem() {
        return achievementSystem;
    }

    public static class ServiceLocatorBuilder {

        private AchievementSystem achievementSystem;

        public ServiceLocatorBuilder achievementSystem(AchievementSystem achievementSystem) {
            this.achievementSystem = achievementSystem;
            return this;
        }

        public ServiceLocator build() {
            return new ServiceLocator(this);
        }
    }
}
