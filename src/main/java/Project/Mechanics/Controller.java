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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    @FXML
    private Pane content;           // map space
    @FXML
    private ToggleButton speedUp;   // button for speeding up
    @FXML
    private ToggleButton pause;     // button for pausing
    @FXML
    private Label timeGUI;          // label for clock

    private List<Draw> elements = new ArrayList<>();    // all elements that will end up on the map
    private Timer timer;        // timer
    private LocalTime time = LocalTime.of(6,0,0); // sets time to 6 AM
    private LocalTime endTime = LocalTime.of(00,00,00); // set ending time to midnight
    public List<Update> updates = new ArrayList<>();   // list of updates
    private DateFormat format = new SimpleDateFormat("HH:mm:ss");   // format in which time will be displayed

    private List<Bus> activeBuses = new ArrayList<>();
    private Map map;
    long speed = 1;
    int paused = 1;

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
        if (speed == 6) {
            speed = 1;
        }
        else {
            speed = 6;
        }
        updateTimer(speed, this);
    }

    /**
     * Pause all bus movement that is happening on the map
     */
    @FXML
    private void pause() {
        paused = paused + 1;
        if(paused % 2 == 0) {
            timer.cancel();
        }
        else {
            updateTimer(speed, this);
        }
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
     * TO DO DOKUMENTACE TETO FUNKCE
     * @param speed
     * @param timeController
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
                             //update.traffic();
                            update.stopAtStop();
                        });
                    });
                }
                time = time.plusSeconds(30);
            }
        }, 0, 200 / speed);

    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public List<Bus> getActiveBuses() {
        return activeBuses;
    }
}

