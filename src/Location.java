import java.util.ArrayList;

public class Location {
    String id;
    String name;
    String label;
    ArrayList<Transition> sourceTransitions = new ArrayList<Transition>();
    ArrayList<Transition> targetTransitions = new ArrayList<Transition>();

    public Location(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void printData() {
        System.out.println("Location: " + name + ", ID: " + id + ", Source:" + String.valueOf(sourceTransitions.size()) + ", Target:" + String.valueOf(targetTransitions.size()));
    }

    public void addSource(Transition transition) {
        sourceTransitions.add(transition);
    }

    public void addTarget(Transition transition) {
        targetTransitions.add(transition);
    }

    public void printTransitions() {
        System.out.println("Source transitions, length: " + sourceTransitions.size());
        for (int i = 0; i < sourceTransitions.size(); i++) {
            System.out.print("[" + i + "] ");
            sourceTransitions.get(i).printData();
        }

        System.out.println("Target transitions, length: " + targetTransitions.size());
        for (int i = 0; i < targetTransitions.size(); i++) {
            System.out.print("[" + i + "] ");
            targetTransitions.get(i).printData();
        }
    }
}
