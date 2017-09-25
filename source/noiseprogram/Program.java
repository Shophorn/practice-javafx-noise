package noiseprogram;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;

import java.awt.image.*;
import javafx.embed.swing.SwingFXUtils;
import java.util.logging.*;

import javafx.scene.image.*;
import java.io.*;
import javax.imageio.*;

public class Program extends Application
{
    private static Stage stage;
    public void start (Stage stage)
    {
        Scene scene = new UIBuilder().build (this, stage);

        stage.setScene (scene);
        stage.setTitle ("NoiseX 9000");
        stage.show();
    }

    public static void saveToFile (WritableImage image)
    {
        /*
        Research:
        File
        FileChoose for saving

        ImageIO

        canvas.snapshot
        writableImage
        renderedImage

        */
        /// GET TO UNDERSTAND THIS
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try
            {
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
