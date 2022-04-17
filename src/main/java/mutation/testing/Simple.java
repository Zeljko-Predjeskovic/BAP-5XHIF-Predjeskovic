package mutation.testing;

public record Simple() {
    public long test(long value) {
        if (value < 10) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
