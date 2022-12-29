package dk.reibke.aoc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Stream;

class StreamUtilityTest {

    @Test
    public void testSetCollector() {
        Set<Integer> a = Set.of(1,2,3);
        Set<Integer> b = Set.of(2,3,4);

        Set<Integer> collect = Stream.of(a, b).collect(CollectionUtility.SetCollector());

        Assertions.assertEquals(4, collect.size());
    }
}