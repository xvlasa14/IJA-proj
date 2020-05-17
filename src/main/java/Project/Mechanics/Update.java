package Project.Mechanics;

import java.time.LocalTime;

public interface Update {
    void update(LocalTime time, Controller busController);
    void traffic();
    void stopAtStop(LocalTime time);
}
