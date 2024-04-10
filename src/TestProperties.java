import static org.junit.Assert.assertEquals;

import org.junit.*;

public class TestProperties {

    @Test
    public void testBatman() throws Exception {
        Runner runner = new Runner();
        Navigator navigator = runner.run(new String[]{"-p", ".\\Benchmarks\\Batman 6 Jan 2016.xml"});
        Float edgeInvolvement = navigator.identifier.checkEdgeInvolvement(navigator.nta.templates[0].locations[0], navigator.nta.templates[0].transitions);
        assertEquals(1.0f, (float)edgeInvolvement, 0);
    }
}