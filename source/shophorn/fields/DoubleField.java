package shophorn.fields;

import javafx.scene.control.TextField;

import shophorn.Maths;

public class DoubleField extends TextField
{
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;

    public DoubleField (double initialValue)
    {
        super ();
        textProperty().addListener (
            (observableValue, oldValue, newValue) -> {
                try {
                    double value = Double.parseDouble(newValue);
                } catch (NumberFormatException e) {
                    setText(oldValue);
                }
            }
        );
        setValue (initialValue);
    }

    public DoubleField (double initialValue, double min, double max)
    {
        this (initialValue);
        this.min = min;
        this.max = max;
    }

    public void setValue (double value)
    {
        value = Maths.clamp (value, min, max);
        setText (Double.toString(value));
    }

    public double doubleValue ()
    {
        return Double.parseDouble (getText ());
    }
}
