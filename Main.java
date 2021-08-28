/**
 * Creates a window that features a calendar and a clock. The calendar
 * is interactive and can contain events for each day on the calendar.
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */

/**
 * Main tester class for the calendar program
 */
public class Main {
    /**
     * Creates CalendarDataModel and CalendarView objects
     * @param args String args
     */
    public static void main(String[] args) {
	    CalendarDataModel calendarDataModel = new CalendarDataModel();
	    CalendarView calendarView = new CalendarView(calendarDataModel);
	    calendarView.displayView();
    }
}
