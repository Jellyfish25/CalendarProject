/**
 * TimeInterval class implements the Comparable interface to verify
 * if any times overlap, while also creating a time interval of two given
 * start and end times.
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */

/**
 * Creates a time interval object that consists of a starting time and ending time
 */
public class TimeInterval implements Comparable {
    private final int startTime_;
    private final int endTime_;

    /**
     * Constructor for class TimeInterval
     * @param startHour The starting hour of the interval
     * @param endHour The ending hour of the interval
     */
    public TimeInterval(int startHour, int endHour) {
        startTime_ = startHour;
        endTime_ = endHour;
    }

    /**
     * Checks if a time interval overlaps with another time interval
     * @param other The other time interval
     * @return True/False boolean
     */
    public boolean overLapsWith(TimeInterval other) {
        return (endTime_ == other.getEndTime_()) || (endTime_ > other.getStartTime_() && endTime_ < other.getEndTime_())
                || (other.getEndTime_() > startTime_ && other.getEndTime_() < endTime_);
    }

    /**
     * Gets the starting time of the interval
     * @return The start time
     */
    public int getStartTime_() {
        return startTime_;
    }

    /**
     * Gets the ending time of the interval
     * @return The end time
     */
    public int getEndTime_() {
        return endTime_;
    }

    /**
     * Converts the time interval to a string
     * @return a time interval string
     */
    @Override
    public String toString() {
        return startTime_ + ":00-" + endTime_ + ":00";
    }

    /**
     * Compares two time intervals
     * @param o The other time interval
     * @return an integer
     */
    @Override
    public int compareTo(Object o) {
        TimeInterval other = (TimeInterval) o;
        return startTime_ - other.getStartTime_();
    }
}
