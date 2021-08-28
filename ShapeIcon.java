/**
 * This class implements the Icon interface and models
 * multiple shape objects for the CalendarView.
 * @author Dimitar Dimitrov
 * @version 7/31/2021
 */
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Stores multiple shape objects in a list and paints the icons
 */
public class ShapeIcon implements Icon {
    private final ArrayList<ShapeObject> shapeObjectList;
    private final int width;
    private final int height;

    /**
     * Constructor for class ShapeIcon
     * @param shapeObjectList array list of shapes
     * @param width the width of the shape
     * @param height the height of the shape
     */
    public ShapeIcon(ArrayList<ShapeObject> shapeObjectList, int width, int height)
    {
        this.shapeObjectList = shapeObjectList;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the icon's width
     * @return the icon's width
     */
    @Override
    public int getIconWidth()
    {
        return width;
    }

    /**
     * Gets the icon's height
     * @return the icon's height
     */
    @Override
    public int getIconHeight()
    {
        return height;
    }

    /**
     * Paints all the shape objects from the provided list
     * @param c the component
     * @param g the graphics
     * @param x the x coordinates to paint the object
     * @param y the y coordinates to paint the object
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        Graphics2D g2 = (Graphics2D)g;
        for(ShapeObject shape : shapeObjectList) {
            shape.draw(g2);
        }
    }
}
