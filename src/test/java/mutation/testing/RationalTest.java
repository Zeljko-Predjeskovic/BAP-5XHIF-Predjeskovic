package mutation.testing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class RationalTest {
    @Test
    public void singleOf() {
        var r = Rational.of(7L);

        assertEquals(7L, r.numerator());
        assertEquals(1L, r.denominator());
    }

    @Test
    public void dualOf() {
        var r = Rational.of(4L, 6L);

        assertEquals(2L, r.numerator());
        assertEquals(3L, r.denominator());
    }

    @Test
    public void negate() {
        assertEquals(Rational.of(-7L), Rational.of(7L).negate());
    }

    @Test
    public void reciprocal() {
        assertEquals(Rational.of(1L, 7L), Rational.of(7L).reciprocal());
    }

    @Test
    public void plus() {
        assertEquals(Rational.of(1L), Rational.plus(Rational.of(1L, 2L), Rational.of(1L, 2L)));
    }

    @Test
    public void minus() {
        assertEquals(Rational.of(1L, 4L), Rational.minus(Rational.of(1L, 2L), Rational.of(1L, 4L)));
    }

    @Test
    public void times() {
        assertEquals(Rational.of(1L, 4L), Rational.times(Rational.of(1L, 2L), Rational.of(1L, 2L)));
    }

    @Test
    public void divide() {
        assertEquals(Rational.of(1L, 4L), Rational.divide(Rational.of(1L, 2L), Rational.of(2L)));
    }
}