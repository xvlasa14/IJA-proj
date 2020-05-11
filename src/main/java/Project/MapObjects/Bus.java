/**
 * <h2>Bus</h2>
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.MapObjects;

import Project.Mechanics.Coordinate;
import Project.Mechanics.Route;
import Project.Mechanics.Update;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Bus implements Draw, Update {
    private Coordinate position;
    private double speed;
    public double distance = 0;
    private Route route;
    private List<Shape> GUI;

    // constructor
    public Bus(Coordinate position, double speed, Route route) {
        this.position = position;
        this.speed = speed;
        this.route = route;
        GUI = new ArrayList<>();
        GUI.add(new Circle(position.getX(), position.getY(), 5, Color.PINK));
    }

    private void movement(Coordinate c) {
        for( Shape shape : GUI) {
            shape.setTranslateX((c.getX() - position.getX()) + shape.getTranslateX());
            shape.setTranslateY((c.getY() - position.getY()) + shape.getTranslateY());
        }
    }

    @Override
    public List<Shape> getGUI() {
        return GUI;
    }

    @Override
    public void update(LocalTime time) {
        distance += speed;
        Coordinate c = route.calculateRoute(distance);
        movement(c);
        position = c;


    }
}
