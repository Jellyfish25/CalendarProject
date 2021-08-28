/**
 * The calendar's data structure, which has array list containers for all the events
 * and the change listeners for the mvc architecture. This class also features
 * mutators that modify the calendar's date (model of the MVC architecture)
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The calendar's data model class that initializes and modifies the calendar's date
 */
public class CalendarDataModel {
    private final ArrayList<Event> events_; //all events in the user's calendar
    private final ArrayList<ChangeListener> changeListeners_; //for the mvc architecture

    private LocalDate date_; // the date occurring during the day/week/month the user wants to view
    private String view_; // the type of view the user wants: day, week, month, or agenda
    private LocalDate startAgendaDate_;
    private LocalDate endAgendaDate_;
    private ColorMode colorMode;

    /**
     * Constructor for class CalendarDataModel
     */
    public CalendarDataModel() {
        events_ = new ArrayList<>();
        changeListeners_ = new ArrayList<>();
        date_ = LocalDate.now();
        view_ = "day";
        colorMode = new LightMode();
    }

    /**
     * Gets the event list
     * @return an array list of events
     */
    public ArrayList<Event> getEvents() {
        return events_;
    }

    /**
     * Adds an event to the event list
     * @param event an event consisting of a description and time interval
     */
    public void addEvent(Event event) {
        for (Event e : events_) {
            if (e.conflictsWith(event)) {
                JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "Error: cannot add " + event.getName_() + " because it " +
                        "conflicts with " + e.getName_() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        events_.add(event);
        Collections.sort(events_);
        notifyView();
    }

    /**
     * Adds recurring events to the event list
     * @param filename the recurring event's file
     */
    public void addRecurringEvents(String filename) {
        File file = new File(filename);
        try {
            Scanner scanner = new Scanner(file);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                Event newEvent = new Event(line);
                addEvent(newEvent);
            }
            notifyView();
        }
        catch (FileNotFoundException exception) {
            JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Error: invalid file name.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a change listener to the change listener list
     * @param listener a change listener
     */
    public void addChangeListener(ChangeListener listener) {
        changeListeners_.add(listener);
    }

    /**
     * Gets the current date set to the calendar
     * @return a local date
     */
    public LocalDate getDate() {
        return date_;
    }

    /**
     * Sets the date to the calendar
     * @param date a local date
     */
    public void setDate(LocalDate date) {
        date_ = date;
        notifyView();
    }

    /**
     * Moves the date forward by one day and notifies the viewer
     */
    public void nextDay() {
        date_ = date_.plusDays(1);
        notifyView();
    }

    /**
     * Moves the date back by one day and notifies the viewer
     */
    public void previousDay() {
        date_ = date_.minusDays(1);
        notifyView();
    }

    /**
     * Moves the date forward by one week and notifies the viewer
     */
    public void nextWeek() {
        date_ = date_.plusWeeks(1);
        notifyView();
    }

    /**
     * Moves the date back by one week and notifies the viewer
     */
    public void previousWeek() {
        date_ = date_.minusWeeks(1);
        notifyView();
    }

    /**
     * Moves the date forward by one month and notifies the viewer
     */
    public void nextMonth() {
        date_ = date_.plusMonths(1);
        notifyView();
    }

    /**
     * Moves the date back by one month and notifies the viewer
     */
    public void previousMonth() {
        date_ = date_.minusMonths(1);
        notifyView();
    }

    /**
     * Gets the view type
     * @return the view type
     */
    public String getView() {
        return view_;
    }

    /**
     * Sets the view type and notifies the viewer
     * @param view a type of view
     */
    public void setView(String view) {
        view_ = view;
        notifyView();
    }

    /**
     * Sets the agenda view to a start and end date and notifies the viewer
     * @param startDate The start date of the agenda
     * @param endDate The end date of the agenda
     */
    public void setAgendaView(LocalDate startDate, LocalDate endDate) {
        view_ = "agenda";
        startAgendaDate_ = startDate;
        endAgendaDate_ = endDate;
        notifyView();
    }

    /**
     * Gets the color mode
     * @return the color mode
     */
    public ColorMode getColorMode() {
        return colorMode;
    }

    /**
     * Sets the color of the text area and notifies the view
     * @param colorMode the color mode being set
     */
    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
        notifyView();
    }

    /**
     * Gets all the events for the text area utilizing getEventsInDay method
     * @return a string of events
     */
    public String getTextAreaContent() {
        StringBuilder textAreaContent = new StringBuilder();
        if (view_.equals("day")) {
            textAreaContent = new StringBuilder(getEventsInDay(date_));
        }
        else if (view_.equals("week")) {
            LocalDate dateIterator = date_;
            while(!dateIterator.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                dateIterator = dateIterator.minusDays(1);
            }

            for (int i = 0; i < 7; i++) {
                textAreaContent.append(getEventsInDay(dateIterator));
                dateIterator = dateIterator.plusDays(1);
            }

        }
        else if (view_.equals("month")) {
            LocalDate dateIterator = date_;
            dateIterator = dateIterator.withDayOfMonth(1);
            while(dateIterator.getMonthValue() == date_.getMonthValue()) {
                textAreaContent.append(getEventsInDay(dateIterator));
                dateIterator = dateIterator.plusDays(1);
            }

        }
        else if (view_.equals("agenda")) {
            LocalDate dateIterator = startAgendaDate_;
            while (!dateIterator.isAfter(endAgendaDate_)) {
                textAreaContent.append(getEventsInDay(dateIterator));
                dateIterator = dateIterator.plusDays(1);
            }
        }
        else {
            System.out.println("Error: invalid view type."); //for debugging purposes
        }
        return textAreaContent.toString();
    }

    /**
     * Notifies the viewer (state changed)
     */
    private void notifyView() {
        ChangeEvent changeEvent = new ChangeEvent(this);
        for (ChangeListener listener : changeListeners_) {
            listener.stateChanged(changeEvent);
        }
    }

    /**
     * Gets the events for a specified day
     * @param date a specified date
     * @return String of events
     */
    private String getEventsInDay(LocalDate date) {
        ArrayList<Event> eventsForDay = new ArrayList<>();
        String dayEvents = date + "\n";
        for (Event e : events_) {
            if (e.getDates_().contains(date)) {
                eventsForDay.add(new Event(e.getName_(), date, e.getTimeInterval_().getStartTime_(),
                        e.getTimeInterval_().getEndTime_()));
            }
        }
        Collections.sort(eventsForDay);
        for (Event e : eventsForDay) {
            dayEvents += e + "\n";
        }
        return dayEvents + "\n";
    }
}
