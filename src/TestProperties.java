import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.*;

public class TestProperties {

    @Test
    public void testBatman() throws Exception {
        Runner runner = new Runner();
        Navigator navigator = runner.run(new String[]{"-p", ".\\Benchmarks\\Batman 6 Jan 2016.xml"});
        Float edgeInvolvement = navigator.identifier.checkDegreePresence(navigator.nta.templates[0].locations[0], navigator.nta.templates[0].transitions);
        assertEquals(1.0f, (float)edgeInvolvement, 0);
    }

    @Test
    public void arrayListTest() {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        addToArrayList(ints);
        assertEquals(ints.size(), 1);
    }

    public void addToArrayList(ArrayList<Integer> ints) {
        ints.add(1);
    }
}