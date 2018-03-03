package util;

import static java.lang.String.format;

/**
 * Created by Xulai on 3/2/2018.
 */

public class Record {
    private String name;
    private int steps;
    private String timeString;
    private long time;

    public Record(int steps, long totalTime){
        this.name = "anonymous";
        this.steps = steps;
        this.time = totalTime / 10;

        int seconds = (int) (time / 100);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int) (time % 100);
        this.timeString = Integer.toString(minutes) + ":" + format("%02d", seconds) + "." + format("%02d", milliseconds);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public String getStepsString() {
        return String.valueOf(steps);
    }

    public long getTime() {
        return time;
    }

    public String getTimeString() {
        return timeString;
    }
}
