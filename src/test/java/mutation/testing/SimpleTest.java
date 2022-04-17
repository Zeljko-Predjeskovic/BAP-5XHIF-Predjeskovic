package mutation.testing;


import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleTest {
    @Test
    public void testMethod() {
        var simple = new Simple();
        assertEquals(33L, simple.test(33L));
    }

    @Test
    public void testMethodConditionalBoundary(){
        var simple = new Simple();
        assertEquals(10L, simple.test(10L));

    }
}