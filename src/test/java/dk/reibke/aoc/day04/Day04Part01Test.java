package dk.reibke.aoc.day04;

import dk.reibke.aoc.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class Day04Part01Test {


    @Test
    public void testAFullOverlapB() {
        Day04Part01.Section A = new Day04Part01.Section(2,8);
        Day04Part01.Section B = new Day04Part01.Section(3,7);

        Day04Part01.Pair pair = new Day04Part01.Pair(new Tuple<>(A, B));

        Assertions.assertTrue(pair.fullOverlap());
    }

    @Test
    public void testBFullOverlapA() {
        Day04Part01.Section A = new Day04Part01.Section(3,4);
        Day04Part01.Section B = new Day04Part01.Section(2,5);

        Day04Part01.Pair pair = new Day04Part01.Pair(new Tuple<>(A, B));

        Assertions.assertTrue(pair.fullOverlap());
    }

    @Test
    public void testADontFullOverlapB() {
        Day04Part01.Section A = new Day04Part01.Section(3,8);
        Day04Part01.Section B = new Day04Part01.Section(6,9);

        Day04Part01.Pair pair = new Day04Part01.Pair(new Tuple<>(A, B));

        Assertions.assertFalse(pair.fullOverlap());
    }

    @Test
    public void testFullOverlapCount() {
        Stream<String> lines = Stream.of(
                "2-4,6-8", 
                        "2-3,4-5", 
                        "5-7,7-9", 
                        "2-8,3-7", 
                        "6-6,4-6", 
                        "2-6,4-8"
        );

        Day04Part01.ClearingPlan clearingPlan = Day04Part01.ClearingPlan.fromLines(lines);
        long count = clearingPlan.countFullOverlaps();

        Assertions.assertEquals(2, count);
    }

    @Test
    public void testFromLineFullOverlap() {
        Day04Part01.Pair pair = Day04Part01.Pair.fromLine("2-8,3-7");

        Assertions.assertTrue(pair.fullOverlap());
    }

    @Test
    public void testPartialOverlap() {
        Assertions.assertTrue(Day04Part01.Pair.fromLine("5-7,7-9").partialOverlap());
    }

    @Test
    public void testPartialOverlapCount() {
        Stream<String> lines = Stream.of(
                "2-4,6-8",
                "2-3,4-5",
                "5-7,7-9",
                "2-8,3-7",
                "6-6,4-6",
                "2-6,4-8"
        );

        Day04Part01.ClearingPlan clearingPlan = Day04Part01.ClearingPlan.fromLines(lines);
        long count = clearingPlan.countPartialOverlaps();

        Assertions.assertEquals(4, count);
    }
    
    public static Stream<Arguments> linesAndPartialOverlap() {
        return Stream.of(
                arguments("2-4,6-8",false),
                arguments("2-3,4-5",false),
                arguments("5-7,7-9",true),
                arguments("2-8,3-7",true),
                arguments("6-6,4-6",true),
                arguments("2-6,4-8",true)
        );
    }
    
    @ParameterizedTest
    @MethodSource("linesAndPartialOverlap")
    public void testLinesAndPartialOverlap(String line, boolean expected) {
        Assertions.assertEquals(expected, Day04Part01.Pair.fromLine(line).partialOverlap());
    }

    public static Stream<Arguments> isInSectionNumbers() {
        return Stream.of(
                arguments("2-8", 3, true),
                arguments("2-8", 7, true)
        );
    }

    @ParameterizedTest
    @MethodSource("isInSectionNumbers")
    public void testIsInSection(String range, int number, boolean expected) {
        Assertions.assertEquals(expected, Day04Part01.Section.fromString(range).isInSection(number));
    }
}
