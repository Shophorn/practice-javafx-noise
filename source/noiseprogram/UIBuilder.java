package noiseprogram;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import java.text.DecimalFormat;
import javafx.collections.*;
import javafx.beans.value.*;

import shophorn.fields.*;

public class UIBuilder
{
    NoiseCanvas noiseCanvas;
    GridPane grid;

    private int insetsSize = 5;
    private int labelColumn = 1;
    private int sliderColumn = 2;
    private int fieldColumn = 3;
    private int rowCounter = 0;
    private int getNextRow ()
    {
        return rowCounter++;
    }

    private int canvasSize = 600;

    public Scene build (Program program, Stage stage)
    {
        noiseCanvas = new NoiseCanvas (canvasSize, canvasSize);
        grid = new GridPane ();

        grid.add(noiseCanvas, 0, 0);
        GridPane.setRowSpan(noiseCanvas, 100);

        addEnumBoxRow ("Noise Pattern", NoisePatternType.class, NoiseEnumValue.PATTERN_TYPE);
        addDoubleSliderRow ("Frequency", 0.1, 100, 5, NoiseValue.FREQUENCY);
        addIntSliderRow ("Octaves", 1, 10, 5, NoiseValue.OCTAVES);
        addDoubleSliderRow ("Gain", 0.0, 1.0, 0.5, NoiseValue.GAIN);
        addDoubleSliderRow ("Lacunarity", 1.0, 3.0, 2.0, NoiseValue.LACUNARITY);
        addDoubleSliderRow ("X Offset", -100.0, 100.0, 0.0, NoiseValue.X_OFFSET);
        addDoubleSliderRow ("Y Offset", -100.0, 100.0, 0.0, NoiseValue.Y_OFFSET);
        addDoubleSliderRow ("Z Offset", -100.0, 100.0, 0.0, NoiseValue.Z_OFFSET);

        addAnimationControls ();
        addDoubleSliderRow ("Animation Speed", 0.0001, 0.001, 0.0002, NoiseValue.ANIMATION_SPEED);

        addColorPickerRow ("Color 1", Color.RED, 0);
        addColorPickerRow ("Color 2", Color.GREEN, 1);
        addColorPickerRow ("Color 3", Color.BLUE, 2);

        BorderPane border = new BorderPane ();
        border.setTop (buildMenuBar ());
        border.setCenter (grid);
        Scene scene = new Scene (border);
        return scene;
    }

    private void addIntSliderRow (String label, int min, int max, int defaultValue, NoiseValue type)
    {
        Label nameLabel = new Label(label);
        nameLabel.setPadding(new Insets (insetsSize));

        IntegerField field = new IntegerField (defaultValue, min, max);
        Slider slider = new Slider (min, max, defaultValue);

        field.setOnAction (
            (event) -> {
                slider.setValue (field.intValue());
            }
        );

        slider.valueProperty ().addListener(
            (observableValue, oldValue, newValue) -> {
                int value = newValue.intValue();
                slider.setValue (value);
                field.setText(Integer.toString(value));
                noiseCanvas.setValue(type, value);
            }
        );
        field.setText (Integer.toString(defaultValue));
        noiseCanvas.setValue(type, defaultValue);

        int row = getNextRow ();
        grid.add(nameLabel, labelColumn, row);
        grid.add(slider, sliderColumn, row);
        grid.add(field, fieldColumn, row);
    }

    private void addDoubleSliderRow (String label, double min, double max, double defaultValue, NoiseValue type)
    {
        Label nameLabel = new Label (label);
        nameLabel.setPadding (new Insets(insetsSize));

        DoubleField field = new DoubleField (defaultValue);
        Slider slider = new Slider (min, max, defaultValue);

        field.setOnAction (
            (event) -> {
                slider.setValue (field.doubleValue ());
            }
        );

        slider.valueProperty ().addListener (
            (observableValue, oldValue, newValue) -> {
                double value = newValue.doubleValue ();
                field.setValue (value);
                noiseCanvas.setValue (type, value);
            }
        );
        noiseCanvas.setValue (type, defaultValue);

        int currentRow = getNextRow ();
        grid.add (nameLabel, labelColumn, currentRow);
        grid.add (slider, sliderColumn, currentRow);
        grid.add (field, fieldColumn, currentRow);
    }

    private void addColorPickerRow (String label, Color initialColor, int index)
    {
        Label nameLabel = new Label (label);
        nameLabel.setPadding (new Insets (insetsSize));

        ColorPicker picker = new ColorPicker (initialColor);
        picker.setOnAction(
            (event) -> {
                noiseCanvas.setColor (index, picker.getValue());
            }
        );
        noiseCanvas.setColor(index, initialColor);

        int row = getNextRow ();
        grid.add(nameLabel, labelColumn, row);
        grid.add(picker, sliderColumn, row);
    }

    private <E extends Enum <E>> void addEnumBoxRow (String label, Class <E> enumData, NoiseEnumValue valueType)
    {
        Label nameLabel = new Label (label);
        nameLabel.setPadding (new Insets (insetsSize));

        ComboBox box = new ComboBox<E> (
            FXCollections.observableArrayList (
                enumData.getEnumConstants()
            )
        );
        box.valueProperty().addListener (
            new ChangeListener <E> () {
                @Override
                public void changed (ObservableValue <? extends E> observableValue, E oldValue, E newValue)
                {
                    noiseCanvas.setEnum (valueType, newValue);
                }
            }
        );

        int row = getNextRow ();
        grid.add (nameLabel, labelColumn, row);
        grid.add (box, sliderColumn, row);
    }

    private void addAnimationControls()
    {
        Label animationLabel = new Label ("Animation");
        animationLabel.setPadding (new Insets (insetsSize));
        Button startButton = new Button ("Start");
        startButton.setOnAction (
            (event) -> {
                noiseCanvas.startAnimation ();
            }
        );
        Button stopButton = new Button ("Stop");
        stopButton.setOnAction (
            (event) -> {
                    noiseCanvas.stopAnimation ();
            }
        );
        int row = getNextRow ();
        grid.add (animationLabel, labelColumn, row);
        grid.add (startButton, sliderColumn, row);
        grid.add (stopButton, fieldColumn, row);
    }

    private MenuBar buildMenuBar ()
    {
        Menu fileMenu = new Menu ("File");
        MenuItem saveItem = new MenuItem ("Save as Image");
        saveItem.setOnAction (
            (event) -> Program.saveToFile (noiseCanvas.capture ())
        );
        MenuItem exitItem = new MenuItem ("Exit");
        exitItem.setOnAction (
            (event)-> System.exit (0)
        );
        fileMenu.getItems ().addAll (saveItem, exitItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus ().add (fileMenu);
        return menuBar;
    }
}
