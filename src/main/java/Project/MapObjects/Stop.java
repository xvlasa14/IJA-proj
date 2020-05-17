package Project.MapObjects;

import Project.Mechanics.Coordinate;
import Project.Mechanics.Draw;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents a stop on a street
 */
@JsonDeserialize(converter = Stop.stopConstructor.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stopName")
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
        setGUI();
    }
    public Stop() {
    }

    /**
     * Sets the GUI for a stop - text and little dot
     */
    public void setGUI() {
        GUI = new ArrayList<>();
        double xStop = coordinates.getX();
        double yStop = coordinates.getY();
        GUI.add(new Text(xStop, yStop, stopName));
        GUI.add(new Circle(xStop, yStop, 3, Color.LIGHTCORAL));
    }

    /**
     * Gets GUI of certain stop
     * @return
     */
    @Override
    public List<Shape> getGUI() {
        return GUI;
    }

    /**
     * Sets street on which stop is located
     * @param map map containing all streets
     */
    public void setStreet(Map map) {
        List<Street> streets = map.getStreets();
        for (Street s : streets) {
            for(Stop t : s.getStops()) {
                if(t == this){
                    this.street = s;
                }
            }
        }
    }

    /**
     * Gets this street
     * @return street on which stop is located
     */
    public Street getStreet() {
        return street;
    }

    /**
     * Gets the name of a stop
     * @return name of this stop
     */
    public String getStopName() {
        return stopName;
    }

    /**
     * Sets the name of a stop
     * @param stopName name to be set.
     */
    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    /**
     * Gets coordinates of this stop
     * @return stop location
     */
    public Coordinate getCoordinates() {
        return coordinates;
    }

    /**
     * Sets coordinates of this stop
     * @param coordinates where stop will be located
     */
    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Stop " + stopName + ": \n" +
               "  Coordinates:" + coordinates;
    }

    /**
     * Stop constructor for loading from YML file
     */
    static class stopConstructor extends StdConverter<Stop, Stop> {
         @Override
        public Stop convert(Stop val){
             val.setGUI();
             return val;
         }
    }
}
