import java.util.ArrayList;

public class Identifier {
    Nta nta;
    ArrayList<String> visitedLocationIDs = new ArrayList<String>();

    public Identifier(Nta nta) {
        this.nta = nta;
    }

    public void printLocationProperties(Location location, Location[] locations, Transition[] transitions, int indent) {
        Navigator.indent(indent);
        System.out.println("Location has degree involvement % of: " + checkDegreeInvolvement(location, transitions) * 100 + "%");
        Navigator.indent(indent);
        System.out.println("Location has " + checkAdjustedUniqueLoops(location, locations) + " adjusted unique loops");
    }

    public void printTemplateProperties(Location[] locations, Transition[] transitions, int indent) {
        if (checkIsLinear(locations)) {
            Navigator.indent(indent);
            System.out.println("Template is linear");
        } else {
            Navigator.indent(indent);
            System.out.println("Template is not linear");
        }
    }

    // returns the % of edges involved with a given location
    public float checkDegreeInvolvement(Location location, Transition[] transitions) {
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
        //System.out.println(counter);
        //System.out.println(transitions.length);
        float percent = counter/(float)transitions.length;
        return percent;
    }

    public int checkAdjustedUniqueLoops(Location startingLocation, Location[] locations) {
        ArrayList<String> visitedTransitions = new ArrayList<String>();
        return getUniqueLoop(startingLocation, startingLocation, locations, cloneTransitionList(visitedTransitions));
    }

    public int getUniqueLoop(Location targetLocation, Location currentLocation, Location[] locations, ArrayList<String> visitedTransitions) {
        int loopCounter = 0;
        for (int i = 0; i < currentLocation.sourceTransitions.size(); i++) {
            if (!transitionVisited(currentLocation.sourceTransitions.get(i), visitedTransitions)) {
                // if transition leads to target, complete a loop count
                if (currentLocation.sourceTransitions.get(i).target.equals(targetLocation.id)) {
                    loopCounter++;
                } else if (!currentLocation.sourceTransitions.get(i).target.equals(currentLocation.id)) {
                    loopCounter += getUniqueLoop(targetLocation, fetchLocationFromID(currentLocation.sourceTransitions.get(i).target, locations), locations, cloneTransitionList(visitedTransitions));
                }
            }
        }
        return loopCounter;
    }

    public ArrayList<String> cloneTransitionList(ArrayList<String> transitions) {
        ArrayList<String> clonedList = new ArrayList<String>(transitions.size());
        for (String transition : transitions) {
            clonedList.add(transition);
        }
        return clonedList;
    }

    public boolean transitionVisited(Transition transition, ArrayList<String> visitedTransitions) {
        for (int i = 0; i < visitedTransitions.size(); i++) {
            //System.out.println(visitedTransitions.size());
            //System.out.println(visitedTransitions.get(i));
            if (visitedTransitions.get(i).equals(transition.id)) {
                return true;
            }
        }
        visitedTransitions.add(transition.id);
        return false;
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
            if (locations[i].targetTransitions.size() == 0) {
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
