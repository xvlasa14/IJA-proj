/**
 * <h2>Design Project.Mechanics.Controller</h2>
 * Controls all events that happen during the
 * use of the GUI.
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.Mechanics;

import Project.MapObjects.Bus;
import Project.MapObjects.Map;
import Project.MapObjects.Street;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controllers that handle events and GUI
 */
public class Controller {
    @FXML
    private Pane content;           // map space
    @FXML
    private Label timeGUI;          // label for clock

    private List<Draw> elements = new ArrayList<>();    // all elements that will end up on the map
    private Timer timer;        // timer
    private LocalTime time = LocalTime.of(6,0,0); // sets time to 6 AM
    private LocalTime endTime = LocalTime.of(23,59,59); // set ending time to midnight
    public List<Update> updates = new ArrayList<>();   // list of updates
    private DateFormat format = new SimpleDateFormat("HH:mm:ss");   // format in which time will be displayed
    private List<Bus> activeBuses = new ArrayList<>();
    private Map map;
    long speed = 3;
    int paused = 1;
    @FXML
    private AnchorPane stopList;
    @FXML
    private Pane schedule;

    public Controller() {
    }


    /**
     * When scroll event happens, whole map can be zoomed
     * in or out.
     * @param scroll
     */
    @FXML
    private void zoom(ScrollEvent scroll){
        scroll.consume();                   // set it only for the scroll pane
        double delta = scroll.getDeltaY();   // get the difference after scroll
        double zoom = 1;


        // assign different values to set scale
        if (delta > 0) {
            zoom = 1.1;
        }
        else if (delta < 0) {
            zoom = 0.9;
        }
        // scale
        content.setScaleX(zoom * content.getScaleX());
        content.setScaleY(zoom * content.getScaleY());
        content.layout();
    }

    /**
     * Speeding up or slowing back down all buses
     */
    @FXML
    private void speedUp() {
        timer.cancel();
        if (speed == 8) {
            speed = 3;
        }
        else {
            speed = 8;
        }
        updateTimer(speed, this);
    }

    /**
     * Pause all bus movement that is happening on the map
     */
    @FXML
    private void nextDay() {
        resetTime();

    }

    /**
     * Resets time when default end of day time occurs. Removes
     * all stops from
     */
    private void resetTime() {
        schedule.getChildren().removeAll(schedule.getChildren());
        for(Bus b : getActiveBuses()){
            b.setDistance(0);
            content.getChildren().removeAll(b.getGUI());
            b.setSpeed(b.getSpeed());
            b.initBus();
            b.visited.clear();
        }
        for(Update u : updates) {
            if(u instanceof Bus) {
                ((Bus) u).setDistance(0);
                ((Bus) u).initBus();
            }
        }
        for(int i = 0; i < map.getStreets().size(); i++){
            map.getStreets().get(i).setGUI();
            map.getStreets().get(i).setTraffic(0);
        }
        getActiveBuses().clear();
        time = LocalTime.of(6,00, 00);
        timer.cancel();
        updateTimer(1, this);
    }

    /**
     * Iterates through all drawable elements and "draws" them on the map.
     * If said elements implements interface Update, update is added
     * @param elements all elements that will be added to the map
     */
    public void setElements(List<Draw> elements) {
        this.elements = elements;
        for (Draw draw : elements) {
            content.getChildren().addAll(draw.getGUI());
            if(draw instanceof Update){
                updates.add((Update) draw);
            }
        }
    }

    /**
     * Adds a bus on the map
     * @param bus bus to be added
     */
    public void addBus(Draw bus) {
        content.getChildren().addAll(bus.getGUI());
    }

    /**
     * Removes bus from the map
     * @param bus bus to be removed
     */
    public void removeBus(Draw bus) {
        content.getChildren().removeAll(bus.getGUI());
    }

    /**
     * GUI of the clock that will be displayed
     * @param time
     */
    public void timeGUI(LocalTime time) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            timeGUI.setText(time.format(formatter));
    }

    /**
     * Updates timers, takes care of adding 30 secongs every second
     * @param speed can be slowed down/speeded up by this number
     * @param timeController controls things that happen with time
     */
    public void updateTimer(long speed, Controller timeController){
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                        timeGUI(time);
                });
                for(Update update : updates){
                    Platform.runLater(()->{
                        update.update(time, timeController);
                        Platform.runLater(()->{
                            update.stopAtStop(time);
                            update.traffic();
                        });
                    });
                }
                time = time.plusSeconds(30);
            }
        }, 0, 200 / speed);

    }

    /**
     * Draws GUI for bus line schedules
     * @param lineStops
     */
    public void drawGUI(List<Shape> lineStops){
        stopList.getChildren().removeAll(stopList.getChildren());
        for(Shape s : lineStops) {
            stopList.getChildren().addAll(s);
        }
    }

    /**
     * Gets gui for line schedule
     * @param scheduleList list of stop names
     */
    public void getS(List<Shape> scheduleList){
        schedule.getChildren().removeAll(schedule.getChildren());
        for(Shape s : scheduleList){
            schedule.getChildren().addAll(s);
        }
    }
    /**
     * Resets traffic when button is pushed
     */
    @FXML
    private void resetTraffic(){
        for(Street s : map.getStreets()){
            s.setTraffic(0);
            s.getGUI().get(0).setStrokeWidth(1.2);   // set the width of the line
        }
    }

    /**
     * Sets map
     * @param map map to be set
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Gets map
     * @return map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Gets active buses (buses on route)
     * @return list of buses
     */
    public List<Bus> getActiveBuses() {
        return activeBuses;
    }
}

