/**
 * Part of the strategy pattern, interface
 * with methods to get the current text color
 * and background color for the text area
 * @author Dimitar Dimitrov
 * @version 8/2/2021
 */

import java.awt.*;

/**
 * Represents a color mode for CalendarView.
 */
public interface ColorMode {
    /**
     * Gets the color for the buttons in the GUI.
     * @return the buttons' color
     */
    Color getTextColor();

    /**
     * Gets the color for the frame of the GUI.
     * @return the frame's color
     */
    Color getBackgroundColor();
}
