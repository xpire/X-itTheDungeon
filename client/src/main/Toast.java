package main;

import com.sun.javafx.css.Style;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.LinkedList;

public final class Toast
{
    private static boolean locked = false;
    private static LinkedList<ToastRequest> requests = new LinkedList<>();

//    public static void makeToast(Stage parent, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
//
//        ToastRequest request = new ToastRequest(parent, toastMsg, toastDelay, fadeInDelay, fadeOutDelay);
//
//        if (!locked) {
//            request.consume();
//        }
//        else {
//            requests.add(request);
//        }
//    }

    private static void makeToast(ToastRequest request) {
        if (!locked) {
            request.consume();
        }
        else {
            requests.add(request);
        }
    }

    private static void nextToast() {
        if (!requests.isEmpty()) {
            requests.removeFirst().consume();
        }
    }

    private static void display(Stage parent, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay,
                                double fontSize, String style)
    {
        locked = true;

        // Create toast
        Stage toast = new Stage();
        toast.initOwner(parent);
        toast.initStyle(StageStyle.TRANSPARENT);
        toast.setResizable(false);
        toast.setAlwaysOnTop(true);

        // Create toast content
        Text text = new Text(toastMsg);
        text.setFont(Font.font("Verdana", fontSize));
        text.setFill(Color.RED);

        StackPane root = new StackPane(text);
        root.setStyle(style);
        root.setOpacity(0);
        root.setFocusTraversable(false);
        root.setMouseTransparent(true);

        Scene scene = new Scene(root);
        scene.setOnMouseClicked(e -> closeToast(toast));
        scene.setFill(Color.TRANSPARENT);
        toast.setScene(scene);
        toast.show();

        parent.requestFocus();

        // Display toast
        Timeline fadeIn = phase(toast, fadeInDelay, 1);
        Timeline stay = phase(toast, toastDelay, 1);
        Timeline fadeOut = phase(toast, fadeOutDelay, 0);

        fadeIn.setOnFinished(e  -> stay.play());
        stay.setOnFinished(e   -> fadeOut.play());
        fadeOut.setOnFinished(e -> closeToast(toast));
        fadeIn.play();
    }


    static private void closeToast(Stage toast) {
        toast.close();
        locked = false;
        nextToast();
    }

    static private Timeline phase(Stage toast, int duration, int endOpacity) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(getOpacityTransition(toast, duration, endOpacity));
        return timeline;
    }

    static private KeyFrame getOpacityTransition(Stage s, int duration, int endOpacity) {
        return new KeyFrame(Duration.millis(duration), new KeyValue (s.getScene().getRoot().opacityProperty(), endOpacity));
    }


    private static class ToastRequest {

        private Stage parent;
        private String toastMsg;
        private int toastDelay;
        private int fadeInDelay;
        private int fadeOutDelay;
        private double fontSize;
        private String style;


        private ToastRequest(Builder builder) {
            this.parent         = builder.parent;
            this.toastMsg       = builder.toastMsg;
            this.toastDelay     = builder.toastDelay;
            this.fadeInDelay    = builder.fadeInDelay;
            this.fadeOutDelay   = builder.fadeOutDelay;
            this.fontSize       = builder.fontSize;
            this.style          = builder.style;
        }

        private void consume() {
            Toast.display(parent, toastMsg, toastDelay, fadeInDelay, fadeOutDelay, fontSize, style);
        }
    }

    public static void messageToast(Stage parent, String message) {
        new Toast.Builder(parent)
                .msg(message)
                .toastDelay(3000)
                .fadeInDelay(250)
                .fadeOutDelay(500)
                .fontSize(26)
                .style("-fx-background-radius: 10; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 5px;")
                .makeToast();
    }

    public static class Builder {
        private Stage parent;
        private String toastMsg = "";
        private int toastDelay = 1000;
        private int fadeInDelay = 1000;
        private int fadeOutDelay = 1000;
        private double fontSize = 40;
        private String style = "-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 50px;";

        public Builder(Stage parent) {
            this.parent = parent;
        }

        public Builder msg(String msg) {
            this.toastMsg = msg;
            return this;
        }

        public Builder toastDelay(int delay) {
            this.toastDelay = delay;
            return this;
        }

        public Builder fadeInDelay(int delay) {
            this.fadeInDelay = delay;
            return this;
        }


        public Builder fadeOutDelay(int delay) {
            this.fadeOutDelay = delay;
            return this;
        }

        public Builder fontSize(double fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder style(String style) {
            this.style = style;
            return this;
        }

        public void makeToast() {
            Toast.makeToast(new ToastRequest(this));
        }
    }
}