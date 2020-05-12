/**
 * <h2>Bus</h2>
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.MapObjects;

import Project.Mechanics.Coordinate;
import Project.Mechanics.Draw;
import Project.Mechanics.Route;
import Project.Mechanics.Update;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents bus on the map.
 */
public class Bus implements Draw, Update {
    private Coordinate position;    // current position
    private double speed;           // speed at which is bus moving
    public double distance = 0;     // distance driven by the bus
    private Route route;            // it's route
    private List<Shape> GUI;

    /**
     * Creates a new bus.
     * @param position default bus position
     * @param speed bus speed
     * @param route route which the bus takes
     */
    public Bus(Coordinate position, double speed, Route route) {
        this.position = position;
        this.speed = speed;
        this.route = route;
        GUI = new ArrayList<>();
        GUI.add(new Circle(position.getX(), position.getY(), 5, Color.PINK));
    }

    /**
     * Method that ensures the movement of a bus
     * on the map itself
     * @param c where will the bus move
     */
    private void movement(Coordinate c) {
        for( Shape shape : GUI) {
                shape.setTranslateX((c.getX() - position.getX()) + shape.getTranslateX());
                shape.setTranslateY((c.getY() - position.getY()) + shape.getTranslateY());
        }
    }

    /**
     * Gets the GUI of bus
     * @return GUI
     */
    @Override
    public List<Shape> getGUI() {
        return GUI;
    }

    /**
     * Update the bus position
     * @param time
     */
    @Override
    public void update(LocalTime time) {
        distance += speed;
        Coordinate c = route.calculateRoute(distance);
        movement(c);
        position = c;
    }
}
