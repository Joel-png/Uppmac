public class Transition {
    String source;
    String target;
    String label;

    public Transition(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public void printData() {
        System.out.println("Location, Source:" + source + ", Target:" + target);
    }
}
