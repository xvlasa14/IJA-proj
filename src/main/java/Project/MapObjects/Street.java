package Project.MapObjects;

import Project.Mechanics.Coordinate;
import Project.Mechanics.Draw;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class Street implements Draw {
    private Coordinate begin;
    private Coordinate end;
    private String streetName;
    private double traffic;
    private List<Stop> stops;

    public Street(Coordinate begin, Coordinate end, String streetName, double traffic, List<Stop> stops) {
        this.begin = begin;
        this.end = end;
        this.streetName = streetName;
        this.traffic = traffic;
        this.stops = stops;
    }

    public Coordinate getBegin() {
        return begin;
    }

    public Coordinate getEnd() {
        return end;
    }

    public String getStreetName() {
        return streetName;
    }

    public double getTraffic() {
        return traffic;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setBegin(Coordinate begin) {
        this.begin = begin;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Override
    public List<Shape> getGUI() {
        double xText = (begin.getX() + end.getX()) / 2;
        double yText = ((begin.getY() + end.getY()) / 2) - 5;
        return Arrays.asList(new Text(xText, yText, streetName),new Line(begin.getX(), begin.getY(), end.getX(), end.getY()));
    }
}
