/**
 * Created by Brandon on 11/12/2017.
 */
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Play_Audio extends Application {

    public static void main(String[] args) throws Exception {
    launch(args);
	System.out.println("Playing audio.");

    }

    @Override
    public void start(Stage stage)
    {
        File songFile = new File("Song File.mp3");
        Media musicFile = new Media(songFile.toURI().toString());
        MediaPlayer player = new MediaPlayer(musicFile);
        player.play();

        stage.show();
    }


}