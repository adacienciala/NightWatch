package nightwatch.radio;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nightwatch.NightwatchController;

import java.nio.file.Paths;

public class RadioController {
  @FXML private Label knob;
  @FXML private Label hertz;
  @FXML private Label sine;
  private double dragStart;
  private double volume = 0;
  private int value = 0;
  private NightwatchController parentController;

  private final int[] frequencies = {30, 60, 90, 120};

  private static final String soundFilename = "src/nightwatch/radio/resources/static.mp3";
  private static final Media sound = new Media(Paths.get(soundFilename).toUri().toString());
  private static final MediaPlayer staticMedia = new MediaPlayer(sound);

  private void setVolumes() {
    double maxStatic = 0.5f;
    for (int i = 0; i < frequencies.length; i++) {
      int distance = Math.abs(frequencies[i] - value);
      if (distance <= 9) {
        double volumeLevel = 1.0f / ((double)distance + 1.0f);
        double staticLevel = (1.0f - volumeLevel) / 2.0f;
        staticMedia.setVolume(staticLevel);
        parentController.setVolumeValue(i, volumeLevel);
        if (staticLevel < maxStatic) {
          maxStatic = staticLevel;
        }
      } else {
        parentController.setVolumeValue(i, 0.0f);
      }
    }
    staticMedia.setVolume(maxStatic);
  }

  public void initialize() {
    new Thread(() -> {
      try {
        Thread.sleep(100);
        knob.getScene().setOnKeyPressed(e -> {
          if (e.getCode().toString().equals("RIGHT")) {
            value++;
            if (value > 150) value = 150;
          } else if (e.getCode().toString().equals("LEFT")) {
            value--;
            if (value < 10) value = 10;
          }
          hertz.setText(value + "Hz");
          setVolumes();
        });
      } catch (Exception ignored) {}
    }).start();
    staticMedia.setVolume(0.5f);
    staticMedia.setOnEndOfMedia(() -> {
      staticMedia.seek(Duration.ZERO);
      staticMedia.play();
    });
    staticMedia.play();
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
      setVolumes();
    });

    Thread sineThread = new Thread(() -> {
      long i = 0;
      while (true) {
        long freq = (150 + 30 - value);
        String s = "-fx-background-position: " + (i++) + ";-fx-background-size: " + freq + " 100;";
        Platform.runLater(() -> {
          sine.setStyle(s);
        });
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

  public void setParentController(NightwatchController controller) {
    parentController = controller;
  }

}
