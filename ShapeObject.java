/**
 * ShapeObject interface for CalendarShape and ClockShape classes
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */
import java.awt.*;

/**
 * A shape object that can be drawn and moved
 */
public interface ShapeObject
{
    /**
     * Draws the shape object
     * @param g2 graphics component
     */
    void draw(Graphics2D g2);

    /**
     * Translates the shape object based on the provided x & y coordinates
     * @param x x position to translate by
     * @param y y position to translate by
     */
    void translate(double x, double y);
}