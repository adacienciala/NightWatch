package nightwatch;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class LightsController {
  @FXML private Slider slider1;
  @FXML private Slider slider2;
  @FXML private Slider slider3;
  @FXML private Slider slider4;
  private NightwatchController parentController;

  private void setValue(int room, double val) {
    if (val < 10.0f) {
      val = 10.0f;
    }
    parentController.setLightsValue(room, val);
  }

  public void initialize() {
    final Slider[] sliders = {slider1, slider2, slider3, slider4};
    for (int i = 0; i < sliders.length; i++) {
      sliders[i].setValue(100.0f);
      final int idx = i;
      sliders[i].setOnMouseDragged(e -> {
        setValue(idx, sliders[idx].getValue());
      });
    }
    new Thread(() -> {
      NightwatchController.sleepFor(100);
      slider1.getScene().getWindow().setOnCloseRequest(e -> {
        NightwatchController.playSound("lights close");
      });
    }).start();
  }

  public void setParentController(NightwatchController controller) {
    parentController = controller;
  }

}