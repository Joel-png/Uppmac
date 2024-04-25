package com.uppmacparser;

import java.io.IOException;
import java.util.ArrayList;


public class Identifier {
    Nta nta;
    ArrayList<String> visitedLocationIDs = new ArrayList<String>();
    ArrayList<TemplateProperty> templateProperties = new ArrayList<TemplateProperty>();
    ExcelWriter excelWriter;

    public Identifier(Nta nta, ExcelWriter excelWriter) {
        this.nta = nta;
        this.excelWriter = excelWriter;
    }


    public void outputNtaTemplateProperties() throws IOException, InterruptedException{
        excelWriter.writeRow(new Object[] {nta.name}); // print name of nta
        for (int i = 0; i < templateProperties.size(); i++) {
            TemplateProperty currentTemplateProperty = templateProperties.get(i);

            int numOfPoplarLocations = currentTemplateProperty.numOfPoplarLocations;
            String isLinear = "no";
            String hasLonelyInit = "";
            String isSingleLocation = "no";

            // System.out.println();
            // System.out.println();
            // System.out.println();
            // System.out.println(currentTemplateProperty.template.name);
            
            // Navigator.indent(1);
            // System.out.println("Has " + numOfPoplarLocations + " popular locations (75-100% degree presences)");

            if (currentTemplateProperty.isLinear) {
                // Navigator.indent(1);
                // System.out.println("Linear");
                isLinear = "yes";
            } else {
                // Navigator.indent(1);
                // System.out.println("Not Linear");
                isLinear = "no";
            }

            if (currentTemplateProperty.lonelyInit) {
                // Navigator.indent(1);
                // System.out.println("Lonely init");
                hasLonelyInit = "yes";
            }

            if (currentTemplateProperty.singleLocation) {
                // Navigator.indent(1);
                // System.out.println("Single location");
                isSingleLocation = "yes";
            }

            excelWriter.writeRow(new Object[] {"", currentTemplateProperty.template.name, String.valueOf(numOfPoplarLocations), isLinear, hasLonelyInit, isSingleLocation});
        }
        excelWriter.writeRow(new Object[] {""});
    }

    public void printLocationProperties(Location location, Location[] locations, Transition[] transitions, int indent) {
        Navigator.indent(indent);
        System.out.println("Location has in-out degree presence % of: " + checkDegreePresence(location, transitions) * 100 + "%");
        Navigator.indent(indent);
        System.out.println("Location has " + checkAdjustedUniqueLoops(location, locations) + " adjusted unique loops");
    }

    public void printTemplateProperties(Template template, int indent) {
        Location[] locations = template.locations;
        Transition[] transitions = template.transitions; 
        Location init = template.getInit();
        TemplateProperty tempTemplateProperty = new TemplateProperty(template);

        if (checkIsLinear(locations, init)) {
            Navigator.indent(indent);
            System.out.println("Template is linear");
            tempTemplateProperty.isLinear = true;
        } else {
            Navigator.indent(indent);
            System.out.println("Template is not linear");
        }

        if (init.sourceTransitions.size() <= 1 && init.targetTransitions.size() == 0) {
            Navigator.indent(indent);
            System.out.println("Template has a lonely init location");
            tempTemplateProperty.lonelyInit = true;
        }

        Navigator.indent(indent);
        int popularLocations = getNumOfPopularLocations(locations, transitions);
        System.out.println("Template has " + popularLocations + " popular locations (75-100% degree presences)");
        tempTemplateProperty.numOfPoplarLocations = popularLocations;

        int singleLocationState = isSingleLocation(locations);
        if (singleLocationState == 1) {
            System.out.println();
            Navigator.indent(indent);
            System.out.println("Template has a single location with no transitions");
        } else if (singleLocationState == 2) {
            System.out.println();
            Navigator.indent(indent);
            System.out.println("Template has a single location with transitions");
        }


        templateProperties.add(tempTemplateProperty);
    }

    public int getNumOfPopularLocations(Location[] locations, Transition[] transitions) {
        int counter = 0;
        for (int i = 0; i < locations.length; i++) {
            if (checkDegreePresence(locations[i], transitions) >= 0.75) {
                counter++;
            }
        }
        return counter;
    }

    // returns 1 if single location, returns 2 if single location with transitions, returns 0 if not single
    public int isSingleLocation(Location[] locations) {
        if (locations.length == 1) {
            if (locations[0].sourceTransitions.size() + locations[0].targetTransitions.size() > 0) {
                return 2;
            }
            return 1;
        }
        return 0;
    }

    // returns the % of edges involved with a given location
    public float checkDegreePresence(Location location, Transition[] transitions) {
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

    public boolean checkIsLinear(Location[] locations, Location init) {
        visitedLocationIDs = new ArrayList<String>();

        return checkLocationIsLinear(init, locations);

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