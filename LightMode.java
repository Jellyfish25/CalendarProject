/**
 * Part of the strategy pattern, implements
 * ColorMode interface
 * @author Dimitar Dimitrov
 * @version 8/2/2021
 */
import java.awt.*;

/**
 * Sets the text color to the default color and
 * the background color to white
 */
public class LightMode implements ColorMode {

    /**
     * Sets the text color to the default color
     * @return null
     */
    @Override
    public Color getTextColor() {
        return null;
    }

    /**
     * Sets the background color to white
     * @return White color
     */
    @Override
    public Color getBackgroundColor() {
        return Color.white;
    }
}
