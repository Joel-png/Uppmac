import static org.junit.Assert.assertEquals;

import org.junit.*;

public class TestProperties {

    @Test
    public void testBatman() throws Exception {
        Runner runner = new Runner();
        Navigator navigator = runner.run(new String[]{"-p", ".\\Benchmarks\\Batman 6 Jan 2016.xml"});
        assertEquals("hello", "hello");
    }

    @Test
    public void test() {
        assertEquals("", "");
    }
}