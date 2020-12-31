package nightwatch;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import nightwatch.cameras.CamerasController;
import nightwatch.radio.RadioController;

import java.io.File;
import java.io.IOException;

public class NightwatchController {
    @FXML public GridPane rootPane;
    @FXML public Pane lightsPane;
    @FXML public Pane audioPane;
    @FXML public Pane emergencyPane;
    @FXML public Pane camerasPane;

    CamerasController[] cameras = new CamerasController[4];

    @FXML
    public void initialize() {
        rootPane.setOpacity(0);
        fadeIn();
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
        if (!lightsPane.getStyleClass().remove("lights-pane")) {
            lightsPane.getStyleClass().remove("lights-pane-active");
            lightsPane.getStyleClass().add("lights-pane");
        }
        else lightsPane.getStyleClass().add("lights-pane-active");

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
        if (!audioPane.getStyleClass().remove("audio-pane")) {
            audioPane.getStyleClass().remove("audio-pane-active");
            audioPane.getStyleClass().add("audio-pane");
        }
        else audioPane.getStyleClass().add("audio-pane-active");
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
        if (!emergencyPane.getStyleClass().remove("emergency-pane")) {
            emergencyPane.getStyleClass().remove("emergency-pane-active");
            emergencyPane.getStyleClass().add("emergency-pane");
        }
        else emergencyPane.getStyleClass().add("emergency-pane-active");
    }

    @FXML
    public void openCamerasWindow(MouseEvent mouseEvent) throws IOException {
        System.out.println("Opening cameras");
        if (!camerasPane.getStyleClass().remove("cameras-pane")) {
            camerasPane.getStyleClass().remove("cameras-pane-active");
            camerasPane.getStyleClass().add("cameras-pane");
        }
        else camerasPane.getStyleClass().add("cameras-pane-active");
        for (int i = 0; i < 4; ++i) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cameras/cameras.fxml"));
            Parent root = loader.load();
            cameras[i] = loader.getController();
            cameras[i].setCameraFootage(new File("src/nightwatch/cameras/resources/", "cam"+(i+1)+"footage.mp4"));
            Stage stage = new Stage();
            stage.setTitle("Camera" + (i+1));
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        }

    }
}
