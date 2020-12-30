package nightwatch;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class LightsController {
  @FXML private Slider slider1;
  @FXML private Slider slider2;
  @FXML private Slider slider3;
  @FXML private Slider slider4;
  private NightwatchController parentController;

  public void initialize() {
    slider1.setOnMouseDragged(e -> {
      System.out.println("value " + slider1.getValue());
      parentController.setLightsValue(1, slider1.getValue());
    });
    slider2.setOnMouseDragged(e -> {
      System.out.println("value " + slider2.getValue());
      parentController.setLightsValue(2, slider2.getValue());
    });
    slider3.setOnMouseDragged(e -> {
      System.out.println("value " + slider3.getValue());
      parentController.setLightsValue(3, slider3.getValue());
    });
    slider4.setOnMouseDragged(e -> {
      System.out.println("value " + slider4.getValue());
      parentController.setLightsValue(4, slider4.getValue());
    });
  }

  public void setParentController(NightwatchController controller) {
    parentController = controller;
  }

}