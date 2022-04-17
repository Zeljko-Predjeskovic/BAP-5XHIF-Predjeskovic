import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class NotSoQuickSort {
    public static final Random R = new Random();

    public static void main(String[] args) {
        final var size = 20000;
        final var longs = LongStream.generate(() -> R.nextLong(size)).limit(size).toArray();
        final var copy = Arrays.copyOf(longs, longs.length);
        System.out.println("Ready: " + preview(longs));
        System.out.println("Go!");
        final var mySort = time(() -> new QuickSort(List.wrap(longs)).sort());
        System.out.println("Done: " + preview(longs));
        System.out.println("Checking...");
        final var javaUtil = time(() -> Arrays.sort(copy));
        final var correct = Arrays.equals(longs, copy);
        System.out.println("Correct: " + correct + "\ntime: " + mySort + "\nshould be: " + javaUtil);
    }

    private static String preview(long[] longs) {
        if (longs.length <= 20) {
            return Arrays.toString(longs);
        } else {
            return Arrays.stream(longs)
                    .limit(10)
                    .mapToObj(Long::toString)
                    .collect(Collectors.joining(", ", "[", " ... "))
                    + Arrays.stream(longs)
                    .skip(longs.length - 10)
                    .mapToObj(Long::toString)
                    .collect(Collectors.joining(", ", "", "]"));

        }
    }

    private static Duration time(Runnable runnable) {
        final var start = System.nanoTime();
        runnable.run();
        final var end = System.nanoTime();
        return Duration.of(end - start, ChronoUnit.NANOS);
    }

    private record QuickSort(List list) {
        void sort() {
            sort(Bounds.of(list));
        }

        private void sort(Bounds bounds) {
            if (bounds.isEmpty()) return;

            final var pivot = bounds.randomWithin();

            while (true) {
                final var leftSide = bounds.leftToRightScanner().exclude(pivot);
                final var nextGreaterThan = leftSide.next(i -> item(i) > item(pivot));
                if (nextGreaterThan == null) {
                    swap(bounds.upper(), pivot);
                    sort(bounds.trimRight());
                    return;
                }

                final var rightSide = bounds.rightToLeftScanner().exclude(pivot);
                final var nextLessThan = rightSide.next(i -> item(i) < item(pivot));
                if (nextLessThan == null) {
                    swap(bounds.lower(), pivot);
                    sort(bounds.trimLeft());
                    return;
                }

                if (nextGreaterThan.isToTheLeftOf(nextLessThan)) {
                    swap(nextGreaterThan, nextLessThan);
                    continue;
                }

                final var middle = nextGreaterThan.isToTheLeftOf(pivot)
                        ? nextGreaterThan
                        : pivot.isToTheLeftOf(nextLessThan)
                        ? nextLessThan
                        : pivot;
                swap(middle, pivot);

                sort(bounds.upTo(middle));
                sort(bounds.downTo(middle));
                return;
            }
        }

        private long item(Index index) {
            return list.get(index.index());
        }

        private void swap(Index a, Index b) {
            final var t = item(a);
            list.set(a.index(), item(b));
            list.set(b.index(), t);
        }


        record Index(int index) {
            Pivot pivot(List list) {
                return new Pivot(this, list.get(index));
            }

            public Distance subtract(Index other) {
                return new Distance(index - other.index);
            }

            public Index add(Distance distance) {
                return new Index(index + distance.distance());
            }

            public boolean isToTheLeftOf(Index other) {
                return index < other.index;
            }
        }

        record Pivot(Index index, long value) {
        }

        record Bounds(Index lower, Index upper) {
            public static Bounds of(List list) {
                return new Bounds(new Index(0), new Index(list.size() - 1));
            }

            public Distance width() {
                return upper.subtract(lower).add(Distance.ONE).absolute();
            }

            public Index randomWithin() {
                return lower.add(new Distance(R.nextInt(width().distance)));
            }

            public Scanner leftToRightScanner() {
                return new AbstractScanner(lower) {
                    @Override
                    Index advance(Index index) {
                        return index.add(Distance.ONE);
                    }
                };
            }

            public Scanner rightToLeftScanner() {
                return new AbstractScanner(upper) {
                    @Override
                    Index advance(Index index) {
                        return index.add(Distance.ONE.inverse());
                    }
                };
            }

            public Bounds upTo(Index where) {
                return new Bounds(lower, where.add(Distance.ONE.inverse()));
            }

            public Bounds downTo(Index where) {
                return new Bounds(where.add(Distance.ONE), upper);
            }

            public boolean isEmpty() {
                return width().distance <= 0;
            }

            public Bounds trimLeft() {
                return downTo(lower);
            }

            public Bounds trimRight() {
                return upTo(upper);
            }

            private abstract class AbstractScanner implements Scanner {
                Index current;

                AbstractScanner(Index current) {
                    this.current = current;
                }

                @Override
                public Supplier<Index> scan() {
                    return () -> {
                        if (!contains(current)) {
                            return null;
                        }
                        try {
                            return current;
                        } finally {
                            current = advance(current);
                        }
                    };
                }

                abstract Index advance(Index index);
            }

            private boolean contains(Index index) {
                return lower.equals(index) || lower.isToTheLeftOf(index) && index.isToTheLeftOf(upper) || upper.equals(index);

            }
        }

        record Distance(int distance) {
            static final Distance ONE = new Distance(1);

            public Distance absolute() {
                return new Distance(Math.abs(distance));
            }

            public Distance inverse() {
                return new Distance(-distance);
            }

            public Distance add(Distance other) {
                return new Distance(distance + other.distance);
            }
        }

        @FunctionalInterface
        interface Scanner {
            Supplier<Index> scan();

            default Scanner exclude(Index excluded) {
                return () -> {
                    final var scanner = Scanner.this.scan();
                    return () -> {
                        while (true) {
                            final var next = scanner.get();
                            if (excluded.equals(next)) continue;
                            return next;
                        }

                    };
                };
            }

            default Index next(Predicate<Index> criterion) {
                final var scanner = scan();
                while (true) {
                    final var candidate = scanner.get();
                    if (candidate == null || criterion.test(candidate)) return candidate;
                }
            }
        }
    }

    private interface List {
        long get(int i);

        long set(int i, long item);

        int size();

        static List wrap(long[] array) {
            return new List() {
                @Override
                public long get(int i) {
                    return array[i];
                }

                @Override
                public long set(int i, long item) {
                    try {
                        return array[i];
                    } finally {
                        array[i] = item;
                    }
                }

                @Override
                public int size() {
                    return array.length;
                }
            };
        }

        default AbstractList<Long> asJavaUtilList() {
            return new AbstractList<>() {
                @Override
                public Long get(int index) {
                    return List.this.get(index);
                }

                @Override
                public Long set(int index, Long element) {
                    return List.this.set(index, element);
                }

                @Override
                public int size() {
                    return List.this.size();
                }
            };
        }
    }
}
