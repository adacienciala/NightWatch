package nightwatch;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import nightwatch.cameras.CamerasController;
import nightwatch.radio.RadioController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class NightwatchController {
    @FXML public GridPane rootPane;
    @FXML public Pane lightsPane;
    @FXML public Pane audioPane;
    @FXML public Pane emergencyPane;
    @FXML public Pane camerasPane;

    CamerasController[] cameras = new CamerasController[4];
    String[] localizations = {"Entrance", "Lockers", "Main Deposit", "Halls"};

    public static void sleepFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignore) {}
    }

    public static void playSound(String name, double vol) {
        new Thread(() -> {
            HashMap<String, String>lut = new HashMap<>();
            lut.put("button click 1", "src/nightwatch/sounds/blip.wav");
            lut.put("access granted", "src/nightwatch/sounds/blip_denied.wav");
            lut.put("access denied", "src/nightwatch/sounds/button_denied.wav");
            lut.put("door open", "src/nightwatch/sounds/opening.wav");
            lut.put("radio open", "src/nightwatch/sounds/radio1.wav");
            lut.put("radio close", "src/nightwatch/sounds/radio2.wav");
            lut.put("lights open", "src/nightwatch/sounds/lights1.wav");
            lut.put("lights close", "src/nightwatch/sounds/lights2.wav");
            lut.put("flashlight notice", "src/nightwatch/sounds/flashlight.wav");
            lut.put("camera open", "src/nightwatch/sounds/camera1.wav");
            lut.put("camera close", "src/nightwatch/sounds/camera2.wav");
            lut.put("alarm", "src/nightwatch/sounds/alarm_once.wav");
            MediaPlayer sound = new MediaPlayer(new Media(Paths.get(lut.get(name)).toUri().toString()));
            sound.setOnEndOfMedia(() -> {
                System.out.println("Disposing");
                sound.dispose();
            });
            sound.setVolume(vol);
            sound.play();
            sleepFor(1000);
            sleepFor((long)sound.getTotalDuration().toSeconds());
        }).start();
    }

    public static void playSound(String name) {
        playSound(name, 0.5f);
    }

    @FXML
    public void initialize() throws IOException {
        rootPane.setOpacity(0);
        fadeIn();
        openCamerasWindow(null);
    }

    private void fadeIn() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(1000));
        transition.setNode(rootPane);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    public void setLightsValue(int room, double value) {
        cameras[room].setOpacity(value/100.0);
    }

    public void setVolumeValue(int room, double value) {
        cameras[room].setVolume(value);
    }

    @FXML
    public void openLightsWindow(MouseEvent mouseEvent) throws IOException {
        System.out.println("Opening lights");
        playSound("lights open");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lights.fxml"));
        Parent root = loader.load();
        ((LightsController)loader.getController()).setParentController(this);
        Stage stage = new Stage();
        stage.setTitle("Lights");
        stage.setScene(new Scene(root, 600, 800));
        stage.show();

    }

    @FXML
    public void openAudioWindow(MouseEvent mouseEvent) {
        System.out.println("Opening audio");
        playSound("radio open", 1.0f);
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("radio/radio.fxml"));
        try {
            root = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ((RadioController)loader.getController()).setParentController(this);
        Stage stage = new Stage();
        stage.setTitle("Radio");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    @FXML
    public void openEmergencyWindow(MouseEvent mouseEvent) {
        System.out.println("Emergency button!");
        Pane flashing = new Pane();
        flashing.getStyleClass().add("alarm-on");
        rootPane.add(flashing, 0, 0, 13, 7);
        new Thread(() -> {
            int _state = 0;
            for (int i = 0; i < 5; i++) {
                final int state = _state;
                new Thread(() -> {
                    if (state == 0) {
                        playSound("alarm");
                    }
                    System.out.println("From " + state + " to " + (state == 0 ? 1 : 0));
                    FadeTransition transition = new FadeTransition();
                    transition.setDuration(Duration.millis(900));
                    transition.setNode(flashing);
                    transition.setFromValue(state);
                    transition.setToValue(state == 0 ? 1 : 0);
                    transition.play();
                }).start();
                _state = _state == 0 ? 1 : 0;
                sleepFor(1000);
            }
            flashing.setStyle("-fx-background-color: black;");
            flashing.getStyleClass().remove("alarm-on");
            Platform.runLater(() -> {
                File file = new File("src/nightwatch/img/finally_dead.mp4");
                Media media = null;
                try {
                    media = new Media(file.toURI().toURL().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
                MediaPlayer player = new MediaPlayer(media);
                MediaView viewer = new MediaView(player);
                viewer.fitWidthProperty().bind(flashing.widthProperty());
                viewer.fitHeightProperty().bind(flashing.heightProperty());
                flashing.getChildren().add(viewer);
                player.setOnEndOfMedia(() -> {
                    player.dispose();
                    System.exit(0);
                });
                player.play();
                player.setVolume(0.8f);
            });
        }).start();
    }

    @FXML
    public void openCamerasWindow(MouseEvent mouseEvent) throws IOException {
        System.out.println("Opening cameras");

        int sceneMinWidth = 660;
        int sceneMinHeight = 380;
        if (mouseEvent != null) {
            playSound("camera open");
        }
        for (int i = 0; i < 4; ++i) {
            if (cameras[i] == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cameras/cameras.fxml"));
                Parent root = loader.load();
                cameras[i] = loader.getController();
                cameras[i].setCameraFootage(new File(
                        "src/nightwatch/cameras/resources/",
                        "cam"+(i+1)+"_footage.mp4"),
                        localizations[i]);
                Scene scene = new Scene(root, sceneMinWidth, sceneMinHeight);
            } else if (!cameras[i].isOpened()){
                final Stage stage = new Stage();
                double factor = 0.62;
                stage.minHeightProperty().bind(Bindings
                        .when(stage.widthProperty().multiply(factor)
                                .greaterThan(sceneMinHeight))
                        .then(stage.widthProperty().multiply(factor))
                        .otherwise(sceneMinHeight));
                stage.minWidthProperty().bind(Bindings
                        .when(stage.heightProperty().multiply(factor)
                                .greaterThan(sceneMinWidth))
                        .then(stage.widthProperty().multiply(factor))
                        .otherwise(sceneMinWidth));
                stage.maxHeightProperty().bind(stage.widthProperty().multiply(factor));

                int cameraID = i;
                Scene scene = cameras[cameraID].screen.getScene();
                stage.setScene(scene);
                stage.setTitle(String.format("CAM%02d", cameraID+1));
                stage.setOnCloseRequest(e -> {
                    playSound("camera close");
                    cameras[cameraID].setOpened(false);
                });
                cameras[cameraID].setOpened(true);
                stage.show();
            }
        }

    }
}
