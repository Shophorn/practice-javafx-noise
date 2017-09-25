package shophorn;

public class StopWatch
{
    private long startTime = 0;
    private long endTime = 0;
    private boolean running = false;

    public StopWatch () { }

    public long getElapsedTime ()
    {
        return running ? System.nanoTime () - startTime : endTime - startTime;
    }

    public double getElapsedSeconds ()
    {
        return getElapsedTime () / 1_000_000_000.0;
    }

    public void start ()
    {
        startTime = System.nanoTime();
        running = true;
    }

    public void stop ()
    {
        endTime = System.nanoTime();
        running = false;
    }
}
