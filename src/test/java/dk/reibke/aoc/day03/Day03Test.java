package dk.reibke.aoc.day03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class Day03Test {

    @Test
    public void TestSummedOddRucksackPriority() {
        Stream<String> lines = Stream.of("vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg",
                "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                "ttgJtRGJQctTZtZT",
                "CrZsJsPPZsGzwwsLwLmpwMDw");

        Day03 day03 = new Day03();
        int sumOfOddRucksackPriorities = day03.getSumOfOddRucksackPriorities(lines);

        Assertions.assertEquals(157, sumOfOddRucksackPriorities);
    }

    static Stream<Arguments> lineAndExpectedPriority() {
        return Stream.of(
                arguments("vJrwpWtwJgWrhcsFMMfFFhFp", 16, 'p'),
                arguments("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", 38, 'L'),
                arguments("PmmdzqPrVvPwwTWBwg",42, 'P'),
                arguments("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", 22, 'v'),
                arguments("ttgJtRGJQctTZtZT", 20, 't'),
                arguments("CrZsJsPPZsGzwwsLwLmpwMDw", 19, 's')
        );
    }

    @ParameterizedTest
    @MethodSource("lineAndExpectedPriority")
    public void testIndividualRucksackOddPriority(String line, int expectedPriority, Character expectedCharacter) {
        Day03.Tuple<List<Character>> tuple = Day03.splitLineToCharacterLists(line);
        Day03.RuckSack ruckSack = Day03.RuckSack.createFromCharacterListsTuples(tuple);

        Day03.Item actualItem = ruckSack.getOddPriorityItem();

        Assertions.assertEquals(expectedCharacter, actualItem.getCharacter());
        Assertions.assertEquals(expectedPriority, actualItem.getPriority());
    }

    @Test
    public void testLowerCasePriority() {
        Character character = 'b';
        Day03.Item item = new Day03.Item(character);

        Assertions.assertEquals(2, item.getPriority());
    }

    @Test
    public void testUpperCasePriority() {
        Character character = 'B';
        Day03.Item item = new Day03.Item(character);

        Assertions.assertEquals(28, item.getPriority());
    }

    @Test
    public void testSplitLineToCharacterLists() {
        String line = "vJrwpWtwJgWrhcsFMMfFFhFp";
        Day03 day03 = new Day03();
        Day03.Tuple<List<Character>> lists = day03.splitLineToCharacterLists(line);

        Assertions.assertEquals("vJrwpWtwJgWr".chars().mapToObj(c -> (char) c).toList(), lists.A());
        Assertions.assertEquals("hcsFMMfFFhFp".chars().mapToObj(c -> (char) c).toList(), lists.B());
    }

    @Test
    public void testAllItems() {
        String line = "vJrwpWtwJgWrhcsFMMfFFhFp";
        Day03 day03 = new Day03();
        Day03.Tuple<List<Character>> tuple = day03.splitLineToCharacterLists(line);
        Day03.RuckSack ruckSack = new Day03.RuckSack(
                new Day03.Compartment(tuple.A()), new Day03.Compartment(tuple.B()));

        Assertions.assertEquals(line.chars().mapToObj(c -> (char) c).toList(),
                ruckSack.getAllItems().stream().map(Day03.Item::getCharacter).toList());
    }

    @Test
    public void testCommonItem() {
        String line = "vJrwpWtwJgWrhcsFMMfFFhFp";
        Day03 day03 = new Day03();
        Day03.Tuple<List<Character>> tuple = day03.splitLineToCharacterLists(line);
        Day03.Compartment A = new Day03.Compartment(tuple.A());
        Day03.Compartment B = new Day03.Compartment(tuple.B());

        Day03.Item commonItem = Day03.findCommonItem(A.getItems(), B.getItems());

        Assertions.assertEquals(new Day03.Item('p'), commonItem);
    }

    @Test
    public void testFindGroupItem1() {
        Stream<String> lines = Stream.of(
                "vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg");

        List<Day03.RuckSack> ruckSacks = lines.map(Day03::splitLineToCharacterLists)
                .map(Day03.RuckSack::createFromCharacterListsTuples)
                .toList();

        Day03.Group group = new Day03.Group(ruckSacks);

        Assertions.assertEquals(new Day03.Item('r'), group.findGroupBadge());
    }

    @Test
    public void testFindGroupItem2() {
        Stream<String> lines = Stream.of(
                "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                        "ttgJtRGJQctTZtZT",
                        "CrZsJsPPZsGzwwsLwLmpwMDw");

        List<Day03.RuckSack> ruckSacks = lines.map(Day03::splitLineToCharacterLists)
                .map(Day03.RuckSack::createFromCharacterListsTuples)
                .toList();

        Day03.Group group = new Day03.Group(ruckSacks);

        Assertions.assertEquals(new Day03.Item('Z'), group.findGroupBadge());
    }
}