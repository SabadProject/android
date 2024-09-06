package farayan.commons;

/**
 * Created by Homayoun on 30/05/2017.
 */

public class Time {
    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public int getMinute() {
        return Minute;
    }

    public void setMinute(int minute) {
        Minute = minute;
    }

    public int getSecond() {
        return Second;
    }

    public void setSecond(int second) {
        Second = second;
    }

    private int Hour;
    private int Minute;
    private int Second;

    @Override
    public String toString() {
        return FarayanUtility.LeadingZero(Hour, 2) + ":" + FarayanUtility.LeadingZero(Minute, 2) + ":" + FarayanUtility.LeadingZero(Second, 2);
    }
}
