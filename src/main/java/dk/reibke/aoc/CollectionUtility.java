package dk.reibke.aoc;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

public class CollectionUtility {

    public static <T> Collector<T, List<List<T>>, List<List<T>>> partionCollector(int blockSize) {
        return Collector.of(ArrayList::new, ((list, value) -> {
            List<T> block = (list.isEmpty() ? null : list.get(list.size() - 1));
            if (block == null || block.size() == blockSize)
                list.add(block = new ArrayList<>(blockSize));
            block.add(value);
        }), ((lists, lists2) -> { throw new RuntimeException("Parallel not supported"); }));
    }

    public static <T> Collector<T, List<List<T>>, List<List<T>>> partionOnFilter(Predicate<BlockStep<T>> startNewBlockIndicator) {
        return Collector.of(ArrayList::new, (list, value) -> {
            List<T> block = (list.isEmpty() ? new ArrayList<>() : list.get(list.size() - 1));
            if (startNewBlockIndicator.test(new BlockStep<>(value, block))) {
                block = block.isEmpty() ? block : new ArrayList<>();
                list.add(block);
            }
            block.add(value);
        }, ((lists, lists2) -> { throw new RuntimeException("Parallel not supported"); }));
    }

    public static record BlockStep<T>(T step, List<T> block){ }

    public static <T> Collector<Set<T>, Set<T>, Set<T>> SetCollector() {
        return Collector.of(HashSet::new, Set::addAll, (Set<T> A1, Set<T> A2) -> {
            A1.addAll(A2);
            return A1;
        });
    }

    public static <T> Collector<List<T>, List<T>, List<T>> ListCollector() {
        return Collector.of(ArrayList::new, List::addAll, (List<T> A1, List<T> A2) -> {
            A1.addAll(A2);
            return A1;
        });
    }

    public static class NextIterator<T> {
        private T current;
        private final Function<T, T> nextFunction;
        private final Predicate<T> hasNextFunction;

        public NextIterator(T initial, Function<T, T> nextFunction, Predicate<T> hasNextFunction) {
            this.current = initial;
            this.nextFunction = nextFunction;
            this.hasNextFunction = hasNextFunction;
        }

        public T next() {
            current = nextFunction.apply(current);
            return current;
        }

        public T current() {
            return current;
        }

        public boolean hasNext() {
            return hasNextFunction.test(current);
        }
    }

    public static Function<Integer, Integer> countUp() {
        return i -> i + 1;
    }

    public static Function<Integer, Integer> countDown() {
        return i -> i - 1;
    }

    public static Predicate<Integer> isNotEqual(int value) {
        return i -> i != value;
    }
}
