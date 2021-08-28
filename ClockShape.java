/**
 * Clock shape class that creates a clock object
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import static java.awt.Color.RED;

/**
 * Implements ShapeObject interface to create a clock object
 */
public class ClockShape implements ShapeObject{

    private final int width;
    private int hour, minute, second, x, y;

    /**
     * Constructor for ClockShape
     * @param width the clock's width
     * @param currentLocalTime the clock's initial time
     */
    public ClockShape(int width, LocalTime currentLocalTime) {
        this.width = width;
        hour = currentLocalTime.getHour();
        minute = currentLocalTime.getMinute();
        second = currentLocalTime.getSecond();
    }

    /**
     * Draws the clock object
     * @param g2 graphics component
     */
    @Override
    public void draw(Graphics2D g2) {
        double xPosition, yPosition, angle;
        //draw clock body
        Ellipse2D.Double clockBody = new Ellipse2D.Double(x, y, width, width);
        g2.draw(clockBody);

        //draw hour hand
        g2.setStroke(new BasicStroke(3f));
        //angle for current hour + minutes (between next hour): 2pi/12 * hour + minutes
        angle = (Math.PI/6) * ((hour % 12) + minute/(float)60);
        xPosition = (x + width/(float)2) + (0.5 * width/2 * Math.sin(angle));
        yPosition = (y + width/(float)2) - (0.5 * width/2 * Math.cos(angle));
        g2.draw(new Line2D.Double(x + width/(float)2, y + width/(float)2, xPosition, yPosition));

        //draw minute hand: 2pi/60 * minutes
        g2.setStroke(new BasicStroke(2f));
        angle = (Math.PI/30) * minute;
        xPosition = (x + width/(float)2) + (0.9 * width/2 * Math.sin(angle));
        yPosition = (y + width/(float)2) - (0.9 * width/2 * Math.cos(angle));
        g2.draw(new Line2D.Double(x + width/(float)2, y + width/(float)2, xPosition, yPosition));

        //draw second hand: 2pi/60 * seconds
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(RED);
        angle = (Math.PI/30) * second;
        xPosition = (x + width/(float)2) + (width/(float)2 * Math.sin(angle));
        yPosition = (y + width/(float)2) - (width/(float)2 * Math.cos(angle));
        g2.draw(new Line2D.Double(x + width/(float)2, y + width/(float)2, xPosition, yPosition));

        String time = this.displayTime();
        g2.drawString(time, x + 2, y + 65);
    }

    /**
     * Translates the clock object
     * @param dx The new x position
     * @param dy The new y position
     */
    @Override
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }

    /**
     * Displays the clock's current time
     * @return the clock's time
     */
    public String displayTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Time time = new Time(hour, minute, second);
        return dateFormat.format(time);
    }

    /**
     * Updates the clock's hours, minutes, and seconds
     */
    public void updateClock() {
        LocalTime currentLocalTime = LocalTime.now();
        hour = currentLocalTime.getHour();
        minute = currentLocalTime.getMinute();
        second = currentLocalTime.getSecond();
    }
}
