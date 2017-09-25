package shophorn;

public class Maths
{
    public static double lerp(double a, double b, double t)
    {
        t = clamp01 (t);
        return (1 - t) * a + t * b;
    }

    public static double smooth (double t)
    {
        // 6v5 - 15v4 + 10v3
        t = t * t * t * (t * (6 * t - 15) + 10);
        t = clamp01 (t);
        return t;
    }

    public static int clamp (int value, int min, int max)
    {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }
        return value;
    }

    public static double clamp (double value, double min, double max)
    {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }
        return value;
    }

    public static double clamp01 (double value)
    {
        if (value > 1.0) {
            value = 1.0;
        } else if (value < 0.0) {
            value = 0.0;
        }
        return value;
    }
}
