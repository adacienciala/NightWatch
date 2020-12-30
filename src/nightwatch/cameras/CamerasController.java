package nightwatch.cameras;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;

public class CamerasController {

    public Pane screen;
    private File cameraFootage;
    private MediaPlayer player;

    public void setCameraFootage(File cameraFootage) throws MalformedURLException {
        this.cameraFootage = cameraFootage;
        Media media = new Media(cameraFootage.toURI().toURL().toString());
        player = new MediaPlayer(media);
        MediaView viewer = new MediaView(player);
        viewer.fitWidthProperty().bind(screen.widthProperty());
        screen.getChildren().add(viewer);
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
            player.play();
        });
        player.play();
        System.out.println(cameraFootage);
    }

    public void setVolume(double value) {
        this.player.setVolume(value);
    }

}
