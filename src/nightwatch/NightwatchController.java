package nightwatch;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class NightwatchController {
    public Pane lightsPane;
    public Pane audioPane;
    public Pane emergencyPane;

    public void openLightsWindow(MouseEvent mouseEvent) throws IOException {
        System.out.println("Opening lights");
        if (!lightsPane.getStyleClass().remove("lights-pane")) {
            lightsPane.getStyleClass().remove("lights-pane-active");
            lightsPane.getStyleClass().add("lights-pane");
        }
        else lightsPane.getStyleClass().add("lights-pane-active");

        Parent root = FXMLLoader.load(getClass().getResource("lights.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Lights");
        stage.setScene(new Scene(root, 450, 450));
        stage.show();

    }

    public void openAudioWindow(MouseEvent mouseEvent) {
        System.out.println("Opening audio");
        if (!audioPane.getStyleClass().remove("audio-pane")) {
            audioPane.getStyleClass().remove("audio-pane-active");
            audioPane.getStyleClass().add("audio-pane");
        }
        else audioPane.getStyleClass().add("audio-pane-active");
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("radio.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("Radio");
        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }

    public void openEmergencyWindow(MouseEvent mouseEvent) {
        System.out.println("Emergency button!");
        if (!emergencyPane.getStyleClass().remove("emergency-pane")) {
            emergencyPane.getStyleClass().remove("emergency-pane-active");
            emergencyPane.getStyleClass().add("emergency-pane");
        }
        else emergencyPane.getStyleClass().add("emergency-pane-active");
    }
}
