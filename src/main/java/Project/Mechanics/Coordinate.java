/**
 * <h2>Coordinate</h2>
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.Mechanics;

import java.util.Objects;

public class Coordinate {
    // x and y coordinates
    private double x;
    private double y;

    // create coordinates
    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Coordinate() {

    }

    // get x coordinate
    public double getX() {
        return x;
    }

    // get y coordinate
    public double getY() {
        return y;
    }

    // set x coordinate
    public void setX(double x) {
        if (x < 0) {
            this.x = 0;
        }
        else {
            this.x = x;
        }
    }

    // set y coordinate
    public void setY(double y) {
        if(y < 0) {
            this.y = 0;
        }
        else {
            this.y = y;
        }
    }

    public double diff(Coordinate one, Coordinate two, int type) {
        // if type is X
        double result = 0;
        if(type == 1) {
            result = two.getX() - one.getX();
        }
        else {
            result = two.getY() - one.getY();
        }
        return result;
    }

    double calculateDistance(Coordinate one, Coordinate two){
        double t1 = Math.pow(diff(one, two, 1), 2);
        double t2 = Math.pow(diff(one, two, 0), 2);
        return Math.sqrt( t1 + t2);
    }

    // return string with coordinates
    public String toString() {
        return "Coordinates: " + "x = " + x + ", y = " + y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
