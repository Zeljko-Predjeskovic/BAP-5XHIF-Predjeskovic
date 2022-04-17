import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Random;

public class Slow {
    static final long T = 2 * Duration.of(1, ChronoUnit.SECONDS).toNanos();

    public static void main(String[] args) {
        System.out.println("work");
        work();
        System.out.println("relax");
        relax();
        System.out.println("trash");
        trash();
    }

    private static void trash() {
        final var r = new Random();
        final var l = new LinkedList<String>();
        final var bs = new byte[64];
        final var end = end();
        while (end.keepGoing()) {
            for (var i = 0; i < 1_000_000; i++) {
                r.nextBytes(bs);
                l.add(new String(bs));
            }
            l.clear();
        }
    }

    private static Timer end() {
        return new Timer(System.nanoTime() + T);
    }

    private static void relax() {
        final var end = end();
        while (end.keepGoing()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore for demonstration purposes
            }
        }
    }

    private static void work() {
        final var r = new Random();
        var l = 1L;
        final var end = end();
        while (end.keepGoing()) {
            for (var i = 0; i < 1_000_000; i++) {
                l *= r.nextLong();
                l = l == 0L ? 1L : l;
            }
        }
    }

    record Timer(long end) {
        boolean keepGoing() {
            return System.nanoTime() < end;
        }
    }
}
