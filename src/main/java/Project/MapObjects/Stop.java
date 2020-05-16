package Project.MapObjects;

import Project.Mechanics.Coordinate;
import Project.Mechanics.Draw;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
/**
 * Represents a stop on a street
 */
public class Stop implements Draw {
    private String stopName;
    private Coordinate coordinates;
    @JsonIgnore
    private List<Shape> GUI;
    @JsonIgnore
    private Street street;

    /**
     * Constructor for bus stup
     * @param stopName name of the stop
     * @param coordinates coordinates of a stop
     */
    public Stop(String stopName, Coordinate coordinates) {
        this.stopName = stopName;
        this.coordinates = coordinates;
        getGUI();
    }

    @Override
    public List<Shape> getGUI() {
        double xStop = coordinates.getX();
        double yStop = coordinates.getY();

        return Arrays.asList(new Text(xStop, yStop, stopName), new Circle(xStop, yStop, 3, Color.LIGHTCORAL));
    }
}
