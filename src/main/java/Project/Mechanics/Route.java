package Project.Mechanics;

import Project.MapObjects.Stop;
import Project.MapObjects.Street;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a route which a bus will take
 */
public class Route extends Coordinate {
    private List<Coordinate> route;     // list of coordinates representing a route
    private List<Street> routeStreets;  // list of streets on this route
    private List<Stop> routeStops;      // list of stops on this route
    Street thisStreet;                  // current street

    /**
     * Route constructor
     * @param route list of coordinates that makes up a route
     */
    public Route(List<Coordinate> route) {
        this.route = route;
    }

    public Route(){
    }

    /**
     * Gets route
     * @return route
     */
    public List<Coordinate> getRoute() {
        return route;
    }

    /**
     * Creates a list of coordinates which defines a route.
     * Function iterates X times where X is the amount of stops - 1
     * to adjust for the last stop. First it gets starting and ending
     * point of a street on which current stop lies, then starting and
     * ending point of the next street and depending on where the streets
     * are connected, it is decided which coordinate will be added to the
     * route list.
     * After inserting the last coordinate, new lists of stops and streets
     * are created according to the stop list given.
     * @param stops list of stop from which route will be created
     */
    public void setRoute(List<Stop> stops) {
        route = new ArrayList<>();
        Coordinate firstBegin, firstEnd;
        Coordinate secondBegin, secondEnd;
        routeStops = new ArrayList<>();
        routeStreets = new ArrayList<>();

        for(int i = 0; i < stops.size() - 1; i++) {
            firstBegin = stops.get(i).getStreet().getBegin();
            firstEnd = stops.get(i).getStreet().getEnd();
            secondBegin = stops.get(i + 1).getStreet().getBegin();
            secondEnd = stops.get(i + 1).getStreet().getEnd();

            // if it's the same street
            if(firstBegin.equals(secondBegin) && firstEnd.equals(secondEnd)){
                continue;
            }
            // if it's same coordinates bud inverted (B E and E B)
            if(firstBegin.equals(secondEnd) && firstEnd.equals(secondBegin)) {
                continue;
            }
            //if both share beginning or first begins where second ends
            if(firstBegin.equals(secondBegin) || firstBegin.equals(secondEnd)) {
                if(route.size() == 0) {     // if it is the first point of route
                    route.add(firstEnd);
                }
                route.add(firstBegin);
            }
            // if both share ending coordinate or first ends where second begins
            if(firstEnd.equals(secondBegin) || firstEnd.equals(secondEnd)) {
                if(route.size() == 0) {
                    route.add(firstBegin);
                }
                route.add(firstEnd);
            }
        }

        // correctly insert the last stop
        Street temp1 = stops.get(stops.size() - 1).getStreet();
        Coordinate temp2 = route.get(route.size() - 1);
        // if the second to last coordinate is the same as the beginning of the last street
        if(temp2.equals(temp1.getBegin())){
            route.add(temp1.getEnd());      // end coordinate is added
        }
        else {
            route.add(temp1.getBegin());    // else beginning coordinate is added
        }

        // iterate through all stops, add them to a list and add their streets to a list
        for(Stop s : stops){
            routeStops.add(s);
            routeStreets.add(s.getStreet());
        }
    }

    /**
     * Gets all streets on this route
     * @return list of streets
     */
    public List<Street> getRouteStreets() {
        return routeStreets;
    }

    /**
     * Get all stops on this route
     * @return list of stops
     */
    public List<Stop> getRouteStops() {
        return routeStops;
    }

    /**
     * Get current street
     * @return current street
     */
    public Street getThisStreet() {
        return thisStreet;
    }

    /**
     * Sets current street to given street
     * @param thisStreet street to be set as current (this)
     */
    public void setThisStreet(Street thisStreet) {
        this.thisStreet = thisStreet;
    }

    /**
     * Calculate the total distance from first point of a route to the last.
     * Looping for all elements in route list, adding distance between current
     * point and the next one.
     * @return total length of a route
     */
    public double totalDistance(){
        double totalDistance = 0;
        for (int i = 0; i < route.size() - 1; i++){
            totalDistance = totalDistance + calculateDistance(route.get(i), route.get(i + 1));
        }
        return totalDistance;
    }

    /**
     * Calculates next position of a bus. For every coordinate in
     * the route list, two coordinates are taken (current and the next one),
     * and distance between the two of them is calculated. The result
     * is added to total route size. Current size is set and distance
     * driven "soFar" is calculated. With it's help a final position
     * s deretmined and returned.
     * @param distance
     * @return final position of a bus
     */
    public Coordinate nextPosition(double distance){
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
            totalRoute = totalRoute + calculateDistance(one, two);
        }

        if(one == null || two == null) {
            return null;
        }

        // find current street
        for(Street s : routeStreets) {
            Coordinate begin = s.getBegin();
            Coordinate end = s.getEnd();

            if(begin.equals(begin.equals(one)) || begin.equals(two)){
                if(end.equals(one) || end.equals(two)){
                    thisStreet = s;
                    break;
                }
            }
        }

        // calculating final position
        double soFar = (distance - totalRoute) / calculateDistance(one, two);
        finalX = one.getX() + diff(one, two, 1) * soFar;       // get final X coordinate
        finalY = one.getY() + diff(one, two, 0) * soFar;       // get final Y coordinate
        finalPosition = new Coordinate(finalX, finalY);                  // get the final position

        return finalPosition;
    }
}
