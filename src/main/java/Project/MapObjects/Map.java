package Project.MapObjects;

import Project.Mechanics.Line;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private List<Street> streets;
    private List<Stop> stops;
    private List<Bus> buses;
    private List<Line> lines;

    /**
     * Creates new lists for all elements of a map
     */
    public Map() {
        streets = new ArrayList<>();
        stops = new ArrayList<>();
        buses = new ArrayList<>();
        lines = new ArrayList<>();
    }

    /**
     * Gets all streets
     * @return streets (in a list)
     */
    public List<Street> getStreets() {
        return streets;
    }

    /**
     * Sets all streets
     * @param streets list of streets to be set
     */
    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    /**
     * Gets all stops
     * @return list of stops
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * Sets stops
     * @param stops list of stops to be set
     */
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    /**
     * Gets buses
     * @return list of all buses
     */
    public List<Bus> getBuses() {
        return buses;
    }

    /**
     * Assigns list of buses
     * @param buses list of buses to be set
     */
    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }

    /**
     * Gets all lines
     * @return list of lines
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * Sets lines
     * @param lines list of lines to be set
     */
    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "MAP ELEMENTS:" + "\n" +
                "   streets: " + streets + "\n" +
                "     stops: " + stops + "\n" +
                "     lines: " + lines + "\n" +
                "     buses: " + buses;
    }
}
