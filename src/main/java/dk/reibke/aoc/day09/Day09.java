package dk.reibke.aoc.day09;

import dk.reibke.aoc.FileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day09 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        List<String> lines = new FileReader().streamFile("day09/part01").toList();

        LineInterpreter lineInterpreter = new LineInterpreter();

        printUniquelyVisitedTailCells(lines, lineInterpreter, 2);
        printUniquelyVisitedTailCells(lines, lineInterpreter, 10);

    }

    private static void printUniquelyVisitedTailCells(List<String> lines, LineInterpreter lineInterpreter, int knots) {
        System.out.println("== Updating Rope and counting uniquely visited tail cells with [" + knots + "] knot rope ==");
        int uniqueVisitedCells = lineInterpreter.runLines(lines, Rope.initialRope(knots)).stream()
                .map(Rope::tail)
                .collect(Collectors.toSet())
                .size();

        System.out.println("Uniquely visited tail cells: " + uniqueVisitedCells);
    }

    private record Command(Movement movement, int times) {

        public static Command fromLine(String line) {
            String[] parts = line.split(" ");

            Movement movement = Movement.fromKey(parts[0]);
            int times = Integer.parseInt(parts[1]);

            return new Command(movement, times);
        }
    }

    public static class LineInterpreter {

        public List<Rope> runLines(List<String> lines, Rope rope) {
            LinkedList<Rope> history = new LinkedList<>();
            history.add(rope);
            lines.stream().map(Command::fromLine)
                    .forEach(command -> {
                        for (int i = 0; i < command.times; i++) {
                            history.add(history.getLast().update(command.movement));
                        }
                    });

            return history;
        }


    }

    public record Rope(List<Knot> knots) {

        public static Rope initialRope(int knotAmount) {
            List<Knot> knots = IntStream.range(0, knotAmount).mapToObj(i -> new Knot(0, 0)).toList();
            return new Rope(knots);
        }

        public Rope update(Movement move) {
            LinkedList<Knot> originalKnots = new LinkedList<>(knots);
            LinkedList<Knot> newKnots = new LinkedList<>();

            Knot head = originalKnots.pollFirst().move(move);
            newKnots.add(head);

            while(!originalKnots.isEmpty()) {
                Knot previousKnot = newKnots.peekLast();
                Knot nextKnot = originalKnots.pollFirst().moveTowards(previousKnot);
                newKnots.add(nextKnot);
            }

            return new Rope(newKnots);
        }

        public Knot tail() {
            return knots.get(knots.size() - 1);
        }

        public Knot head() {
            return knots.get(0);
        }
    }

    public enum Movement {
        UP("U"), DOWN("D"), LEFT("L"), RIGHT("R");

        private static Map<String, Movement> keyMap = Stream.of(Movement.values()).collect(Collectors.toMap(Movement::getKey, movement -> movement));

        private final String key;

        Movement(String key) {
            this.key = key;
        }

        public static Movement fromKey(String key) {
            if (keyMap.containsKey(key)) {
                return keyMap.get(key);
            }
            throw new RuntimeException(new InvalidKeyException("No key found for: " + key + ", expected either: [" +
                    String.join(", ", keyMap.keySet()) + "]"));
        }

        public String getKey() {
            return key;
        }
    }

    public record Knot(int x, int y) {

        int chessBoardDistance(Knot other) {
            return Math.max(Math.abs(other.x - x), Math.abs(other.y - y));
        }

        boolean adjacent(Knot other) {
            return chessBoardDistance(other) <= 1;
        }

        public Knot move(Movement move) {
            switch (move) {
                case UP: return new Knot(x, y + 1);
                case DOWN: return new Knot(x, y - 1);
                case LEFT: return new Knot(x - 1, y);
                case RIGHT: return new Knot(x + 1, y);
            }

            throw new RuntimeException("Impossible move: " + move);
        }

        public Knot moveTowards(Knot other) {
            if (adjacent(other)) {
                return this;
            }
            int x_delta = singletized(other.x - x);
            int y_delta = singletized(other.y - y);

            return new Knot(x + x_delta, y + y_delta);
        }

        private int singletized(int n) {
            if (n == 0) {
                return 0;
            } else if (n > 0) {
                return 1;
            }
            return -1;
        }
    }
}
