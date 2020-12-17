package nightwatch.radio;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RadioController {
  @FXML private Label knob;
  @FXML private Label hertz;
  private double dragStart;
  private double volume = 0;
  private int value = 0;

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
      double shifted = volume + 0.8;
      double percent = shifted / 1.6;
      value = (int)(150.0 * percent);
      int deg = (int)Math.floor(180 * volume);
      knob.setStyle("-fx-rotate: " + deg + ";");
      hertz.setText(value + "Hz");
    });
  }
}
