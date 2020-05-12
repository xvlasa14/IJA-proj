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
    private String name;

    public Street(String name, Coordinate begin, Coordinate end) {
        this.begin = begin;
        this.end = end;
        this.name = name;
    }

    @Override
    public List<Shape> getGUI() {
        double xText = (begin.getX() + end.getX()) / 2;
        double yText = ((begin.getY() + end.getY()) / 2) - 5;
        return Arrays.asList(new Text(xText, yText, name),new Line(begin.getX(), begin.getY(), end.getX(), end.getY()));
    }
}
