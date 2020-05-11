/**
 * <h2>Draw</h2>
 * Allows to draw all busses, streets,
 * lines and stops.
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.MapObjects;

import javafx.scene.shape.Shape;

import java.util.List;

public interface Draw {
    List<Shape> getGUI();
}
