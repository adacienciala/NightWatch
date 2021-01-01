package nightwatch.door;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import nightwatch.NightwatchController;

import java.io.IOException;

public class DoorController {
    @FXML
    public AnchorPane rootPane;
    @FXML
    private Label codeLabel;

    @FXML
    public void clearCode(ActionEvent event) {
        NightwatchController.playSound("button click 1");
        codeLabel.setText("");
    }

    @FXML
    public void digitInput(ActionEvent event) {
        Button b = (Button)event.getSource();
        String currPin = codeLabel.getText();
        if (currPin.length()<4) {
            codeLabel.setText(currPin+b.getText());
            NightwatchController.playSound("button click 1");
        }
    }

    @FXML
    public void verifyAccess(ActionEvent event) throws InterruptedException {
        String pin = codeLabel.getText();
        if (pin.equals("1111")) {
            try {
                NightwatchController.playSound("access granted");
                Thread.sleep(200);
            } catch (Exception ignore) {}
            NightwatchController.playSound("door open");
            codeLabel.setText("OK");
            fadeOut();
        }
        else {
            NightwatchController.playSound("access denied");
            codeLabel.setText("");
        }
    }

    public void fadeOut() throws InterruptedException {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(2000));
        transition.setNode(rootPane);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setOnFinished((e) -> loadCockpitScene());
        transition.play();
    }

    public void loadCockpitScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("../nightwatch.fxml"));
            Parent cockpitRoot = loader.load();
            Scene cockpitScene = new Scene(cockpitRoot);
            Stage currStage = (Stage)rootPane.getScene().getWindow();
            currStage.setScene(cockpitScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}