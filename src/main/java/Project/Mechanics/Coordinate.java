/**
 * <h2>Coordinate</h2>
 * @author Ales Jaksik (xjaksi01)
 * @author Nela Vlasakova (xvlasa14)
 * @since 04 2020
 */
package Project.Mechanics;

import java.util.Objects;

/**
 * Class that handles coordinates and location-base calculations
 */
public class Coordinate {
    // x and y coordinates
    private double x;
    private double y;

    /**
     * Coodinate constructor
     * @param x coordinate on the x axis
     * @param y coordinate on the y axis
     */
    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Coordinate() {
    }

    /**
     * Get X coorrdinate
     * @return coordinate X
     */
    public double getX() {
        return x;
    }

    /**
     * Get Y coordinate
     * @return coordinate Y
     */
    public double getY() {
        return y;
    }

    /**
     * Set X coordinate
     * @param x coordiante
     */
    public void setX(double x) {
        if (x < 0) {
            this.x = 0;
        }
        else {
            this.x = x;
        }
    }

    /**
     * Set Y coordinates
     * @param y coordinate to be set
     */
    public void setY(double y) {
        if(y < 0) {
            this.y = 0;
        }
        else {
            this.y = y;
        }
    }

    /**
     * Calculates the difference of two coordinates, either X or Y
     * @param one first coordiante (pair)
     * @param two second coordinate (pair)
     * @param type X or Y (1 or any other number)
     * @return X2 - x1 or Y2 - Y1
     */
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

    /**
     * Calculates the distance between two points
     * @param one first point
     * @param two second point
     * @return their distance
     */
    double calculateDistance(Coordinate one, Coordinate two){
        double t1 = Math.pow(diff(one, two, 1), 2);
        double t2 = Math.pow(diff(one, two, 0), 2);
        return Math.sqrt( t1 + t2);
    }


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
