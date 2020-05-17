package Project.Mechanics;

import java.time.LocalTime;

/**
 * Updates in time
 */
public interface Update {
    /**
     * Update when in time something happens (movement etc)
     * @param time current time within the app
     * @param busController controller
     */
    void update(LocalTime time, Controller busController);

    /**
     * Handles traffic events (when it is increased or decreased)
     */
    void traffic();

    /**
     * Takes care of calculating if a bus is on stop
     * @param time current time
     */
    void stopAtStop(LocalTime time);
}
