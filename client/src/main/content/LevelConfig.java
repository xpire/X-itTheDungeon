package main.content;

public class LevelConfig {

    private String name;
    private String filename;
    private int levelNum;
    private boolean isCompleted;
    private boolean isLocked;

    public LevelConfig(String name, String filename, int levelNum) {
        this.name = name;
        this.filename = filename;
        this.levelNum = levelNum;

        isCompleted = false;
        isLocked = false;
    }

//    public LevelConfig(String name, String filename, boolean isLocked) {
//        this.name = name;
//        this.filename = filename;
//    }


    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public int getLevelNum() {
        return levelNum;
    }

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
