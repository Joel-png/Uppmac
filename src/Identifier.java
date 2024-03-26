import java.util.ArrayList;

public class Identifier {
    Nta nta;
    ArrayList<String> visitedLocationIDs = new ArrayList<String>();

    public Identifier(Nta nta) {
        this.nta = nta;
    }

    public void printLocationProperties(Location location, Transition[] transitions) {
        System.out.println("Location has edge involvement % of: " + checkEdgeInvolvement(location, transitions) * 100 + "%");
    }

    public void printTemplateProperties(Location[] locations, Transition[] transitions) {
        if (checkIsLinear(locations)) {
            System.out.println("Template is linear");
        } else {
            System.out.println("Template is not linear");
        }
    }

    // returns the % of edges involved with a given location
    public float checkEdgeInvolvement(Location location, Transition[] transitions) {
        if (transitions.length == 0) {
            return 0;
        }
        String matchID = location.id;
        int counter = 0;
        for (int i = 0; i < transitions.length; i++) {
            // if id matches either of the transition source target ids
            if (transitions[i].source.equals(matchID) || transitions[i].target.equals(matchID)) {
                counter++;
            }
        }
        System.out.println(counter);
        System.out.println(transitions.length);
        float percent = counter/(float)transitions.length;
        return percent;
    }


    public boolean checkIsLinear(Location[] locations) {
        int startingLocationIndex = getStartingNode(locations);
        if (startingLocationIndex == -1) {
            return false;
        }

        visitedLocationIDs = new ArrayList<String>();

        return checkLocationIsLinear(locations[startingLocationIndex], locations);

    }

    public boolean checkLocationIsLinear(Location location, Location[] locations) {
        if (location.sourceTransitions.size() == 0) {
            return true;
        }

        for (int i = 0; i < location.sourceTransitions.size(); i++) {
            Transition currentTransition = location.sourceTransitions.get(i);
            if (!locationVisited(location)) {
                if (!checkLocationIsLinear(fetchLocationFromID(currentTransition.target, locations), locations)) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public boolean locationVisited(Location location) {
        for (int i = 0; i < visitedLocationIDs.size(); i++) {
            if (visitedLocationIDs.get(i).equals(location.id)) {
                return true;
            }
        }
        visitedLocationIDs.add(location.id);
        return false;
    }

    // returns node that has no target transitions, returns -1 if no such node exists
    public int getStartingNode(Location[] locations) {
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].sourceTransitions.size() == 0) {
                return i;
            }
        }
        return -1;
    }

    public Location fetchLocationFromID(String id, Location[] locations) {
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].id.equals(id)) {
                return locations[i];
            }
        }
        System.out.println("UNABLE TO FIND LOCATION FROM ID");
        return new Location("ERROR", "ERROR");
    }
}
