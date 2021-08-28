/**
 * CalendarView class that functions as the viewer of the MVC architecture
 * @author Dimitar
 * @version 7/31/2021
 */
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Displays the calendar's view
 */
public class CalendarView {
    private final CalendarDataModel calendarDataModel;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 380;

    /**
     * Constructor for CalendarView
     * @param modelParam The calendar's data model
     */
    public CalendarView(CalendarDataModel modelParam) {
        calendarDataModel = modelParam;
    }

    /**
     * Displays the view frame
     */
    public void displayView() {
        //creating the calendar object to display
        CalendarShape calendarShape = new CalendarShape(0, -10, calendarDataModel.getDate());
        //creating the clock object to display
        LocalTime currentLocalTime = LocalTime.now();
        ClockShape clockShape = new ClockShape(50, currentLocalTime);
        clockShape.translate(175,230);

        //Arraylist of shape objects in order to contain all the objects in one label
        final ArrayList<ShapeObject> shapes = new ArrayList<>();
        shapes.add(calendarShape);
        shapes.add(clockShape);
        ShapeIcon icon = new ShapeIcon(shapes, WIDTH, HEIGHT);
        JLabel calendarObjectsLabel = new JLabel(icon);

        //change listener for calendarShape
        ChangeListener currentCalendarChangeListener = e -> calendarShape.setDate(calendarDataModel.getDate());
        calendarDataModel.addChangeListener(currentCalendarChangeListener);

        //******CONTROLLER (mouse listener for the Label)
        calendarObjectsLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                LocalDate newDate;
                LocalDate currentDate = calendarDataModel.getDate();
                ArrayList<numberData> data = calendarShape.getNumberData();

                int xClick = e.getX();
                int yClick = e.getY();

                for (numberData numData : data) {
                    int minX = (int) numData.getBox().getX();
                    int minY = (int) numData.getBox().getY();
                    int maxX = (int) numData.getBox().getMaxX();
                    int maxY = (int) numData.getBox().getMaxY();

                    if (xClick >= minX && xClick <= maxX && yClick >= minY && yClick <= maxY) {
                        //if the number clicked is a number from the previous month (first row, gray numbers)
                        if (yClick <= data.get(0).getBox().getMaxY() && numData.getNumber() >= 20) {
                            newDate = currentDate.minusMonths(1);
                            newDate = LocalDate.of(newDate.getYear(), newDate.getMonth(), numData.getNumber());
                            calendarDataModel.setDate(newDate);
                            calendarObjectsLabel.repaint();
                        }
                        //if the number clicked is a number from the next month (rows 5/6, gray numbers)
                        else if (yClick >= data.get(28).getBox().getY() && numData.getNumber() <= 15) {
                            newDate = currentDate.plusMonths(1);
                            newDate = LocalDate.of(newDate.getYear(), newDate.getMonth(), numData.getNumber());
                            calendarDataModel.setDate(newDate);
                            calendarObjectsLabel.repaint();
                        }
                        //if the number clicked is a day in the current month (white numbers)
                        else {
                            newDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), numData.getNumber());
                            calendarDataModel.setDate(newDate);
                            calendarObjectsLabel.repaint();
                        }
                    }
                }

                //for "<" arrow on the calendar, go to previous month
                if(xClick >= 168 && xClick <= 180 && yClick >= 14 && yClick <= 26) {
                    calendarDataModel.previousMonth();
                    calendarObjectsLabel.repaint();
                }

                //for ">" arrow on the calendar, go to next month
                if(xClick >= 186 && xClick <= 198 && yClick >= 14 && yClick <= 26) {
                    calendarDataModel.nextMonth();
                    calendarObjectsLabel.repaint();
                }
            }
        });

        //Timer to update the clock every second
        Timer timer = new Timer(1000, e -> {
            clockShape.updateClock();
            calendarObjectsLabel.repaint();
        });
        timer.start();

        //start of making the Today < > buttons code
        JButton todayButton = new JButton("Today");
        todayButton.addActionListener(e -> {
            calendarDataModel.setDate(LocalDate.now());
            calendarObjectsLabel.repaint();
        });
        JButton previousDateButton = new JButton("<");
        previousDateButton.addActionListener(e -> {
            switch (calendarDataModel.getView()) {
                case "day" -> calendarDataModel.previousDay();
                case "week" -> calendarDataModel.previousWeek();
                case "month" -> calendarDataModel.previousMonth();
                default -> {
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Error: current view type cannot be moved back.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            calendarObjectsLabel.repaint();
        });
        JButton nextDateButton = new JButton(">");
        nextDateButton.addActionListener(e -> {
            switch (calendarDataModel.getView()) {
                case "day" -> calendarDataModel.nextDay();
                case "week" -> calendarDataModel.nextWeek();
                case "month" -> calendarDataModel.nextMonth();
                default -> {
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Error: current view type cannot be moved forward.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            calendarObjectsLabel.repaint();
        });

        JPanel datePanel = new JPanel();
        datePanel.setBorder(BorderFactory.createEmptyBorder(10, 87, 5 ,0));
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.add(todayButton);
        datePanel.add(previousDateButton);
        datePanel.add(nextDateButton);
        //end of making the Today < > buttons code

        //start of Day Week Month Agenda buttons code
        JButton dayButton = new JButton("   Day    ");
        dayButton.addActionListener(e -> calendarDataModel.setView("day"));
        JButton weekButton = new JButton("  Week ");
        weekButton.addActionListener(e -> calendarDataModel.setView("week"));
        JButton monthButton = new JButton(" Month ");
        monthButton.addActionListener(e -> calendarDataModel.setView("month"));
        JButton agendaButton = new JButton("Agenda");
        agendaButton.addActionListener(e -> {
            JPanel inputForm = new JPanel();
            inputForm.add(new JLabel("Start Date (YYYY-MM-DD):"));
            JTextField startField = new JTextField(10);
            inputForm.add(startField);
            inputForm.add(Box.createHorizontalStrut(10));
            inputForm.add(new JLabel("End Date (YYYY-MM-DD):"));
            JTextField endField = new JTextField(10);
            inputForm.add(endField);
            int formValue = JOptionPane.showConfirmDialog(null, inputForm,
                    "Enter dates for agenda:", JOptionPane.OK_CANCEL_OPTION);
            if (formValue == JOptionPane.OK_OPTION) {
                try {
                    LocalDate startDate = LocalDate.parse(startField.getText());
                    LocalDate endDate = LocalDate.parse(endField.getText());
                    calendarDataModel.setAgendaView(startDate, endDate);
                }
                catch (DateTimeParseException exception) {
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Error: invalid input.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        viewPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        viewPanel.add(dayButton);
        viewPanel.add(weekButton);
        viewPanel.add(monthButton);
        viewPanel.add(agendaButton);
        //end of Day Week Month Agenda buttons code

        //start of CREATE From File buttons code
        JButton createButton = new JButton("CREATE");
        createButton.addActionListener(e -> {
            boolean eventConflicts = false, wantsToRepeat;
            JPanel inputForm = new JPanel();
            inputForm.setLayout(new BoxLayout(inputForm, BoxLayout.Y_AXIS));
            inputForm.add(new JLabel("Event Name:"));
            JTextField nameField = new JTextField();
            inputForm.add(nameField);
            inputForm.add(new JLabel("Event Date (YYYY-MM-DD):"));
            JTextField dateField = new JTextField();
            inputForm.add(dateField);
            inputForm.add(new JLabel("Event Start Hour (0-23):"));
            JTextField startHourField = new JTextField();
            inputForm.add(startHourField);
            inputForm.add(new JLabel("Event End Hour (0-23):"));
            JTextField endHourField = new JTextField();
            inputForm.add(endHourField);
            do {
                int formValue = JOptionPane.showConfirmDialog(null, inputForm,
                        "Enter information for one-time event:", JOptionPane.OK_CANCEL_OPTION);
                if (formValue == JOptionPane.OK_OPTION) {
                    eventConflicts = false;
                    wantsToRepeat = false;
                    try {
                        String name = nameField.getText();
                        LocalDate date = LocalDate.parse(dateField.getText());
                        int startHour = Integer.parseInt(startHourField.getText());
                        int endHour = Integer.parseInt(endHourField.getText());
                        Event newEvent = new Event(name, date, startHour, endHour);
                        //check for time conflict
                        for (Event event : calendarDataModel.getEvents()) {
                            if (event.conflictsWith(newEvent)) {
                                JPanel panel = new JPanel();
                                JOptionPane.showMessageDialog(panel, "Error: " + newEvent.getName_() +
                                        " cannot be created because it conflicts with " + event.getName_() + ".",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                eventConflicts = true;
                                wantsToRepeat = true;
                                break;
                            }
                        }
                        if (!eventConflicts)  {
                            calendarDataModel.addEvent(newEvent);

                        }
                    }
                    catch (NullPointerException exception) {
                        JPanel panel = new JPanel();
                        JOptionPane.showMessageDialog(panel, "Error: one of the fields was left empty.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (DateTimeParseException exception) {
                        JPanel panel = new JPanel();
                        JOptionPane.showMessageDialog(panel, "Error: invalid input for date.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (NumberFormatException exception) {
                        JPanel panel = new JPanel();
                        JOptionPane.showMessageDialog(panel, "Error: invalid input for start and/or end hours.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    wantsToRepeat = false;
                }
            } while (eventConflicts && wantsToRepeat);
        });
        JButton fromFileButton = new JButton("From File");
        fromFileButton.addActionListener(e -> {
            JPanel inputForm = new JPanel();
            inputForm.add(new JLabel("File Name (filename.txt):"));
            JTextField fileField = new JTextField(10);
            inputForm.add(fileField);
            int formValue = JOptionPane.showConfirmDialog(null, inputForm,
                    "Enter file name (with file extension):", JOptionPane.OK_CANCEL_OPTION);
            if (formValue == JOptionPane.OK_OPTION) {
                String filename = fileField.getText();
                calendarDataModel.addRecurringEvents(filename);
            }
        });

        JPanel eventCreatorPanel = new JPanel();
        eventCreatorPanel.setLayout(new BoxLayout(eventCreatorPanel, BoxLayout.X_AXIS));
        eventCreatorPanel.setBorder(BorderFactory.createEmptyBorder(0, 81, 0 ,0));
        eventCreatorPanel.add(createButton);
        eventCreatorPanel.add(fromFileButton);
        datePanel.add(eventCreatorPanel);
        //end of CREATE From File buttons code

        //start of current calendar/clock code
        JButton placeHolderButton = new JButton("Current Calendar");
        JPanel currentCalendarPanel = new JPanel();
        currentCalendarPanel.setLayout(new BoxLayout(currentCalendarPanel, BoxLayout.Y_AXIS));
        currentCalendarPanel.add(placeHolderButton);
        //end of current calendar/clock code

        //Strategy pattern buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton lightButton = new JButton("  Light   ");
        JButton darkButton = new JButton("  Dark   ");
        buttonPanel.add(lightButton);
        buttonPanel.add(darkButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        viewPanel.add(buttonPanel);
        lightButton.addActionListener(e -> calendarDataModel.setColorMode(new LightMode()));
        darkButton.addActionListener(e -> calendarDataModel.setColorMode(new DarkMode()));
        //end of strategy pattern buttons

        //start of textArea/scrollPane code
        JTextArea textArea = new JTextArea(calendarDataModel.getTextAreaContent());
        Border border = BorderFactory.createLineBorder(new Color(12, 187, 178));
        textArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5,5,5,90)));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        //end of textArea/scrollPane code

        //start of frame code
        JFrame frame = new JFrame();
        frame.add(datePanel, BorderLayout.NORTH);
        frame.add(viewPanel, BorderLayout.WEST);
        frame.add(calendarObjectsLabel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.EAST);
        frame.setTitle("Calendar Program");
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //end of frame code

        ChangeListener changeListener = e -> {
            textArea.setText(calendarDataModel.getTextAreaContent());
            textArea.setForeground(calendarDataModel.getColorMode().getTextColor());
            textArea.setBackground(calendarDataModel.getColorMode().getBackgroundColor());
        };
        calendarDataModel.addChangeListener(changeListener);
    }
}
