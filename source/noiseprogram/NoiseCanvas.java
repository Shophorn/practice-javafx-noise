package noiseprogram;

import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.animation.AnimationTimer;

import shophorn.*;

public class NoiseCanvas extends Canvas
{
    private GraphicsContext graphics;
    private PixelWriter canvasWriter;

    private int width;
    private int height;

    private NoiseType type = NoiseType.VALUE;
    private NoisePatternType patternType = NoisePatternType.NONE;

    private double frequency;
    private int octaves;
    private double gain;
    private double lacunarity;
    private double xOffset;
    private double yOffset;
    private double zOffset;

    private NoiseCanvasTimer timer;
    private boolean isAnimationPlaying = false;
    private double timeValue = 0.0;
    private double animationSpeed = 0.01;

    public NoiseCanvas (int width, int height)
    {
        super (width, height);
        this.width = width;
        this.height = height;
        graphics = getGraphicsContext2D ();
        canvasWriter = graphics.getPixelWriter ();
        timer = new NoiseCanvasTimer (this);
        drawOnCanvas ();
    }

    // Make sense of these numbers somehow
    private static final double toSeconds = (1000 * 1000 * 1000);
    private void animate (long now)
    {
        timeValue += animationSpeed * now / 10_000_000_000_000.0;//toSeconds / 10000.0;
        drawOnCanvas ();
    }

    public void startAnimation ()
    {
        timer.start();
        isAnimationPlaying = true;
    }

    public void stopAnimation ()
    {
        timer.stop ();
        isAnimationPlaying = false;
    }

    public <E extends Enum<E>> void setEnum (NoiseEnumValue valueType, E enumData)
    {
        switch (valueType)
        {
            case TYPE:
                type = (NoiseType)enumData;
                break;
            case PATTERN_TYPE:
                patternType = (NoisePatternType)enumData;
                break;
        }
        if (!isAnimationPlaying) {
            drawOnCanvas ();
        }
    }

    public void setValue (NoiseValue valueType, double value)
    {
        switch (valueType)
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
        if (!isAnimationPlaying) {
            drawOnCanvas ();
        }
    }

    private void drawOnCanvas ()
    {
        draw (canvasWriter);
    }

    private void draw (PixelWriter targetWriter)
    {
        // System.out.println ("drawOnCanvasn" + timeValue);
        double wHalf = width / 2.0;
        double hHalf = height / 2.0;

        StopWatch stopWatch = new StopWatch ();
        stopWatch.start();
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                double sampleX = ((double)w - wHalf) / width + xOffset;
                double sampleY = ((double)h - hHalf) / height + yOffset;
                double sampleZ = timeValue + zOffset;
                double value = 0.0;
                switch (patternType) {
                    case NONE:
                        value = Noise.value3D (sampleX, sampleY, sampleZ, frequency);
                        break;
                    case FRACTIONAL_BROWNIAN_MOTION:
                        value = Noise.fbm (sampleX, sampleY, sampleZ, frequency, octaves, gain, lacunarity);
                        break;
                    case DOMAIN_WARPING:
                        value = Noise.fPattern (sampleX, sampleY, sampleZ, frequency, octaves, gain, lacunarity);
                        break;
                }
                targetWriter.setColor (w, h, interpolateColor(value));
            }
        }
        stopWatch.stop ();
        System.out.println (stopWatch.getElapsedSeconds());
    }

    public WritableImage capture ()
    {
        WritableImage image = new WritableImage (width, height);
        draw (image.getPixelWriter ());
        return image;
    }

    private Color [] colors = {
        Color.WHITE,
        Color.GRAY,
        Color.BLACK
    };

    public void setColor (int index, Color color)
    {
        colors [index] = color;
        drawOnCanvas ();
    }

    private Color interpolateColor (double t)
    {
        t = Maths.smooth (t);

        double r0 = colors[0].getRed();
        double g0 = colors[0].getGreen ();
        double b0 = colors[0].getBlue ();

        double r1 = colors [1].getRed ();
        double g1 = colors [1].getGreen ();
        double b1 = colors [1].getBlue ();

        double r2 = colors [2].getRed ();
        double g2 = colors [2].getGreen ();
        double b2 = colors [2].getBlue ();

        Color result;
        if (t < 0.5) {
            result = new Color(
                Maths.lerp (r0, r1, 2 * t),
                Maths.lerp (g0, g1, 2 * t),
                Maths.lerp (b0, b1, 2 * t),
                1
            );
        } else {
            result = new Color (
                Maths.lerp (r1, r2, 2 * t - 1),
                Maths.lerp (g1, g2, 2 * t - 1),
                Maths.lerp (b1, b2, 2 * t - 1),
                1
            );
        }
        return result;
    }

    class NoiseCanvasTimer extends AnimationTimer
    {
        NoiseCanvas target;
        public NoiseCanvasTimer (NoiseCanvas target)
        {
            this.target = target;
        }

        @Override
        public void handle (long now)
        {
            target.animate (now);
        }
    }
}
