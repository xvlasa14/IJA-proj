package Project.Mechanics; /**
 * <h2>Design Project.Mechanics.Controller</h2>
 * Controls all events that happen during the
 * use of the GUI.
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */

import Project.MapObjects.Draw;
import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    @FXML
    private Pane content;
    private List<Draw> elements = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.now();
    private List<Update> updates = new ArrayList<>();


    /* - - - - - - - - - - - - - - - - - - ZOOM - - - - - - - - - - - - - - - - - - */
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

    public void setElements(List<Draw> elements) {
        this.elements = elements;
        for (Draw draw : elements) {
            content.getChildren().addAll(draw.getGUI());
            if(draw instanceof Update){
                updates.add((Update) draw);
            }
        }
    }

    public void updateTimer(){
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds((long) 0.5);
                for(Update update :updates){
                    update.update(time);
                }
            }
        }, 0, 500);

    }

}
