/**
 * <h2>Bus</h2>
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.MapObjects;

import Project.Mechanics.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents bus on the map.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "busID")
public class Bus implements Draw, Update {
    private String busID;           // bus ID in X.Y format where X is the line and Y is number of the bus
    @JsonIgnore
    private Coordinate position;    // current position
    private double speed;           // speed at which is bus moving
    @JsonIgnore
    public double distance = 0;     // distance driven by the bus
    @JsonIgnore
    private Route route;            // it's route
    private String startsAt;         // time at which this bus starts moving
    private Line line;              // line to which bus belongs
    @JsonIgnore
    private LocalTime time;
    @JsonIgnore



    private List<Shape> GUI;

    /**
     * Creates a new bus.
     * @param position default bus position
     * @param speed bus speed
     * @param route route which the bus takes
     * @param busID bus ID in X.Y format
     */
    public Bus(Line line, LocalTime time, String startsAt, Coordinate position, double speed, Route route, String busID) {
        this.line = line;
        this.startsAt = startsAt;
        this.busID = busID;
        this.position = position;
        this.speed = speed;
        this.route = route;
        GUI = new ArrayList<>();
        GUI.add(new Circle(position.getX(), position.getY(), 5, Color.PINK));
    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @JsonIgnore
    public LocalTime getTime() {
        return time;
    }

    @JsonIgnore
    public void setTime(LocalTime time) {
        this.time = time;
    }

    @JsonIgnore
    public void setStart() {
        position = line.getStops().get(0).getStreet().getStart();
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
