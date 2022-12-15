package dk.reibke.aoc.day03;

import dk.reibke.aoc.FileReader;
import dk.reibke.aoc.StreamUtility;
import dk.reibke.aoc.Tuple;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Day03 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        partOne();
        partTwo();
    }

    private static void partTwo() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day03/part01");
        List<Group> groups = Group.createGroups(lines, 3);
        Integer groupBadgeSum = groups.stream()
                .map(Group::findGroupBadge)
                .map(Item::getPriority)
                .reduce(Integer::sum)
                .orElse(0);

        System.out.println(groupBadgeSum);
    }

    private static void partOne() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day03/part01");

        Day03 day03 = new Day03();
        int sumOfOddRucksackPriorities = day03.getSumOfOddRucksackPriorities(lines);

        System.out.println(sumOfOddRucksackPriorities);
    }

    public int getSumOfOddRucksackPriorities(Stream<String> lines) {
        Integer sumOfOddRucksackPriorities = lines.map(Day03::splitLineToCharacterLists)
                .map(RuckSack::createFromCharacterListsTuples)
                .map(RuckSack::getOddPriorityItem)
                .map(Item::getPriority)
                .reduce(0, Integer::sum);

        return sumOfOddRucksackPriorities;
    }

    public static Tuple<List<Character>> splitLineToCharacterLists(String line) {
        List<Character> characters = line.chars()
                .mapToObj(c -> (char) c)
                .toList();
        int split = characters.size() / 2;

        List<Character> listA = characters.subList(0, split);
        List<Character> listB = characters.subList(split, characters.size());

        return new Tuple<>(listA, listB);
    }

    public static Item findCommonItem(List<Item> A, List<Item> B) {
        List<Item> copy = new ArrayList<>(A);
        copy.retainAll(B);

        return copy.stream()
                .findFirst()
                .orElse(null);
    }

    public static class Group {
        private final List<RuckSack> ruckSacks;

        public Group(List<RuckSack> ruckSacks) {
            this.ruckSacks = ruckSacks;
        }

        public Item findGroupBadge() {
            return ruckSacks.stream().findFirst().map(ruckSack -> {
                List<Item> copy = new ArrayList<>(ruckSack.getAllItems());
                ruckSacks.stream()
                        .skip(1)
                        .map(RuckSack::getAllItems)
                        .forEach(copy::retainAll);

                return copy.stream().findFirst().orElse(null);
            }).orElse(null);
        }

        public static List<Group> createGroups(Stream<String> lines, int groupSizes) {
            return lines.map(Day03::splitLineToCharacterLists)
                    .map(RuckSack::createFromCharacterListsTuples)
                    .collect(StreamUtility.partionCollector(groupSizes))
                    .stream()
                    .map(Group::new)
                    .toList();
        }

        @Override
        public String toString() {
            return "Group{" +
                    "GroupId=" + findGroupBadge() +
                    ", ruckSacks=" + ruckSacks +
                    '}';
        }
    }

    public static class RuckSack {
        private final Compartment compartment1;
        private final Compartment compartment2;

        public RuckSack(Compartment compartment1, Compartment compartment2) {
            this.compartment1 = compartment1;
            this.compartment2 = compartment2;
        }

        public Item getOddPriorityItem() {
            return compartment1.commonItem(compartment2);
        }

        public static RuckSack createFromCharacterListsTuples(Tuple<List<Character>> characterListTuples) {
            Compartment compartment1 = new Compartment(characterListTuples.A());
            Compartment compartment2 = new Compartment(characterListTuples.B());

            return new RuckSack(compartment1, compartment2);
        }

        public List<Item> getAllItems() {
            List<Item> items = new ArrayList<>(compartment1.items);
            items.addAll(compartment2.items);

            return items;
        }

        @Override
        public String toString() {
            return "RuckSack{" +
                    "compartment1=" + compartment1 +
                    ", compartment2=" + compartment2 +
                    '}';
        }
    }

    public static class Compartment {
        private final List<Item> items;

        public Compartment(List<Character> characters) {
            this.items = characters.stream().map(Item::new).toList();
        }

        public Item commonItem(Compartment other) {
            return findCommonItem(this.items, other.items);
        }

        public List<Item> getItems() {
            return this.items;
        }

        @Override
        public String toString() {
            return "Compartment{" +
                    "items=" + items +
                    '}';
        }
    }

    public static class Item {

        private static final int A_LOWER_CHAR_VALUE = 'a';
        private static final int A_UPPER_CHAR_VALUE = 'A';
        private static final int LOWER_CASE_START_VALUE = 1;
        private static final int UPPER_CASE_START_VALUE = 27;

        private final Character character;

        public Item(Character character) {
            this.character = character;
        }

        public int getPriority() {
            if (Character.isLowerCase(character)) {
                return character - A_LOWER_CHAR_VALUE + LOWER_CASE_START_VALUE;
            }
            return character - A_UPPER_CHAR_VALUE + UPPER_CASE_START_VALUE;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "character=" + character +
                    '}';
        }

        public Character getCharacter() {
            return character;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;
            Item item = (Item) o;
            return character.equals(item.character);
        }

        @Override
        public int hashCode() {
            return Objects.hash(character);
        }
    }
}
