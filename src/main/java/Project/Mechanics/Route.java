package Project.Mechanics;

import java.util.List;

public class Route {
    List<Coordinate> route;

    public Route(List<Coordinate> route) {
        this.route = route;
    }

    public List<Coordinate> getRoute() {
        return route;
    }

    public void setRoute(List<Coordinate> route) {
        this.route = route;
    }

    // difference of two coordinates, for x and y
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

    private double calculateDistance(Coordinate one, Coordinate two){
        double t1 = Math.pow(diff(one, two, 1), 2);
        double t2 = Math.pow(diff(one, two, 0), 2);
        return Math.sqrt( t1 + t2);
    }

    public Coordinate calculateRoute(double distance){
        double totalRoute = 0;  // total length of the route
        double finalX, finalY;
        Coordinate finalPosition = null; // final position of the bus
        Coordinate one = null;  // first coordinate
        Coordinate two = null;  // second coordinate

        // loop while i is less than the total route size
        for (int i = 0; i < route.size() - 1; i++){
            one = route.get(i);
            two = route.get(i + 1);

            if (totalRoute + calculateDistance(one, two) > distance) {
                break;
            }
            totalRoute += calculateDistance(one, two);
        }
        double soFar = (distance - totalRoute) / calculateDistance(one, two);
        finalX = one.getX() + diff(one, two, 1) * soFar;       // get final X coordinate
        finalY = one.getY() + diff(one, two, 0) * soFar;       // get final Y coordinate
        finalPosition = new Coordinate(finalX, finalY);                  // get the final position

        System.out.println(finalPosition);

        if (route.get(route.size() - 1).getX() < finalPosition.getX() || route.get(route.size() - 1).getY() < finalPosition.getY()){
            return route.get(route.size() - 1);
        }
        return finalPosition;
    }
}
