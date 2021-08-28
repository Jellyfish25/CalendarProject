/**
 * Event class that creates event objects and verifies
 * if any events are overlapping using the Comparable interface
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Creates an event object that consists of a name, date and time.
 */
public class Event implements Comparable {
    private final String name_;
    private final TimeInterval timeInterval_;
    private ArrayList<LocalDate> dates_;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final String DAY_ABBREVS = " MTWHFAS";

    /**
     * Constructor for class Event
     * @param name The event's name
     * @param date The event's date
     * @param startHour The event's starting hour
     * @param endHour The event's ending hour
     */
    public Event(String name, LocalDate date, int startHour, int endHour) { //one-time events
        name_ = name;
        timeInterval_ = new TimeInterval(startHour, endHour);
        dates_ = new ArrayList<>();
        dates_.add(date);
    }

    /**
     * Constructor for class Event that takes a file line
     * @param fileLine A line from a text file
     */
    public Event(String fileLine) { //recurring events
        Scanner scanner = new Scanner(fileLine);
        scanner.useDelimiter(";");
        name_ = scanner.next();
        int year = Integer.parseInt(scanner.next());
        int startMonth = Integer.parseInt(scanner.next());
        int endMonth = Integer.parseInt(scanner.next());
        String recurringDays = scanner.next();
        int startHour = Integer.parseInt(scanner.next());
        int endHour = Integer.parseInt(scanner.next());
        timeInterval_ = new TimeInterval(startHour, endHour);

        //getting values of recurring days of the week
        ArrayList<Integer> recurringDayValues = new ArrayList<>();
        char[] recurringDaysArray = recurringDays.toCharArray();
        for (char day : recurringDaysArray) {
            recurringDayValues.add(DAY_ABBREVS.indexOf(day));
        }

        //calculating and adding dates the event recurs
        dates_ = new ArrayList<>();
        LocalDate iteratorDate = LocalDate.of(year, startMonth, 1);
        LocalDate endDate = LocalDate.of(year, endMonth, 1);
        while (!iteratorDate.isEqual(endDate)) {
            if (recurringDayValues.contains(iteratorDate.getDayOfWeek().getValue())) {
                dates_.add(iteratorDate);
            }
            iteratorDate = iteratorDate.plusDays(1);
        }
        Collections.sort(dates_);
    }

    /**
     * Checks if the event conflicts with another event
     * @param other The other event being compared to
     * @return a true/false boolean
     */
    public boolean conflictsWith(Event other) {
        boolean datesIntersect = false;
        for (LocalDate date : other.getDates_()) {
            if (dates_.contains(date)) {
                datesIntersect = true;
                break;
            }
        }
        return timeInterval_.overLapsWith(other.getTimeInterval_()) && datesIntersect;
    }

    /**
     * Gets the event's name
     * @return The event's name
     */
    public String getName_() {
        return name_;
    }

    /**
     * Gets the event's time interval
     * @return The event's time interval
     */
    public TimeInterval getTimeInterval_() {
        return timeInterval_;
    }

    /**
     * Gets the list of dates
     * @return list of type LocalDate
     */
    public ArrayList<LocalDate> getDates_() {
        return dates_;
    }

    /**
     * Converts the event to string
     * @return The event in the form of a string
     */
    @Override
    public String toString() {
        return name_ + ": " + timeInterval_;
    }

    /**
     * Compares two events
     * @param o The other object
     * @return an integer based on the two events being compared
     */
    @Override
    public int compareTo(Object o) {
        Event other = (Event) o;
        if (dates_.get(0).isBefore(other.getDates_().get(0))) {
            return -1;
        }
        else if (dates_.get(0).isAfter(other.getDates_().get(0))) {
            return 1;
        }
        else {
            return timeInterval_.compareTo(other.getTimeInterval_());
        }
    }
}
