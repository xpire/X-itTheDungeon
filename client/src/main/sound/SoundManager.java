package main.sound;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayerBuilder;
import main.Level;
import main.events.KeyEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager {

    private static SoundManager soundManagerInstance = null;
    private ExecutorService soundPool;
    private Map<String, AudioClip> soundEffectsMap = new HashMap<>();
    private double FXVolume = 1.0;
//    private ExecutorService BGMPool;
    private MediaPlayer BGMPlayer;
    private Map<String, Media> BGMMap = new HashMap<>();
    private double BGMVolume = 0.5;

    private SoundManager(int numberOfThreads) {
        soundPool = Executors.newFixedThreadPool(numberOfThreads);

        //initialise sounds to be used
        //Buttons
        loadSoundEffect("Ting", "sfx/snd_dewdrop.mp3");
        loadSoundEffect("Secret Notification", "sfx/snd_secret.mp3");
        loadSoundEffect("Item", "sfx/snd_item.mp3");
        //enemies
        loadSoundEffect("Mob", "sfx/snd_bones.mp3");
        //props
        // - bomb
        loadSoundEffect("Lit Bomb Explosion","sfx/snd_blast.mp3");
        loadSoundEffect("Lit Bomb", "sfx/snd_burning.mp3");
        // - boulder
        loadSoundEffect("Boulder", "sfx/snd_meld.mp3");
        // - flying arrow
        loadSoundEffect("Puff", "sfx/snd_puff.mp3");
        //TODO: find a better whooshing sound
        // - ice block
        //TODO: find a sound for ice block
        //terrain
        // - Door
        loadSoundEffect("Door Locked", "sfx/snd_door_open.mp3");
        // - Switch
        loadSoundEffect("Click","sfx/snd_click.mp3");
        //Avatar
        // - die
        //      - by Bomb
        //      - by Enemy
        loadSoundEffect("Death", "sfx/snd_die.mp3");
        //      - by Pit
        loadSoundEffect("Falling","sfx/snd_falling.mp3");
        // - swind sword
        loadSoundEffect("Hit", "sfx/snd_hit.mp3");
        //      - sword breaks
        loadSoundEffect("Shatter", "sfx/snd_shatter.mp3");
        // - shoot arrow
        loadSoundEffect("Arrow", "sfx/snd_miss.mp3");
        // - place bomb
        loadSoundEffect("Bomb", "sfx/snd_trap.mp3");
        // - use key
        loadSoundEffect("Unlock", "sfx/snd_unlock.mp3");
        // - pickup
        //      - item
        //TODO: find a sound for general picking up items
        //      - potion
        loadSoundEffect("Drink", "sfx/snd_drink.mp3");
        //      - treasure
        loadSoundEffect("Treasure", "sfx/snd_gold.mp3");
        loadSoundEffect("Coin", "sfx/snd_coin.mp3");
        // Game
        // - start
        loadSoundEffect("Start", "sfx/snd_descend.mp3");
        // - Success
        loadSoundEffect("Achievement", "sfx/snd_mastery.mp3");
        loadSoundEffect("Success", "sfx/snd_success.mp3");
        // - Failure
        loadSoundEffect("Failure", "sfx/snd_lullaby.mp3");
        //BGM
        loadMusic("Main Menu","bgm/05-hendy_marvin-ready_for_action.mp3");
        loadMusic("Level 1", "bgm/02-hendy_marvin-plan_revenge.mp3");
        loadMusic("Level 2", "bgm/03-hendy_marvin-rushing_fast.mp3");
        loadMusic("Level 3", "bgm/theme.mp3");
        loadMusic("Happy", "bgm/surface.mp3");
        loadMusic("Cutscene", "bgm/game.mp3");

        playBGM("Main Menu");
    }

    public static SoundManager getInstance(int numberOfThreads) {
        if (soundManagerInstance == null)
            soundManagerInstance = new SoundManager(numberOfThreads);

        return soundManagerInstance;
    }

//    StringBuilder mapPath = new StringBuilder("./src/main/");
//        mapPath.append(path).append("/").append(mapName).append(".txt");
    public void loadSoundEffect(String name, String source) {
        StringBuilder sb = new StringBuilder("src/asset/music/");
        sb.append(source);
        File file = new File(sb.toString());
        System.out.println(file.toURI().toString());
        AudioClip sound = new AudioClip(file.toURI().toString());
        soundEffectsMap.put(name,sound);
    }

    public void loadMusic(String name, String source) {
        StringBuilder sb = new StringBuilder("src/asset/music/");
        sb.append(source);
        File file = new File(sb.toString());
        System.out.println(file.toURI().toString());
        Media sound = new Media(file.toURI().toString());
        BGMMap.put(name,sound);
    }

    public void playSoundEffect(String name) {
        Runnable soundPlay = () -> soundEffectsMap.get(name).play(FXVolume);
        soundPool.execute(soundPlay);
    }

    public void playBGM(String name) {
        if (BGMPlayer != null) {
            BGMPlayer.stop();
            BGMPlayer.dispose();
        }
        Media sound = BGMMap.get(name);
        BGMPlayer = new MediaPlayer(sound);
        BGMPlayer.setVolume(BGMVolume);
        BGMPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        BGMPlayer.play();
    }

    public void setFXVolume(double value) {
        if (value >= 0.0 && value <= 1.0)
            FXVolume = value;
    }

    public void setBGMVolume(double value) {
        if (value >= 0.0 && value <= 1.0)
            BGMVolume = value;
        if (BGMPlayer != null)
            BGMPlayer.setVolume(BGMVolume);
    }

    public void shutDown() {
        soundPool.shutdown();
    }
}
