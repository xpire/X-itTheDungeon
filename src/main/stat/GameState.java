package main.stat;


// For saving and loading data
public class GameState {

    private GameSettings settings;

    public class LevelState {

        private String levelName;
        private String fileName;
        private boolean isCompleted;

        public LevelState(String levelName, String fileName) {
            this.levelName = levelName;
            this.fileName = fileName;
            isCompleted = false;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted() {
            isCompleted = true;
        }
    }
}
