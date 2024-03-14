public class Template {
    String name;
    Declaration declaration;
    Location[] locations;
    Transition[] transitions;

    public Template(String name, int locationsLength, int transitionsLength) {
        this.name = name;
        this.locations = new Location[locationsLength];
        this.transitions = new Transition[transitionsLength];
    }

    public void addLocation(Location location, int index) {
        locations[index] = location;
    }

    public void addTransition(Transition transition, int index) {
        transitions[index] = transition;
    }

    public void printData() {
        System.out.println("Template: " + name + ", " + String.valueOf(locations.length) + " locations, " + String.valueOf(transitions.length) + " transitions");
    }

    public Location fetchLocationFromID(String id) {
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].id.equals(id)) {
                return locations[i];
            }
        }
        System.out.println("UNABLE TO FIND LOCATION FROM ID");
        return new Location("ERROR", "ERROR");
    }

    public void updateTemplateWithTransitions(Transition transition) {
        fetchLocationFromID(transition.source).addSource(transition);
        fetchLocationFromID(transition.target).addTarget(transition);
    }

    public void printLocations() {
        for (int i = 0; i < locations.length; i++) {
            System.out.print("[" + i + "] ");
            locations[i].printData();
        }
    }
}
