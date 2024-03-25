public class Identifier {
    Nta nta;

    public Identifier(Nta nta) {
        this.nta = nta;
    }

    public void printLocationProperties(Location location, Transition[] transitions) {
        System.out.println("Location has edge involvement % of: " + checkEdgeInvolvement(location, transitions) * 100 + "%");
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

}
