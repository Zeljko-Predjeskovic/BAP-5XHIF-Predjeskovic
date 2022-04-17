package mutation.testing;

public record Rational(long numerator, long denominator) {
    public Rational {
        if (denominator < 0) {
            throw new IllegalArgumentException("Denominator must be positive: " + numerator + "/" + denominator);
        }
        if (denominator != 1L && Math.floorMod(numerator, denominator) == 0) {
            throw new IllegalArgumentException("Can be reduced: " + numerator + "/" + denominator);
        }
    }

    public static Rational of(long integer) {
        return new Rational(integer, 1L);
    }

    public static Rational of(long numerator, long denominator) {
        var gcd = gcd(numerator, denominator);

        return new Rational(Math.floorDiv(numerator, gcd), Math.floorDiv(denominator, gcd));
    }

    public Rational negate() {
        return new Rational(-numerator, denominator);
    }

    public Rational reciprocal() {
        return new Rational(denominator, numerator);
    }

    public static Rational plus(Rational a, Rational b) {
        return of(a.numerator * b.denominator + b.numerator * a.denominator, a.denominator * b.denominator);
    }

    public static Rational minus(Rational a, Rational b) {
        return plus(a, b.negate());
    }

    public static Rational times(Rational a, Rational b) {
        return new Rational(a.numerator * b.numerator, a.denominator * b.denominator);
    }

    public static Rational divide(Rational a, Rational b) {
        return times(a, b.reciprocal());
    }

    private static long gcd(long a, long b) {
        while (b != 0L) {
            var t = b;
            b = Math.floorMod(a, b);
            a = t;
        }
        return a;
    }
}
