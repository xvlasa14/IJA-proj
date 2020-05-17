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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents bus on the map.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "busID")
public class Bus implements Draw, Update {
    private String busID;               // bus ID in X.Y format where X is the line and Y is number of the bus
    @JsonIgnore
    private Coordinate position;        // current position
    private double speed;               // speed at which is bus moving
    @JsonIgnore
    public double distance = 0;         // distance driven by the bus
    @JsonIgnore
    private Route route;                // it's route
    private List<String> timeString;    // times at which this bus starts moving
    private Line line;                  // line to which bus belongs
    @JsonIgnore
    private List<LocalTime> timeLocal;  // starting time in time format
    @JsonIgnore
    private List<Shape> GUI;
    @JsonIgnore
    private boolean isActive = false;   // is bus active or not
    private double speedSet;            // slower speed
    private int wait = 0;               // default time of waiting at a stop

    /**
     * Constructor for bus
     * @param speed its speed
     * @param timeString time at which the bus starts (in string)
     * @param line line to which it belongs
     * @param timeLocal time in local time format
     */
    public Bus(double speed, List<String> timeString, Line line, List<LocalTime> timeLocal, String busID) {
        this.busID = busID;
        this.speed = speed;
        this.timeString = timeString;
        this.line = line;
        this.timeLocal = timeLocal;
    }
    private Bus() {
    }
    /**
     * GETTERY A SETTERY
     */

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

    public List<String> getTimeString() {
        return timeString;
    }

    public void setTimeString(List<String> timeString) {
        this.timeString = timeString;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<LocalTime> getTimeLocal() {
        return timeLocal;
    }

    public void setTimeLocal(List<String> timeString) {
        timeLocal = new ArrayList<>();
        for( String t : timeString){
            timeLocal.add(LocalTime.parse(t));
        }
    }

    public void setGUI(List<Shape> GUI) {
        this.GUI = GUI;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getSpeedSet() {
        return speedSet;
    }

    public void setSpeedSet(double speedSet) {
        this.speedSet = speedSet;
    }

    public int getWait() {
        return wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    /**
     * Sets the starting position to beginning coordinate of
     * the first street on which first stop lies
     */
    public void setStart() {
        position = line.getStops().get(0).getStreet().getBegin();
    }
    /**
     * Set GUI in the form of a circle of certain color and radius. Color is taken
     * from line to which this bus belongs.
     */
    public void setGUI() {
        GUI = new ArrayList<>();
        GUI.add(new Circle(position.getX(), position.getY(), 5, Color.web(line.getColor())));
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
     * At the right time, function places a bus on the map and
     * when said bus finishes his route, it is removed from the map.
     * @param locTime current time (in program runtime terms)
     * @param busController controller that places and removes buses
     */
    @Override
    public void update(LocalTime locTime, Controller busController) {
        // if it's time to start the route (current time is equal to time of the bus)
        if(locTime.equals(timeLocal.get(0))) {
            if(busController.getActiveBuses().contains(this)){
                return;
            }
            busController.getActiveBuses().add(this);
            busController.addBus(this::getGUI); // add bus to the map
            isActive = true;    // set bus to active
        }
        // else if it's after the starting time
        else if(locTime.isAfter(timeLocal.get(0))) {
            distance += speed;
            double routeLength = route.totalDistance();
            if(routeLength < distance) {
                busController.removeBus(this::getGUI);
                busController.getActiveBuses().remove(this);
                return;
            }
            Coordinate c = route.nextPosition(distance);
            movement(c);
            position = c;
        }
    }

    /**
     * If there's traffic, bus slows down by a fraction it's original speed.
     * Slowest a bus can move is 0.4.
     */
    @Override
    public void traffic() {
        double traffic = route.getThisStreet().getTraffic();
        double currentSpeed;
        if(traffic > 0) {
            currentSpeed = speedSet;
            while(traffic > 0) {
                currentSpeed = currentSpeed - 0.2;
                if(currentSpeed < 0.2) {
                    currentSpeed = 0.4;
                    break;
                }
                traffic = traffic - 1;
            }
            speed = currentSpeed;
        }
        else {
            speed = speedSet;
        }
    }

    /**
     * Iterates through all stops along the route and if bus is somewhere
     * really close to a stop, it stops and waits for a while (aka lets passengers
     * in and out. Deviation is used to calculate how close to a stop bus really is.
     */
    @Override
    public void stopAtStop() {
        for(Stop t : route.getThisStreet().getStops()) {
            double deviationX = 0;
            double deviationY = 0;
            deviationX = Math.abs(Math.abs(t.getCoordinates().getX() - Math.abs(position.getX())));
            deviationY = Math.abs(Math.abs(t.getCoordinates().getY() - Math.abs(position.getY())));

            if(deviationX  < 2.0 && deviationY < 2.0){
                System.out.println("BUS " + busID + "IS AT STOP " + t.getStopName() + "\n");
                if(wait == 0) {
                    wait = 6;
                    speed = 0;
                }
                else {
                    wait = wait - 1;
                    if(wait == 0) {
                        speed = speedSet;
                    }
                }
                break;
            }
        }
    }

    /**
     * Initializes s bus. First its starting position is set, then GUI
     * is created and time is set to its starting time. Then new route
     * is created and stops are loeaded in from line to which this
     * bus belongs and speed is set.
     * After that, in the event of a mouse click, its route is highlighted
     * in the color of its line.
     */
    public void initBus(){
        setStart();
        setGUI();
        setTimeLocal(timeString);
        route = new Route();
        route.setRoute(line.getStops());
        speedSet = speed;
        GUI.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            @FXML
            public void handle(MouseEvent event) {
                for(Street s : route.getRouteStreets()){
                    s.getGUI().get(0).setStroke(Color.web(line.getColor()));
                    s.getGUI().get(0).setStrokeWidth(2);
                }
            }
        });
    }

    @Override
    public String toString() {
        return "Bus " + busID + " line: " + line + '\n' +
               "  Starts at: " + timeString + '\n' +
               "      Speed: " +  speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bus bus = (Bus) o;
        return Double.compare(bus.speed, speed) == 0 &&
                Double.compare(bus.distance, distance) == 0 &&
                isActive == bus.isActive &&
                Double.compare(bus.speedSet, speedSet) == 0 &&
                wait == bus.wait &&
                Objects.equals(busID, bus.busID) &&
                Objects.equals(position, bus.position) &&
                Objects.equals(route, bus.route) &&
                Objects.equals(timeString, bus.timeString) &&
                Objects.equals(line, bus.line) &&
                Objects.equals(timeLocal, bus.timeLocal) &&
                Objects.equals(GUI, bus.GUI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(busID, position, speed, distance, route, timeString, line, timeLocal, GUI, isActive, speedSet, wait);
    }
}
