/**
 * <h2>Draw</h2>
 * Allows to draw all busses, streets,
 * lines and stops.
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.Mechanics;

import javafx.scene.shape.Shape;

import java.util.List;

/**
 * "Drawing" the GUI
 */
public interface Draw {
    /**
     * Gets GUI
     * @return list of shapes that make up a GUI
     */
    List<Shape> getGUI();
}
