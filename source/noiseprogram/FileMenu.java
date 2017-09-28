package noiseprogram;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu extends Menu
{
    public FileMenu ()
    {
        super ("File");

        MenuItem exportImageItem = new MenuItem ("Export Image");
        MenuItem exportAnimationItem = new MenuItem ("Export Animation");

        MenuItem exitItem = new MenuItem ("Exit");
        exitItem.setOnAction ((event)-> Program.exit(0));

        getItems().addAll(
            exportImageItem,
            exportAnimationItem,
            exitItem
        );
    }



}
