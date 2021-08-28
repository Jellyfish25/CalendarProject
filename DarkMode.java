/**
 * Part of the strategy pattern, implements
 * ColorMode interface
 * @author Dimitar Dimitrov
 * @version 8/2/2021
 */
import java.awt.*;

/**
 * Sets the text color to white and the background color to black
 */
public class DarkMode implements ColorMode
{
    /**
     * Sets the text color to white
     * @return White color
     */
    @Override
    public Color getTextColor() {
        return Color.WHITE;
    }

    /**
     * Sets the background color to black
     * @return black color
     */
    @Override
    public Color getBackgroundColor() {
        return new Color(47, 47, 47);
    }
}
