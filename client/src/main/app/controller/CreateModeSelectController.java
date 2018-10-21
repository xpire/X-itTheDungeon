package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.Toast;
import main.app.model.CreateModeSelectScreen;
import main.app.model.MainScreen;
import main.sound.SoundManager;

public class CreateModeSelectController extends AppController<CreateModeSelectScreen>{

    SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    private Accordion draftsView;

    @FXML
    private TextField newDraftNameField;

    @FXML
    private Button beginNewDraftBtn;

    public CreateModeSelectController(CreateModeSelectScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {
        screen.initialiseDraftList(draftsView);
    }

    @FXML
    public void onEnterLabBtnPressed() {
        soundManager.playSoundEffect("Item");

        newDraftNameField.setVisible(true);
        beginNewDraftBtn.setVisible(true);
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onBeginNewDraftBtnPressed() {
        if (newDraftNameField.getText().equals("") || !newDraftNameField.getText().matches("[a-zA-Z0-9]+")) {
            Toast.messageToast(screen.getStage(), "Please enter valid name");
            return;
        }

        screen.initialiseNewDraft(newDraftNameField.getText());
    }
}
