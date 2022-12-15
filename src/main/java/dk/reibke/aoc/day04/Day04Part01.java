package dk.reibke.aoc.day04;

import dk.reibke.aoc.FileReader;
import dk.reibke.aoc.Tuple;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

public class Day04Part01 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        parts();
    }

    private static void parts() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day04/part01");

        ClearingPlan clearingPlan = ClearingPlan.fromLines(lines);

        System.out.println("Full overlaps: " + clearingPlan.countFullOverlaps());
        System.out.println("Partial overlaps: " + clearingPlan.countPartialOverlaps());
    }

    public static class ClearingPlan {
        private final List<Pair> clearingPairs;

        public ClearingPlan(List<Pair> clearingPairs) {
            this.clearingPairs = clearingPairs;
        }

        public static ClearingPlan fromLines(Stream<String> lines) {
            return new ClearingPlan(lines.map(Pair::fromLine).toList());
        }

        public long countFullOverlaps() {
            return clearingPairs.stream()
                    .filter(Pair::fullOverlap)
                    .count();
        }

        public long countPartialOverlaps() {
            return clearingPairs.stream()
                    .filter(Pair::partialOverlap)
                    .count();
        }
    }

    public static class Pair {
        private final Tuple<Section> sections;

        public Pair(Tuple<Section> sections) {
            this.sections = sections;
        }

        public boolean fullOverlap() {
            return sections.A().contains(sections.B()) || sections.B().contains(sections.A());
        }

        public static Pair fromLine(String line) {
            String[] split = line.split(",");
            return new Pair(new Tuple<>(Section.fromString(split[0]), Section.fromString(split[1])));
        }

        public boolean partialOverlap() {
            return sections.B().partialOverlap(sections.A()) || sections.A().partialOverlap(sections.B());
        }
    }

    public static class Section {
        private final int from, to;

        public Section(int from, int to) {
            this.from = from;
            this.to = to;
        }

        private boolean contains(Section other) {
            return from <= other.from && to >= other.to;
        }

        public static Section fromString(String range) {
            String[] split = range.split("-");
            return new Section(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }

        public boolean partialOverlap(Section other) {
            return isInSection(other.from) || isInSection(other.to);
        }

        public boolean isInSection(int number) {
            return from <= number && to >= number;
        }
    }
}
