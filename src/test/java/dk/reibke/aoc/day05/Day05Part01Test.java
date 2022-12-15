package dk.reibke.aoc.day05;

import dk.reibke.aoc.FileReader;
import dk.reibke.aoc.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static dk.reibke.aoc.day05.Day05Part01.getInputs;
import static org.junit.jupiter.api.Assertions.*;

class Day05Part01Test {

    @Test
    public void testInitialStructure() {
        Day05Part01.GiantCargoCrane crane = generateData();

        Assertions.assertEquals(List.of('N', 'D', 'P'), crane.getTopCrates().stream().map(Day05Part01.Crate::getLetter).toList());
    }

    @Test
    public void testFirstMove() {
        Day05Part01.GiantCargoCrane crane = generateData();

        crane.moveCrates(new Day05Part01.CraneCrateMove(1,2,1));

        Assertions.assertEquals(List.of('D', 'C', 'P'), crane.getTopCrates().stream().map(Day05Part01.Crate::getLetter).toList());
    }

    @Test
    public void testSecondMove() {
        Day05Part01.GiantCargoCrane crane = generateData();

        crane.moveCrates(new Day05Part01.CraneCrateMove(1,2,1));
        crane.moveCrates(new Day05Part01.CraneCrateMove(3,1,3));

        Assertions.assertEquals(List.of("_", 'C', 'Z'), crane.getTopCrates().stream()
                .map(crate -> {
                    if (crate == null) {
                        return "_";
                    }
                    return crate.getLetter();
                }).toList());
    }

    @Test
    public void testMovementsFinalStructure() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day05/part01");
        Iterator<String> iterator = lines.iterator();

        Tuple<List<String>> inputs = getInputs(iterator);

        Day05Part01.GiantCargoCrane crane = Day05Part01.GiantCargoCrane.fromLines(inputs.A());
        List<Day05Part01.CraneCrateMove> craneCrateMoves = Day05Part01.CraneCrateMove.fromLines(inputs.B());

        craneCrateMoves.forEach(crane::moveCrates);

        Assertions.assertEquals("CMZ", crane.getTopCratesAsString());
    }

    @Test
    public void testInputSplit() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day05/part01");
        Tuple<List<String>> inputs = getInputs(lines.iterator());

        Assertions.assertEquals(4, inputs.A().size());
        Assertions.assertEquals(4, inputs.B().size());
    }

    @Test
    public void testCraneInput() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day05/part01");
        Tuple<List<String>> inputs = getInputs(lines.iterator());

        Day05Part01.GiantCargoCrane crane = Day05Part01.GiantCargoCrane.fromLines(inputs.A());
        Assertions.assertEquals(List.of('N', 'D', 'P'), crane.getTopCrates().stream().map(Day05Part01.Crate::getLetter).toList());
    }

    public static Stream<Arguments> getMovements() {
        return Stream.of(
                Arguments.arguments("move 1 from 2 to 1", 1,2,1),
                Arguments.arguments("move 3 from 1 to 3", 3,1,3),
                Arguments.arguments("move 2 from 2 to 1",2,2,1),
                Arguments.arguments("move 1 from 1 to 2",1,1,2)
        );
    }

    @ParameterizedTest
    @MethodSource("getMovements")
    public void testOperationsInput(String line, int amount, int from, int to) {
        Assertions.assertEquals(new Day05Part01.CraneCrateMove(amount,from,to), Day05Part01.CraneCrateMove.fromLine(line));
    }

    @Test
    public void testStackCountLine() {
        Assertions.assertEquals(3, Day05Part01.GiantCargoCrane.stackCount(" 1   2   3"));
    }

    private Day05Part01.GiantCargoCrane generateData() {
        Day05Part01.Stack stack1 = new Day05Part01.Stack(List.of(
                new Day05Part01.Crate('Z'),
                new Day05Part01.Crate('N')
        ));
        Day05Part01.Stack stack2 = new Day05Part01.Stack(List.of(
                new Day05Part01.Crate('M'),
                new Day05Part01.Crate('C'),
                new Day05Part01.Crate('D')
        ));
        Day05Part01.Stack stack3 = new Day05Part01.Stack(List.of(
                new Day05Part01.Crate('P')
        ));

        Day05Part01.GiantCargoCrane crane = new Day05Part01.GiantCargoCrane(new ArrayList<>(List.of(
                stack1,
                stack2,
                stack3
        )));
        return crane;
    }
}