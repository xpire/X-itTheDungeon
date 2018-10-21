package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.AppScreen;
import main.app.model.LocalDraftsScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.structure.ReqStructure;
import main.client.util.LocalManager;
import main.sound.SoundManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Controller for the Local Drafts screen
 */
public class LocalDraftsController extends AppController {

    public static String PATH = "src/asset/buffer/";

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public LocalDraftsController(AppScreen screen) { super(screen); }

    @FXML
    public TabPane paneDraft;

    private SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    public void initialize() {
        if (Main.currClient.isLoggedin()) {
            ArrayList<LocalManager.LocalStructure> pData = LocalManager.fetchLocalDraft(Main.currClient.getLoggedUser());
            Tab ptab = new Tab();
            ptab.setText("My drafts");

            Accordion personalList = new Accordion();

            for (LocalManager.LocalStructure x: pData) {
                TitledPane curr = new TitledPane();
                curr.setText(x.mapname);

                Button uploadBtn = new Button();

                uploadBtn.setOnAction(e -> {
                    AlertHelper.showAlert(Alert
                            .AlertType.INFORMATION,
                            "Message",
                            Main.currClient
                                    .attemptUpload(
                                            x.mapname,
                                            x.mapContent)
                    );
                });

                Button play = new Button();
                play.setText("Play this level");

                play.setOnAction(e ->{
                    String map = x.mapContent;

                    File test = new File(PATH + "buffer.txt");
                    try {
                        PrintWriter writer = new PrintWriter(test);
                        writer.print("");
                        writer.close();

                        FileWriter writer2 = new FileWriter(test);
                        writer2.write(map);
                        writer2.close();
                    }
                    catch (IOException s) { s.printStackTrace(); }

                    switchScreen(new PlayLevelScreen(
                            screen,
                            screen.getStage(),
                            "buffer",
                            PATH,
                            0 ,
                            false
                    ));
                });

                uploadBtn.setText("Upload this map.");

                Button deleteBtn = new Button();
                deleteBtn.setText("Delete this map");

                deleteBtn.setOnAction(e -> {
                    LocalManager.delMapDraft(x, Main.currClient.getLoggedUser());
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message", "Map deleted");
                    switchScreen(new LocalDraftsScreen(screen.getStage()));
                });


                VBox box = new VBox();
                box.getChildren().addAll(new Label("Author: " + x.username), uploadBtn, play, deleteBtn);
                curr.setContent(box);
                personalList.getPanes().add(curr);
            }

            ptab.setContent(personalList);
            paneDraft.getTabs().addAll(ptab);
        }

        // add the default tab
        ArrayList<LocalManager.LocalStructure> dData = LocalManager.fetchLocalDraft("default");

        Tab dTab = new Tab();
        dTab.setText("Default");

        Accordion defaultList = new Accordion();

        for (LocalManager.LocalStructure x: dData) {
            TitledPane curr = new TitledPane();
            curr.setText(x.mapname);

            TextField addReq = new TextField();

            addReq.setVisible(false);

            Button tryAgain = new Button();
            tryAgain.setText("Try again");

            tryAgain.setVisible(false);

            Button uploadBtn = new Button();
            uploadBtn.setText("Add this draft to my drafts.");


            if (!Main.currClient.isLoggedin()) { uploadBtn.setVisible(false); }

            // Button is pressed
            uploadBtn.setOnAction(e -> {
                soundManager.playSoundEffect("Item");
                System.out.println(x.mapname);
                if (LocalManager.hasMapLocal(new ReqStructure(Main.currClient.getLoggedUser(), x.mapname), "drafts")) {
                    addReq.setVisible(true);
                    addReq.setText("Duplicate name!!!");
                    tryAgain.setVisible(true);
                    uploadBtn.setVisible(false);
                }
                else {

                    LocalManager.moveDraftTo(x);
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message", "Success!!");
                    switchScreen(new LocalDraftsScreen(screen.getStage()));
                }
            });

            tryAgain.setOnAction(e -> {
                if (!addReq.getText().matches("[a-zA-Z0-9]+")) {
                    addReq.setText("InvalidInput");
                    return;
                }

                if (LocalManager.hasMapLocal(new ReqStructure(Main.currClient.getLoggedUser(), addReq.getText()), "drafts")) {
                    addReq.setText("Duplicate name!!!");
                }

                else {
                    LocalManager.moveDraftTo(x, addReq.getText());
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message", "Success!!");
                    switchScreen(new LocalDraftsScreen(screen.getStage()));
                }
            });

            Button play = new Button();
            play.setText("Play this level");

            play.setOnAction(e ->{
                String map = x.mapContent;

                System.out.println(map);
                File test = new File(PATH + "buffer.txt");
                try {
                    PrintWriter writer = new PrintWriter(test);
                    writer.print("");
                    writer.close();

                    FileWriter writer2 = new FileWriter(test);
                    writer2.write(map);
                    writer2.close();
                }
                catch (IOException s) { s.printStackTrace(); }

                switchScreen(new PlayLevelScreen(
                        screen,
                        screen.getStage(),
                        "buffer",
                        PATH,
                        0 ,
                        false
                ));
            });

            Button deleteBtn = new Button();
            deleteBtn.setText("Delete this map");

            deleteBtn.setOnAction(e -> {
                LocalManager.delMapDraft(x, "default");
                AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message", "Map deleted");
                switchScreen(new LocalDraftsScreen(screen.getStage()));
            });


            VBox box = new VBox();
            box.getChildren().addAll(new Label("Author: " + x.username), uploadBtn, addReq, tryAgain, play, deleteBtn);
            curr.setContent(box);
            defaultList.getPanes().add(curr);
        }

        dTab.setContent(defaultList);
        paneDraft.getTabs().addAll(dTab);
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
