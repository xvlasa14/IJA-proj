/**
 * <h1>Project.Mechanics.Main class</h1>
 * This app shows a map with buses, streets
 * and stops. It simulates the movement of
 * buses in time and shows their route.
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @version 1.0
 * @since 04 2020
*/
package Project.Mechanics;

import Project.MapObjects.Bus;
import Project.MapObjects.Map;
import Project.MapObjects.Stop;
import Project.MapObjects.Street;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    /**
     * Starts the stage. Loads the GUI, opens the window,
     * loads elements from YML file.
     * @param primaryStage window with GUI
     * @throws Exception exception that occurs while the application is running
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loadGUI = new FXMLLoader((getClass().getResource("/design.fxml")));  // load the GUI design fxml file
        primaryStage.setScene(new Scene(loadGUI.load()));       // set scene, load the GUI
        primaryStage.show();                                    // show window

        Controller guiController = loadGUI.getController();
        List<Draw> mapObj = new ArrayList<>();

        // loading data from file
        YAMLFactory base = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(base);
        Map map = mapper.readValue(new File("export.yml"), Map.class);
        mapObj.addAll(map.getStreets());
        mapObj.addAll(map.getStops());

        // assigning each stop to its street
        for(Street s : map.getStreets()) {
            for(Stop t : s.getStops()) {
                t.setStreet(map);
            }
        }
        guiController.setElements(mapObj);
        // let out the buses
        for(Bus b : map.getBuses()) {
            b.initBus();
            if(b instanceof Update) {
                guiController.updates.add((Update) b);
            }
            guiController.setMap(map);
          //  System.out.println(map.getStreets());
            guiController.updateTimer(1, guiController);

        }
    }
}
