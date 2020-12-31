package nightwatch.cameras;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;

public class CamerasController {

    public Pane screen;
    private File cameraFootage;
    private MediaView viewer;

    public void setCameraFootage(File cameraFootage) throws MalformedURLException {
        this.cameraFootage = cameraFootage;
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
        this.viewer.setOpacity(value);
    }

}
