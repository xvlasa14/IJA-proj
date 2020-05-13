package Project.Mechanics;

import Project.MapObjects.Stop;

import java.util.List;

/**
 * Class that holds information about line.
 * Line is a group of buses that go on a
 * certain route.
 */
public class Line {
    private Integer lineID;     // lines ID
    private String color;       // color of the buses that belong to this line
    private List<Stop> stops;   // stops of this line

    /**
     * Line constructor
     * @param lineID id of the line. Integer
     * @param color color of the buses in this line
     * @param stops stops on the line
     */
    public Line(Integer lineID, String color, List<Stop> stops) {
        this.lineID = lineID;
        this.color = color;
        this.stops = stops;
    }

    /**
     * Gets line ID
     * @return line ID
     */
    public Integer getLineID() {
        return lineID;
    }

    /**
     * sets line ID
     * @param lineID line ID to be set
     */
    public void setLineID(Integer lineID) {
        this.lineID = lineID;
    }

    /**
     * Gets the color of the line bus
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Set color
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}
