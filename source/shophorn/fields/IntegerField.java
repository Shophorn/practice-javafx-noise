package shophorn.fields;

import javafx.scene.control.TextField;
// import javafx.event.*;

import shophorn.Maths;

public class IntegerField extends TextField
{
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public IntegerField (int initialValue)
    {
        super ();
        textProperty().addListener (
            (observableValue, oldValue, newValue) -> {
                try {
                    int value = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    setText (oldValue);
                }
            }
        );
        setValue (initialValue);
    }

    public IntegerField (int initialValue, int min, int max)
    {
        this (initialValue);
        this.min = min;
        this.max = max;
    }

    public void setValue (int value)
    {
        value = Maths.clamp(value, min, max);
        setText (Integer.toString (value));
    }

    public int intValue ()
    {
        return Integer.parseInt(getText ());
    }
}
