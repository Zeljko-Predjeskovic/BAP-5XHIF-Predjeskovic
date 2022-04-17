package mutation.testing;

import junit.framework.TestCase;

import java.util.Optional;

public class RangeTest extends TestCase {

    public void testScale() {
        assertEquals(new Range(2L, 2L), new Range(1L, 1L).scale(2L));
    }

    public void testTranslate() {
        assertEquals(new Range(2L, 2L), new Range(1L, 1L).translate(1L));
    }

    public void testCover() {
        assertEquals(new Range(1L, 4L), Range.cover(new Range(1L, 2L), new Range(3L, 4L)));
    }

    public void testContains() {
        assertTrue(new Range(1L, 3L).contains(2L));
    }

    public void testContainsConditionalBoundaries() {
        assertTrue(new Range(1L, 3L).contains(3L));
        assertTrue(new Range(1L, 3L).contains(1L));
    }

    public void testIntersect() {
        assertEquals(Optional.of(new Range(2L, 3L)), Range.intersect(new Range(1L, 3L), new Range(2L, 4L)));
    }

}