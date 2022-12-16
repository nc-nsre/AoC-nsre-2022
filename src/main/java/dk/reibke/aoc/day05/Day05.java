package dk.reibke.aoc.day05;

import dk.reibke.aoc.FileReader;
import dk.reibke.aoc.Tuple;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day05 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day05/part01");
        Iterator<String> iterator = lines.iterator();

        Tuple<List<String>> inputs = getInputs(iterator);

        GiantCargoCrane9000 crane = GiantCargoCrane9000.fromLines(inputs.A());
        List<CraneCrateMove> craneCrateMoves = CraneCrateMove.fromLines(inputs.B());

        craneCrateMoves.forEach(crane::moveCrates);
        System.out.println(crane.getTopCratesAsString());

        GiantCargoCrane9001 upgradedCrane = new GiantCargoCrane9001(GiantCargoCrane9000.fromLines(inputs.A()));

        craneCrateMoves.forEach(upgradedCrane::moveCrates);
        System.out.println(upgradedCrane.getTopCratesAsString());

    }

    public static Tuple<List<String>> getInputs(Iterator<String> lines) {

        List<String> giantCraneConstructionList = new ArrayList<>();
        List<String> operations = new ArrayList<>();
        while(lines.hasNext()) {
            String line = lines.next();
            if (line.isBlank()) {
                break;
            }
            giantCraneConstructionList.add(line);
        }
        while (lines.hasNext()) {
            operations.add(lines.next());
        }

        return new Tuple<>(giantCraneConstructionList, operations);
    }

    public static class CraneCrateMove {
        private final int to;
        private final int from;
        private final int amount;

        public static List<CraneCrateMove> fromLines(List<String> lines) {
            return lines.stream()
                    .map(CraneCrateMove::fromLine)
                    .toList();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CraneCrateMove)) return false;
            CraneCrateMove that = (CraneCrateMove) o;
            return to == that.to && from == that.from && amount == that.amount;
        }

        @Override
        public int hashCode() {
            return Objects.hash(to, from, amount);
        }

        public CraneCrateMove(int amount, int from, int to) {
            this.to = to;
            this.from = from;
            this.amount = amount;
        }

        public static CraneCrateMove fromLine(String line) {
            Pattern pattern = Pattern.compile("move (?<amount>[1-9][0-9]*) from (?<from>[1-9][0-9]*) to (?<to>[1-9][0-9]*)");
            Matcher matcher = pattern.matcher(line);

            matcher.find();

            int amount = Integer.parseInt(matcher.group("amount"));
            int from = Integer.parseInt(matcher.group("from"));
            int to = Integer.parseInt(matcher.group("to"));

            return new CraneCrateMove(amount, from, to);
        }

        public int getTo() {
            return to - 1;
        }

        public int getFrom() {
            return from - 1;
        }

        public int getAmount() {
            return amount;
        }
    }

    public static class GiantCargoCrane9001 extends GiantCargoCrane9000 {

        public GiantCargoCrane9001(GiantCargoCrane9000 oldCrane) {
            super(oldCrane.stacks);
        }

        @Override
        public void moveCrates(CraneCrateMove craneCrateMove) {
            LinkedList<Crate> grabbedCrates = pickCrates(craneCrateMove);

            while(!grabbedCrates.isEmpty()) {
                this.stacks.get(craneCrateMove.getTo()).putCrate(grabbedCrates.removeLast());
            }
        }

        private LinkedList<Crate> pickCrates(CraneCrateMove craneCrateMove) {
            LinkedList<Crate> grabbedCrates = new LinkedList<>();
            for (int i = 0; i < craneCrateMove.getAmount(); i++) {
                grabbedCrates.add(this.stacks.get(craneCrateMove.getFrom()).pickFromTop());
            }
            return grabbedCrates;
        }
    }

    public static class GiantCargoCrane9000 {
        public static final int LINE_STACK_BETWEEN_DISTANCE = 4;
        public static final int INITIAL_LETTER_OFFSET = 1;
        protected final List<Stack> stacks;

        public GiantCargoCrane9000(List<Stack> stacks) {
            this.stacks = stacks;
        }

        public List<Crate> getTopCrates() {
            return stacks.stream()
                    .map(Stack::getTopCrate)
                    .toList();
        }

        public String getTopCratesAsString() {
            return getTopCrates()
                    .stream()
                    .map(crate -> crate.getLetter().toString())
                    .collect(Collectors.joining(""));
        }

        public void moveCrates(CraneCrateMove craneCrateMove) {
            Stack fromStack = stacks.get(craneCrateMove.getFrom());
            Stack toStack = stacks.get(craneCrateMove.getTo());

            for (int i = 0; i < craneCrateMove.getAmount(); i++) {
                Crate topCrate = fromStack.pickFromTop();
                toStack.putCrate(topCrate);
            }
        }

        public static GiantCargoCrane9000 fromLines(List<String> lines) {
            LinkedList<String> list = new LinkedList<>(lines);
            String stackNumberLine = list.removeLast();
            int stacks = stackCount(stackNumberLine);

            List<List<Crate>> stackCrates = new ArrayList<>();
            for (int i = 0; i < stacks; i++) {
                stackCrates.add(new ArrayList<>());
            }

            while(!list.isEmpty()) {
                String line = list.removeLast();
                for (int i = 0; INITIAL_LETTER_OFFSET + i * LINE_STACK_BETWEEN_DISTANCE < line.length(); i++) {
                    char letter = line.charAt(1 + i * 4);
                    if (Character.isLetter(letter)) {
                        stackCrates.get(i).add(new Crate(letter));
                    }
                }
            }

            List<Stack> stackList = stackCrates.stream()
                    .map(Stack::new)
                    .toList();

            return new GiantCargoCrane9000(stackList);
        }

        public static int stackCount(String stackNumberLine) {
            return (int) Stream.of(stackNumberLine.split(" "))
                    .filter(element -> !element.isBlank())
                    .count();
        }

    }

    public static class Stack {
        private final LinkedList<Crate> crates;

        public Stack(List<Crate> crates) {
            this.crates = new LinkedList<>(crates);
        }

        public Crate getTopCrate() {
            return crates.isEmpty() ? null : crates.getLast();
        }

        public Crate pickFromTop() {
            return crates.removeLast();
        }

        public void putCrate(Crate crate) {
            crates.add(crate);
        }
    }

    public static class Crate {
        private final Character letter;

        public Crate(Character letter) {
            this.letter = letter;
        }

        public Character getLetter() {
            return letter;
        }
    }
}
