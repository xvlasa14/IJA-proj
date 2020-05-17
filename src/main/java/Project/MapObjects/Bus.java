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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
    public double distance = 0;         // distance traveled by the bus
    @JsonIgnore
    private Route route;                // it's route
    public static List<String> timeString;    // times at which this bus starts moving
    private Line line;                  // line to which bus belongs
    @JsonIgnore
    private List<LocalTime> timeLocal;  // starting time in time format
    @JsonIgnore
    private List<Shape> GUI;
    @JsonIgnore
    private boolean isActive = false;   // is bus active or not
    private double speedSet;            // slower speed
    private int wait = 0;               // default time of waiting at a stop
    private boolean atStopFlag = false;
    public List<String> visited = new ArrayList<>();
    @FXML
    private List<Shape> stopText;
    private ArrayList<Shape> sGUI;
    private boolean schedGUI = false;



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
     * Gets bus id
     * @return busID
     */
    public String getBusID() {
        return busID;
    }

    /**
     * Sets bus id
     * @param busID id to be set
     */
    public void setBusID(String busID) {
        this.busID = busID;
    }

    /**
     * Gets position of a bus
     * @return position in coordinates
     */
    public Coordinate getPosition() {
        return position;
    }

    /**
     * Sets bus' position
     * @param position where bus currently is
     */
    public void setPosition(Coordinate position) {
        this.position = position;
    }

    /**
     * Gets the speed of a bus
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets speed of a bus
     * @param speed speed to be set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Gets distance so far traveled by a bus
     * @return
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets distance traveled by this bus
     * @param distance distance traveled
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Gets route this bus takes
     * @return route of this bus
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Sets route that will be traveled by this bus
     * @param route to be traveled
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * Gets list of times in string form
     * @return List of times
     */
    public List<String> getTimeString() {
        return timeString;
    }

    /**
     * Sets list of times in string form
     * @param timeString list of times to be set
     */
    public void setTimeString(List<String> timeString) {
        this.timeString = timeString;
    }

    /**
     * Gets line
     * @return line
     */
    public Line getLine() {
        return line;
    }

    /**
     * Sets line
     * @param line line to be set
     */
    public void setLine(Line line) {
        this.line = line;
    }

    /**
     * Gets time in LocalTime format
     * @return list of times
     */
    public List<LocalTime> getTimeLocal() {
        return timeLocal;
    }

    /**
     * Converts time in string to localtime format
     * @param timeString
     */
    public void setTimeLocal(List<String> timeString) {
        timeLocal = new ArrayList<>();
        for( String t : timeString){
            timeLocal.add(LocalTime.parse(t));
        }
    }

    /**
     * Sets GUI
     * @param GUI gui to be set
     */
    public void setGUI(List<Shape> GUI) {
        this.GUI = GUI;
    }

    /**
     * Returns the value of isActive varualbe
     * @return
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets isActive varuable
     * @param active true/false
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets default speed
     * @return
     */
    public double getSpeedSet() {
        return speedSet;
    }

    /**
     * Sets default speed
     * @param speedSet
     */
    public void setSpeedSet(double speedSet) {
        this.speedSet = speedSet;
    }

    /**
     * Gets wait time
     * @return wait time
     */
    public int getWait() {
        return wait;
    }

    /**
     * Sets wait time to given int
     * @param wait wait time
     */
    public void setWait(int wait) {
        this.wait = wait;
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
     * Creates a line schedule list, color coded
     * @param schedule controller that controls coloring of stop ID's
     */
    public void getStopList(Controller schedule) {
        double x = 85;
        double y = 40;
        stopText = new ArrayList<>();
        for(Line line : schedule.getMap().getLines()){
            int timeCode = 0;
            Text title = new Text(x - 25, y, "LINE SCHEDULE");
            title.setFont(Font.font("Garamond", FontWeight.SEMI_BOLD, 20));
            title.setStroke(Color.web(line.getColor()));
            stopText.add(title);
            y = y + 22;
            for(Stop s : line.getStops()){
                String timeText = timeString.get(timeCode + 1);
                Text tempText = new Text(x, y, s.getStopName() + "      " + timeText);
                timeCode = timeCode + 1;

                tempText.setFont(Font.font("Garamond", FontWeight.LIGHT, 15));
                tempText.setStroke(Color.LIGHTGRAY);

                stopText.add(tempText);
                y = y + 22;
            }
            schedule.drawGUI(stopText);
            y = y + 50;
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
            getStopList(busController);
            if(schedGUI == true){
                scheduleGUI();
                busController.getS(sGUI);
            }
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
     * calculates if this bus is on a street
     * @return street on which bus is currently
     */
    private Street busOnStreet(){
        for(Street s : route.getRouteStreets()){
            Coordinate sBegin = s.getBegin();
            Coordinate sEnd = s.getEnd();
            double busBeginX = position.getX() - sBegin.getX();
            double busBeginY = position.getY() - sBegin.getY();

            double beginEndX = sEnd.getX() - sBegin.getX();
            double beginEndY = sEnd.getY() - sBegin.getY();

            double result = busBeginX * beginEndY - busBeginY * beginEndX;

            if(-0.05 < result && result < 0.05) {
                if(Math.abs(beginEndX) >= Math.abs(beginEndY)) {
                    if(beginEndX > 0){
                        if(sBegin.getX() <= position.getX() && position.getX() <= sEnd.getX()) {
                            return s;
                        }
                    }
                    else{
                        if(sEnd.getX() <= position.getX() && position.getX() <= sBegin.getX()) {
                            return s;
                        }
                    }
                }
                else {
                    if(beginEndY > 0){
                        if (sBegin.getY() <= position.getY() && position.getY() <= sEnd.getY()) {
                            return s;
                        }
                    }
                    else{
                        if (sEnd.getY() <= position.getY() && position.getY() <= sBegin.getY()) {
                            return s;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * If there's traffic, bus slows down by a fraction it's original speed.
     * Slowest a bus can move is 0.4.
     */
    @Override
    public void traffic() {
        double traffic = busOnStreet().getTraffic();
        double currentSpeed;
        if(traffic > 0) {
            System.out.println("vlezl jsem tady ale nemel jsem.");
            currentSpeed = speedSet;
            while(traffic > 0) {
                currentSpeed = currentSpeed - 0.2;
                if(currentSpeed < 0.2) {
                    currentSpeed = 0.4;
                    break;
                }
                traffic = traffic - 1;
            }
            if(atStopFlag == false){
                speed = currentSpeed;
            }
            else {
                speed = 0;
            }

        }
        else {
            if(atStopFlag == false){
                speed = speedSet;
            }
            else {
                speed = 0;
            }
        }
    }

    /**
     * Iterates through all stops along the route and if bus is somewhere
     * really close to a stop, it stops and waits for a while (aka lets passengers
     * in and out. Deviation is used to calculate how close to a stop bus really is.
     */
    @Override
    public void stopAtStop(LocalTime time) {
        for(Stop t : route.getRouteStops()) {
            double deviationX = 0;
            double deviationY = 0;
            deviationX = Math.abs(Math.abs(t.getCoordinates().getX() - Math.abs(position.getX())));
            deviationY = Math.abs(Math.abs(t.getCoordinates().getY() - Math.abs(position.getY())));

            if(deviationX  < 1.6 && deviationY < 1.6){
                if(wait == 0) {
                    if(visited.contains(t.getStopName())){
                        speed = speedSet;
                        break;
                    }
                    else {
                        atStopFlag = true;
                        wait = 6;
                        speed = 0;
                        visited.add(t.getStopName());
                    }

                }
                else {
                    wait = wait - 1;
                    if(wait == 0) {
                        atStopFlag = false;
                        System.out.println(time);
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
        setTimeLocal(timeString);
        route = new Route();
        route.setRoute(line.getStops());
        position = route.getRoute().get(0);
        setGUI();
        speedSet = speed;
        GUI.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            @FXML
            public void handle(MouseEvent event) {
                for(Street s : route.getRouteStreets()){
                    if(! s.getGUI().get(0).getStroke().equals(Color.DARKSLATEGRAY)){
                        s.getGUI().get(0).setStroke(Color.web(line.getColor()));
                        s.getGUI().get(0).setStrokeWidth(1.2);
                    }
                    schedGUI = true;
                    scheduleGUI();
                }
            }
        });
    }

    /**
     * Creates GUI that indicates stops bus already visited
     */
     public void scheduleGUI(){
        sGUI = new ArrayList<>();
        double x = 50;
        double y = 10;

        int t = 0;
        for(Stop p : line.getStops()){
            Text stopNameText = new Text(x, y, p.getStopName());
            t = t + 1;
            if(visited.contains(p.getStopName())){
                stopNameText.setFont(Font.font("Garamond", FontWeight.SEMI_BOLD, 15));
                stopNameText.setStroke(Color.web(line.getColor()));
            }
            else {
                stopNameText.setFont(Font.font("Garamond", FontWeight.LIGHT, 15));
                stopNameText.setStroke(Color.LIGHTGRAY);
            }
            sGUI.add(stopNameText);
            x = x + 60;
        }
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
