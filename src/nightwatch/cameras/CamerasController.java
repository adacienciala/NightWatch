package nightwatch.cameras;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import nightwatch.NightwatchController;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamerasController {

    public Pane screen;
    public Label dataLabel;
    public Label camLabel;
    private MediaView viewer;
    private final Scale newScale = new Scale();
    private boolean opened = false;
    private String localization = "";

    public void initialize() {
        new Thread(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd EEE \nHH:mm:ss");
            while (true) {
                NightwatchController.sleepFor(1000);
                final String time = simpleDateFormat.format(new Date());
                Platform.runLater(() -> {
                    dataLabel.setText(time);
                    camLabel.setText("CAM-" + localization);
                });
            }
        }).start();
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void setCameraFootage(File cameraFootage, String localization) throws MalformedURLException {
        this.localization = localization;
        Media media = new Media(cameraFootage.toURI().toURL().toString());
        MediaPlayer player = new MediaPlayer(media);
        viewer = new MediaView(player);
        viewer.fitWidthProperty().bind(screen.widthProperty());
        viewer.fitHeightProperty().bind(screen.heightProperty());
        screen.getChildren().add(viewer);
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
            player.play();
        });
        player.play();
        setVolume(0);
        System.out.println(cameraFootage);
    }

    public void setVolume(double value) {
        this.viewer.getMediaPlayer().setVolume(value);
    }

    public void setOpacity(double value) {
        if (this.localization.compareTo("Main Deposit") != 0) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-1*(1-value));
            this.viewer.setEffect(colorAdjust);
        }
    }

    public void zoomCam(ScrollEvent event) {
        screen.getTransforms().clear();
        double zoomFactor = event.getDeltaY() < 0 ? -0.2 : 0.2;
        newScale.setX(newScale.getX() + zoomFactor);
        newScale.setY(newScale.getY() + zoomFactor);
        if (newScale.getX() < 1) {
            newScale.setX(1);
            newScale.setY(1);
        }
        if (newScale.getX() > 3) {
            newScale.setX(3);
            newScale.setY(3);
        }
        newScale.setPivotX(event.getX());
        newScale.setPivotY(event.getY());
        screen.getTransforms().add(newScale);
    }
}
