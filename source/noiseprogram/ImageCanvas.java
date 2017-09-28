package noiseprogram;

import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import javafx.animation.AnimationTimer;

import shophorn.Noise;
import shophorn.fields.*;

public class ImageCanvas extends Canvas
{
    int width = 600;
    int height = 600;


    private double frequency = 1.0;
    private int octaves = 1;
    private double gain = 0.5;
    private double lacunarity = 2.0;
    private double xOffset = 0.0;
    private double yOffset = 0.0;
    private double zOffset = 0.0;
    private double animationSpeed = 1.0;

    private NoisePatternType patternType = NoisePatternType.NONE;


    private double timePosition;
    private final double animationTimeScale = 0.01;
    private boolean isAnimating = false;
    private final AnimationTimer animationTimer;

    PixelWriter localWriter;

    public ImageCanvas ()
    {
        super();
        setWidth (width);
        setHeight (height);

        timePosition = 0;
        animationTimer = new AnimationTimer () {
            long last;
            boolean started = false;

            @Override
            public void handle (long now) {
                if (!started)
                {
                    started = true;
                    last = now;
                }

                if (!isAnimating)
                {
                    stop ();
                    started = false;
                }
                double delta = (now - last) / 1_000_000_000.0 * animationTimeScale;
                animate (delta);
                last = now;
            }
        };

        localWriter = getGraphicsContext2D().getPixelWriter();
        drawLocal ();
    }

    public void setNumericValue (ImageValue type, double value)
    {
        switch (type)
        {
            case FREQUENCY:
                frequency = value;
                break;
            case OCTAVES:
                octaves = (int)value;
                break;
            case GAIN:
                gain = value;
                break;
            case LACUNARITY:
                lacunarity = value;
                break;
            case X_OFFSET:
                xOffset = value;
                break;
            case Y_OFFSET:
                yOffset = value;
                break;
            case Z_OFFSET:
                zOffset = value;
                break;
            case ANIMATION_SPEED:
                animationSpeed = value;
                break;
        }
        drawLocal ();
    }

    public <E extends Enum<E>> void setEnumValue (ImageEnumValue type, E value)
    {
        switch (type)
        {
            case PATTERN_TYPE:
                patternType = (NoisePatternType) value;
                break;
        }
        drawLocal ();
    }

    private void drawLocal () { draw (localWriter); }

    private void draw (PixelWriter writer)
    {
        double hWidth = width / 2.0;
        double hHeight = height / 2.0;
        for (double y = 0; y < height; y++)
        {
            for (double x = 0; x < width; x++)
            {
                double sampleX = x / width - hWidth;
                double sampleY = y / height - hHeight;
                double sampleZ = timePosition;
                double value = 0.0;
                switch (patternType)
                {
                    case NONE:
                        value = Noise.value3D(sampleX, sampleY, sampleZ, frequency);
                        break;
                    case FRACTIONAL_BROWNIAN_MOTION:
                        value = Noise.fbm(sampleX, sampleY, sampleZ, frequency, octaves, gain, lacunarity);
                        break;
                    case DOMAIN_WARPING:
                        value = Noise.fPattern (sampleX, sampleY, sampleZ, frequency, octaves, gain, lacunarity);
                }
                writer.setColor((int)x, (int)y, new Color (value, value, value, 1));
            }
        }
    }

    public void startAnimation ()
    {
        isAnimating = true;
        animationTimer.start ();
    }

    public void stopAnimation ()
    {
        isAnimating = false;
    }

    private void animate (double deltaTime)
    {
        timePosition += deltaTime * animationSpeed;
        System.out.println ("Animating: " + timePosition);
        drawLocal ();
    }
}
