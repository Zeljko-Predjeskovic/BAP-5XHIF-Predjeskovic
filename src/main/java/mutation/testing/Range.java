package mutation.testing;

import java.util.Optional;

public record Range(long from, long to) {
    public Range {
        if (from > to) {
            throw new IllegalArgumentException("From " + from + " must not be greater than to " + to);
        }
    }

    public Range scale(long factor) {
        return new Range(from * factor, to * factor);
    }

    public Range translate(long distance) {
        return new Range(from + distance, to + distance);
    }

    public static Range cover(Range a, Range b) {
        return new Range(Math.min(a.from, b.from), Math.max(a.to, b.to));
    }

    public boolean contains(long point) {
        return from <= point && point <= to;
    }

    public static Optional<Range> intersect(Range a, Range b) {
        if (a.contains(b.from)) {
            return Optional.of(a.contains(b.to) ? b : new Range(b.from, a.to));
        }
        if (b.contains(a.from)) {
            return Optional.of(b.contains(a.to) ? a : new Range(a.from, b.to));
        }
        return Optional.empty();
    }
}
