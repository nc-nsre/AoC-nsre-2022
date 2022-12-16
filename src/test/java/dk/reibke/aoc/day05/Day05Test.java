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
import java.util.stream.Stream;

import static dk.reibke.aoc.day05.Day05.getInputs;

class Day05Test {

    @Test
    public void testInitialStructure() {
        Day05.GiantCargoCrane9000 crane = generateData();

        Assertions.assertEquals(List.of('N', 'D', 'P'), crane.getTopCrates().stream().map(Day05.Crate::getLetter).toList());
    }

    @Test
    public void testFirstMove() {
        Day05.GiantCargoCrane9000 crane = generateData();

        crane.moveCrates(new Day05.CraneCrateMove(1,2,1));

        Assertions.assertEquals(List.of('D', 'C', 'P'), crane.getTopCrates().stream().map(Day05.Crate::getLetter).toList());
    }

    @Test
    public void testSecondMove() {
        Day05.GiantCargoCrane9000 crane = generateData();

        crane.moveCrates(new Day05.CraneCrateMove(1,2,1));
        crane.moveCrates(new Day05.CraneCrateMove(3,1,3));

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

        Day05.GiantCargoCrane9000 crane = Day05.GiantCargoCrane9000.fromLines(inputs.A());
        List<Day05.CraneCrateMove> craneCrateMoves = Day05.CraneCrateMove.fromLines(inputs.B());

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

        Day05.GiantCargoCrane9000 crane = Day05.GiantCargoCrane9000.fromLines(inputs.A());
        Assertions.assertEquals(List.of('N', 'D', 'P'), crane.getTopCrates().stream().map(Day05.Crate::getLetter).toList());
    }

    @Test
    public void testCrane9001FinalStructure() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day05/part01");
        Tuple<List<String>> inputs = getInputs(lines.iterator());

        Day05.GiantCargoCrane9000 crane = Day05.GiantCargoCrane9000.fromLines(inputs.A());
        Day05.GiantCargoCrane9001 upgradedCrane = new Day05.GiantCargoCrane9001(crane);

        Day05.CraneCrateMove.fromLines(inputs.B()).forEach(upgradedCrane::moveCrates);
        Assertions.assertEquals(List.of('M', 'C', 'D'), upgradedCrane.getTopCrates().stream().map(Day05.Crate::getLetter).toList());
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
        Assertions.assertEquals(new Day05.CraneCrateMove(amount,from,to), Day05.CraneCrateMove.fromLine(line));
    }

    @Test
    public void testStackCountLine() {
        Assertions.assertEquals(3, Day05.GiantCargoCrane9000.stackCount(" 1   2   3"));
    }

    private Day05.GiantCargoCrane9000 generateData() {
        Day05.Stack stack1 = new Day05.Stack(List.of(
                new Day05.Crate('Z'),
                new Day05.Crate('N')
        ));
        Day05.Stack stack2 = new Day05.Stack(List.of(
                new Day05.Crate('M'),
                new Day05.Crate('C'),
                new Day05.Crate('D')
        ));
        Day05.Stack stack3 = new Day05.Stack(List.of(
                new Day05.Crate('P')
        ));

        Day05.GiantCargoCrane9000 crane = new Day05.GiantCargoCrane9000(new ArrayList<>(List.of(
                stack1,
                stack2,
                stack3
        )));
        return crane;
    }
}