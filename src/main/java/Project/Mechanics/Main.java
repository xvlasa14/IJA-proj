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
import Project.MapObjects.Draw;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    /**
     * Starts the stage. Loads the GUI, opens the window,
     * loads elements.
     * @param primaryStage primary stage where the GUI happens
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loadGUI = new FXMLLoader((getClass().getResource("/design.fxml")));  // load the GUI design fxml file
        primaryStage.setScene(new Scene(loadGUI.load()));       // set scene, load the GUI
        primaryStage.show();                                    // show window

        Controller guiController = loadGUI.getController();
        List<Draw> mapObj = new ArrayList<>();

        Coordinate pointOne = new Coordinate(100, 100);
        Coordinate pointTwo = new Coordinate(200, 200);
        Coordinate pointThree = new Coordinate(700, 700);

        Route newRoute = new Route(Arrays.asList(pointOne, pointTwo, pointThree));

        mapObj.add(new Street("Street", pointOne, pointThree));
        mapObj.add(new Bus(pointOne, 5, newRoute));

        guiController.setElements(mapObj);
        guiController.updateTimer(1);
    }
}
