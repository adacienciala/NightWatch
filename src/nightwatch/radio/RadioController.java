package nightwatch.radio;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RadioController {
  @FXML private Label knob;
  @FXML private Label hertz;
  @FXML private Label sine;
  private double dragStart;
  private double volume = 0;
  private int value = 0;

  public void initialize() {
    knob.setOnMousePressed(e -> {
      dragStart = e.getScreenX();
    });
    knob.setOnMouseDragged(e -> {
      double delta = e.getScreenX() - dragStart;
      volume += delta / 300 / 15;
      if (volume > 0.8) volume = 0.8;
      if (volume < -0.8) volume = -0.8;
      value = (int)(140.0 * ((volume + 0.8) / 1.6)) + 10;
      int deg = (int)Math.floor(180 * volume);
      knob.setStyle("-fx-rotate: " + deg + ";");
      hertz.setText(value + "Hz");
    });

    Thread sineThread = new Thread(() -> {
      long i = 0;
      while (true) {
        long freq = (150 + 30 - value);
        String s = "-fx-background-position: " + (i++) + ";-fx-background-size: " + freq + " 100;";
        sine.setStyle(s);
        try {
          Thread.sleep(50);
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
      }
    });
    sineThread.setDaemon(true);
    sineThread.start();

  }
}
