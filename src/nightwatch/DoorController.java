package nightwatch;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DoorController {
    @FXML
    public AnchorPane rootPane;
    @FXML
    private Label codeLabel;
    @FXML
    private Button approveBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    public void clearCode(ActionEvent event) {
        codeLabel.setText("");
    }

    @FXML
    public void digitInput(ActionEvent event) {
        Button b = (Button)event.getSource();
        String currPin = codeLabel.getText();
        if (currPin.length()<4) {
            codeLabel.setText(currPin+b.getText());
        }
    }

    @FXML
    public void verifyAccess(ActionEvent event) throws InterruptedException {
        String pin = codeLabel.getText();
        if (pin.equals("1111")) {
            codeLabel.setText("OK");
            Thread.sleep(1000);
            fadeOut();
        }
        else {
            codeLabel.setText("XXXX");
            Thread.sleep(1000);
            codeLabel.setText("");
        }
    }

    public void fadeOut() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(1000));
        transition.setNode(rootPane);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setOnFinished((e) -> loadCockpitScene());
        transition.play();
    }

    public void loadCockpitScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("nightwatch.fxml"));
            Parent cockpitRoot = loader.load();
            Scene cockpitScene = new Scene(cockpitRoot);
            Stage currStage = (Stage)rootPane.getScene().getWindow();
            currStage.setScene(cockpitScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}