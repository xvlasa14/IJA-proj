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
    private String startsAt;            // time at which this bus starts moving
    private Line line;                  // line to which bus belongs
    @JsonIgnore
    private LocalTime time;
    @JsonIgnore
    private List<Shape> GUI;
    @JsonIgnore
    private boolean isActive = false;   // is bus active or not
    private double speedSet;            // slower speed
    private int wait = 0;               // default time of waiting at a stop

    /**
     * Constructor for bus
     * @param speed its speed
     * @param startsAt time at which the bus starts
     * @param line line to which it belongs
     * @param time time
     */
    public Bus(double speed, String startsAt, Line line, LocalTime time) {
        this.speed = speed;
        this.startsAt = startsAt;
        this.line = line;
        this.time = time;
        setGUI();
    }
    private Bus() {
    }

    /**
     * Set GUI in the form of a circle of certain color and radius. Color is taken
     * from line to which this bus belongs.
     */
    public void setGUI() {
        GUI = new ArrayList<>();
        GUI.add(new Circle(position.getX(), position.getY(), 5, Color.web(line.getColor())));
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
    public void setTime(String startsAt) {
        this.time = LocalTime.parse(startsAt);
    }

    @JsonIgnore
    public void setStart() {
        position = line.getStops().get(0).getStreet().getBegin();
    }

    public boolean isActive() {
        return isActive;
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
     * At the right time, function places a bus on the map and
     * when said bus finishes his route, it is removed from the map.
     * @param locTime current time (in program runtime terms)
     * @param busController controller that places and removes buses
     */
    @Override
    public void update(LocalTime locTime, Controller busController) {
        // if it's time to start the route (current time is equal to time of the bus)
        if(locTime.equals(time)) {
            busController.addBus(this::getGUI); // add bus to the map
            isActive = true;    // set bus to active
        }
        // else if it's after the starting time
        else if(locTime.isAfter(time)) {
            distance += speed;
            double routeLength = route.totalDistance();
            if(routeLength < distance) {
                busController.removeBus(this::getGUI);
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
            // TO DO vypocítat odchylku
            // if odchylka je oukej: (následující bude v ifu výsledku té odchylky
            if(wait == 0) {
                wait = 4;
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
        setTime(startsAt);
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
               "  Starts at: " + startsAt + '\n' +
               "      Speed: " +  speed;
    }
}
