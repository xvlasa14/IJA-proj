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

import java.util.Arrays;
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

    /**
     * Sets the GUI for a stop
     * @return GUI in the form of dot with text above it
     */
    @Override
    public List<Shape> setGUI() {
        double xStop = coordinates.getX();
        double yStop = coordinates.getY();

        return Arrays.asList(new Text(xStop, yStop, stopName), new Circle(xStop, yStop, 3, Color.LIGHTCORAL));
    }

    /**
     * Gets GUI of certain stop
     * @return
     */
    @Override
    public List<Shape> getGUI() {
        return GUI;
    }

    public void setStreet(Map map) {
        List<Street> streets = map.getStreets();
        for (Street s : streets) {
            for(Stop t : s.getStop()) {
                if(t == this){
                    this.street = s;
                }
            }
        }

    }

    static class stopConstructor extends StdConverter<Stop, Stop> {
         @Override
        public Stop convert(Stop val){
             val.setGUI();
             return val;
         }
    }
}
