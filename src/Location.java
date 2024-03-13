import java.util.ArrayList;

public class Location {
    String id;
    String name;
    Label label;
    ArrayList<Transition> sourceTransitions = new ArrayList<Transition>();
    ArrayList<Transition> targetTransitions = new ArrayList<Transition>();

    public Location(String id, String name) {

    }
}
