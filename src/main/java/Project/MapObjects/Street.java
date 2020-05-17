package Project.MapObjects;

import Project.Mechanics.Coordinate;
import Project.Mechanics.Draw;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents street on a map
 */
@JsonDeserialize(converter = Street.streetConstructor.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sName")
public class Street implements Draw {
    private Coordinate begin;       // beginning of a street
    private Coordinate end;         // ending of a street
    private List<Coordinate> streetLocation;  // pair of coordinates - beginning and end
    private String sName;      // name of the street
    private double traffic;         // amount of traffic on a street
    private List<Stop> stops;       // list of stops
    @JsonIgnore
    private List<Shape> GUI = new ArrayList<>();

    /**
     * Street constructor
     *
     * @param begin      beginning of a street
     * @param end        ending of a street
     * @param sName name of the street
     * @param traffic    amount of traffic on a street
     * @param stops      list of stops
     */
    public Street(Coordinate begin, Coordinate end, String sName, double traffic, List<Stop> stops) {
        this.begin = begin;
        this.end = end;
        this.sName = sName;
        this.traffic = traffic;
        this.stops = stops;
        this.streetLocation.add(begin);
        this.streetLocation.add(end);
        setGUI();
    }
    public Street() {
    }

    /**
     * Gets the beginning point of a street
     *
     * @return coordinates of that point
     */
    public Coordinate getBegin() {
        return begin;
    }

    /**
     * Gets the ending point of a street
     *
     * @return coordinates of that point
     */
    public Coordinate getEnd() {
        return end;
    }

    /**
     * Gets the street's name
     *
     * @return street's name
     */
    public String getsName() {
        return sName;
    }

    /**
     * Gets the traffic info
     *
     * @return traffic (represented by a number)
     */
    public double getTraffic() {
        return traffic;
    }

    /**
     * Gets all stops on a street
     *
     * @return list of stops
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * Sets the beginning of a street
     *
     * @param begin coordinate of the beginning point
     */
    public void setBegin(Coordinate begin) {
        this.begin = begin;
    }

    /**
     * Sets the end of a street
     *
     * @param end coordinate of the ending point
     */
    public void setEnd(Coordinate end) {
        this.end = end;
    }

    /**
     * Sets street's name
     *
     * @param sName name of the street to be set
     */
    public void setsName(String sName) {
        this.sName = sName;
    }

    /**
     * Sets the traffic for this street
     *
     * @param traffic number representing how much traffic there is
     */
    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    /**
     * Sets stops of this street
     *
     * @param stops list of stops to be set
     */
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    /**
     * Prepares the GUI that represents a street
     * @return
     */
    public List<Shape> setGUI() {
        // calculating where to place the text
        double xText = (begin.getX() + end.getX()) / 2;
        double yText = ((begin.getY() + end.getY()) / 2) - 5;

        // create the text and line representing street
        Text text = new Text(xText, yText, sName);
        Line line = new Line(begin.getX(), begin.getY(), end.getX(), end.getY());

        // construct GUI
        GUI.add(line);  // add line
        GUI.get(0).setStroke(Color.SLATEGRAY);  // set color of the line
        GUI.get(0).setStrokeWidth(1);   // set the width of the line
        GUI.add(text); // add text
        return GUI;
    }

    public void clickedStreet(){
        GUI.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                traffic = traffic + 1;
                GUI.get(0).setStroke(Color.DARKSLATEGRAY);
                if(traffic < 5) {
                    GUI.get(0).setStrokeWidth(traffic);
                }
            }
        });
    }

    @Override
    public List<Shape> getGUI() {
        return this.GUI;
    }


    static class streetConstructor extends StdConverter<Street, Street> {
        @Override
        public Street convert(Street val){
            val.setGUI();
            val.clickedStreet();
            return val;
        }
    }

    @Override
    public String toString() {
        return "Street " + sName + '\n' +
                "traffic: " + traffic;
    }
}
