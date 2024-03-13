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

    public void printData() {
        System.out.println("Template: " + name + ", " + String.valueOf(locations.length) + " locations");
    }
}
