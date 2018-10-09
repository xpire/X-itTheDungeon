package main.content;

public class LevelConfig {

    private String name;
    private String filename;
    private boolean isCompleted;
    private boolean isLocked;

    public LevelConfig(String name, String filename) {
        this.name = name;
        this.filename = filename;

        isCompleted = false;
        isLocked = false;
    }

//    public LevelConfig(String name, String filename, boolean isLocked) {
//        this.name = name;
//        this.filename = filename;
//    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted() {
        this.isCompleted = true;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void unlock() {
        this.isLocked = true;
    }
}
