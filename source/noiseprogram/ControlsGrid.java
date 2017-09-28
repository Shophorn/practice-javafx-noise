package noiseprogram;

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

public class ControlsGrid extends GridPane
{
    private ImageCanvas image;

    public ControlsGrid (ImageCanvas image)
    {
        super ();
        this.image = image;
        build ();
    }

    private int row = 0;
    private int getRow () { return row++; }
    private void build ()
    {
        addEnumDropdownRow ("Pattern Type", NoisePatternType.class, ImageEnumValue.PATTERN_TYPE);
        addDoubleSliderRow ("Frequency", 0.1, 40.0, 10.0, ImageValue.FREQUENCY);
        addIntSliderRow ("Octaves", 1, 10, 3, ImageValue.OCTAVES);
        addDoubleSliderRow ("Gain", 0.0, 1.0, 0.5, ImageValue.GAIN);
        addDoubleSliderRow ("Lacunarity", 1.0, 3.0, 2.0, ImageValue.LACUNARITY);
        addDoubleSliderRow ("X Offset", -100.0, 100.0, 0.0, ImageValue.X_OFFSET);
        addDoubleSliderRow ("Y Offset", -100.0, 100.0, 0.0, ImageValue.Y_OFFSET);
        addDoubleSliderRow ("Z Offset", -100.0, 100.0, 0.0, ImageValue.Z_OFFSET);

        addAnimationControls ();
        addDoubleSliderRow ("Animation Speed", 1.0, 0.0, 100.0, ImageValue.ANIMATION_SPEED);

    }

    private final Insets labelPadding = new Insets (5);

    private void addIntSliderRow (String labelText, int minValue, int maxValue, int defaultValue, ImageValue type)
    {
        Label label = new Label (labelText);
        label.setPadding (labelPadding);

        Slider slider = new Slider (minValue, maxValue, defaultValue);
        IntegerField field = new IntegerField (defaultValue, minValue, maxValue);

        slider.valueProperty ().addListener (
            (observableValue, oldValue, newValue) -> {
                int value = newValue.intValue ();
                slider.setValue (value);
                field.setValue (value);
                image.setNumericValue (type, value);
            }
        );

        field.setOnAction (
            (event) -> slider.setValue(field.intValue ())
        );

        slider.setValue (defaultValue);
        image.setNumericValue (type, defaultValue);

        addRow (getRow (), label, slider, field);
    }

    private void addDoubleSliderRow (String labelText, double minValue, double maxValue, double defaultValue, ImageValue type)
    {
        Label label = new Label (labelText);
        label.setPadding (labelPadding);

        Slider slider = new Slider (minValue, maxValue, defaultValue);
        DoubleField field = new DoubleField (defaultValue, minValue, maxValue);

        slider.valueProperty ().addListener (
            (observableValue, oldValue, newValue) -> {
                double value = newValue.doubleValue ();
                field.setValue (value);
                image.setNumericValue (type, value);
            }
        );

        field.setOnAction (
            (event) -> slider.setValue (field.doubleValue ())
        );

        slider.setValue (defaultValue);
        image.setNumericValue (type, defaultValue);

        addRow (getRow (), label, slider, field);
    }

    private void addAnimationControls ()
    {
        Label label = new Label ("Animation");
        label.setPadding (labelPadding);

        Button startButton = new Button ("Start");
        startButton.setOnAction (
            (event) -> image.startAnimation ()
        );

        Button stopButton = new Button ("Stop");
        stopButton.setOnAction (
            (event) -> image.stopAnimation ()
        );

        addRow (getRow (), label, startButton, stopButton);
    }

    private <E extends Enum<E>> void addEnumDropdownRow (String labelText, Class<E> enumData, ImageEnumValue type)
    {
        Label label = new Label (labelText);
        label.setPadding (labelPadding);

        ComboBox<E> dropdown = new ComboBox <E>(
            FXCollections.observableArrayList (
                enumData.getEnumConstants ()
            )
        );
        dropdown.valueProperty ().addListener (
            (observableValue, oldValue, newValue)-> image.setEnumValue(type, newValue)
        );
        dropdown.getSelectionModel().selectFirst();
        addRow (getRow (), label, dropdown);
    }
}
