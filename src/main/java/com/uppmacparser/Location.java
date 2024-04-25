package com.uppmacparser;
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

    public void printData(int indent) {
        Navigator.indent(indent);
        System.out.println("Location: " + name + ", ID: " + id + ", Source:" + String.valueOf(sourceTransitions.size()) + ", Target:" + String.valueOf(targetTransitions.size()));
    }

    public void addSource(Transition transition) {
        sourceTransitions.add(transition);
    }

    public void addTarget(Transition transition) {
        targetTransitions.add(transition);
    }

    public void printTransitions(int indent) {
        Navigator.indent(indent);
        System.out.println("Source transitions, length: " + sourceTransitions.size());
        for (int i = 0; i < sourceTransitions.size(); i++) {
            Navigator.indent(indent);
            System.out.print("[" + i + "] ");
            sourceTransitions.get(i).printData();
        }

        Navigator.indent(indent);
        System.out.println("Target transitions, length: " + targetTransitions.size());
        for (int i = 0; i < targetTransitions.size(); i++) {
            Navigator.indent(indent);
            System.out.print("[" + i + "] ");
            targetTransitions.get(i).printData();
        }
    }
}
