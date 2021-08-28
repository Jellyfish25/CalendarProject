/**
 * Calendar shape class that creates a calendar object
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Implements the ShapeObject interface to create a calendar object
 */
public class CalendarShape implements ShapeObject{

    private int x, y;
    private ArrayList<numberData> numberList;
    LocalDate currentLocalDate;

    /**
     * Constructor for CalendarShape
     * @param x The calendar's x position
     * @param y The calendar's y position
     * @param currentLocalDate the initial date set to the calendar
     */
    public CalendarShape(int x, int y, LocalDate currentLocalDate) {
        this.x = x;
        this.y = y;
        this.currentLocalDate = currentLocalDate;
    }

    /**
     * Sets the date of the calendar
     * @param currentLocalDate the new date being set
     */
    public void setDate(LocalDate currentLocalDate) {
        this.currentLocalDate = currentLocalDate;
    }

    /**
     * Translates the calendar to another position
     * @param dx The calendar's new x position
     * @param dy The calendar's new y position
     */
    public void translate(double dx, double dy)
    {
        x += dx;
        y += dy;
    }

    /**
     * Gets the calendar's number data
     * @return A list of numbers consisting of the number and their relative positions
     */
    public ArrayList<numberData> getNumberData() {
        return numberList;
    }

    /**
     * draws a calendar with the current date highlighted
     * @param g2 graphics component
     */
    public void draw(Graphics2D g2) {
        //initialize the number list to default
        numberList = new ArrayList<>();
        Rectangle2D rectangle = new Rectangle(5 + x, 5 + y, 220, 220);
        g2.setColor(new Color(47, 47, 47));
        g2.fill(rectangle);
        g2.draw(rectangle);

        String display = (currentLocalDate.getMonth() + " " + currentLocalDate.getYear());
        g2.setColor(Color.white);
        g2.drawString(display, 34 + x, 36 + y);
        //display the arrows for month traversal on calendar
        g2.drawString("<   >", 173 + x, 26);
        g2.drawString("Su   Mo   Tu   We   Th   Fr   Sa",33 + x, 56 + y);

        int monthLength = currentLocalDate.lengthOfMonth();
        LocalDate thisMonth = LocalDate.of(currentLocalDate.getYear(), currentLocalDate.getMonth(), 1);

        //to display next/previous month
        LocalDate previousLocalDate = LocalDate.of(currentLocalDate.getYear(),currentLocalDate.getMonthValue(), currentLocalDate.getDayOfMonth());
        LocalDate previousMonth = LocalDate.of(previousLocalDate.getYear(), previousLocalDate.getMonth().minus(1), 1);

        int previousMonthLength = previousMonth.lengthOfMonth();
        int startingDay = thisMonth.getDayOfWeek().getValue();
        int startingDayCounter = startingDay;

        int counter = 1;
        int dayCounter = 1;
        int nextMonthDayCounter = 1;

        for(int i = 0; i < 6; i++) {
            for(int k = 0; k < 7; k++) {
                if(startingDay != 0 && startingDay != 7) {
                    g2.setColor(new Color(88, 88, 88));
                    g2.setFont(new Font("default", Font.BOLD, 12));
                    int previousMonthValue = previousMonthLength - startingDayCounter + counter;
                    //25 = offset , 30 = margins
                    g2.drawString(Integer.toString(previousMonthValue), (k * 25) + 30 + x, (i * 25) + 80 + y);
                    numberList.add(new numberData(previousMonthValue, new Rectangle((k * 25) + 27 + x, (i * 25) + 65 + y, 20, 20)));
                    startingDay--;
                    counter++;
                }
                else if(monthLength != 0) {
                    Rectangle2D box = new Rectangle(0,0);
                    g2.setColor(Color.white);
                    if(dayCounter == currentLocalDate.getDayOfMonth()) {
                        g2.setColor(new Color(12, 187, 178));
                        box = new Rectangle((k * 25) + 27 + x, (i * 25) + 65 + y, 20, 20);
                    }
                    g2.drawString(Integer.toString(dayCounter),(k * 25) + 30 + x, (i * 25) + 80 + y);
                    numberList.add(new numberData(dayCounter, new Rectangle((k * 25) + 27 + x, (i * 25) + 65 + y, 20, 20)));
                    g2.setColor(new Color(12, 187, 178));
                    g2.draw(box);
                    dayCounter++;
                    monthLength--;
                }
                else {
                    g2.setColor(new Color(88, 88, 88));
                    g2.drawString(Integer.toString(nextMonthDayCounter),(k * 25) + 30 + x, (i * 25) + 80 + y);
                    numberList.add(new numberData(nextMonthDayCounter, new Rectangle((k * 25) + 27 + x, (i * 25) + 65 + y, 20, 20)));
                    nextMonthDayCounter++;
                }
            }
        }
    }
}
