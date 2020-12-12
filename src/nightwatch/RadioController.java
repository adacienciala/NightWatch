package nightwatch;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RadioController {
  @FXML private Label knob;
  private double dragStart;
  private double volume = 0;

  public void initialize() {
    System.out.println("Radio controller initialized");
    knob.setOnMousePressed(e -> {
      dragStart = e.getScreenX();
    });
    knob.setOnMouseDragged(e -> {
      double delta = e.getScreenX() - dragStart;
      volume += delta / 300 / 15;
      if (volume > 0.8) volume = 0.8;
      if (volume < -0.8) volume = -0.8;
      int deg = (int)Math.floor(180 * volume);
      knob.setStyle("-fx-rotate: " + deg + ";");
    });
  }
}
