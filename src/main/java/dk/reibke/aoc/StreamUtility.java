package dk.reibke.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public class StreamUtility {

    public static <T> Collector<T, List<List<T>>, List<List<T>>> partionCollector(int blockSize) {
        return Collector.of(ArrayList::new, ((list, value) -> {
            List<T> block = (list.isEmpty() ? null : list.get(list.size() - 1));
            if (block == null || block.size() == blockSize)
                list.add(block = new ArrayList<>(blockSize));
            block.add(value);
        }), ((lists, lists2) -> { throw new RuntimeException("Parallel not supported"); }));
    }
}
