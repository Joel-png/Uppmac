package com.uppmacparser;
public class Transition {
    String source;
    String target;
    String label;
    String id;

    public Transition(String source, String target, String id) {
        this.source = source;
        this.target = target;
        this.id = id;
    }

    public void printData() {
        System.out.println("Transition, id: " + id + ", Source:" + source + ", Target:" + target);
    }
}
