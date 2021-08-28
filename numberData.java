/**
 * Helper class for calendarShape: when a calendar number is created,
 * the calendar's number is saved alongside a box that contains the number's
 * coordinates on the calendar.
 * @author Dimitar Dimitrov
 * @version 7/25/2021
 */
import java.awt.geom.Rectangle2D;

/**
 * Models a numberData object that consists of a number and a box shape
 */
public class numberData {
    private final int number;
    private final Rectangle2D box;

    /**
     * Constructor for numberData
     * @param number the calendar number being stored
     * @param box a box containing the number's coordinates
     */
    public numberData(int number, Rectangle2D box) {
        this.number = number;
        this.box = box;
    }

    /**
     * Gets the calendar's number
     * @return the calendar's number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the number's box
     * @return returns the number's box
     */
    public Rectangle2D getBox() {
        return box;
    }
}
