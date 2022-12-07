package dk.reibke.aoc.day01;


import dk.reibke.aoc.FileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day01Part02 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day01/part01");
        long highestCalories = new Day01Part02().findTopNHighestCalorie(lines, 3);

        System.out.println(highestCalories);
    }

    public long findTopNHighestCalorie(Stream<String> lines, int n) {

        PriorityQueue<Long> priorityQueue = new PriorityQueue<>(reverseComparator());

        AtomicLong calories = new AtomicLong();

        lines.forEachOrdered(line -> {
            if (line.isBlank()) {
                updateQueueAndResetCalories(priorityQueue, calories);
            } else {
                addLineToCalories(calories, line);
            }
        });

        updateQueueAndResetCalories(priorityQueue, calories);

        long sum = LongStream.generate(priorityQueue::poll)
                .limit(3)
                .summaryStatistics()
                .getSum();

        return sum;
    }

    private Comparator<Long> reverseComparator() {
        return (o1, o2) -> o1.compareTo(o2) * -1;
    }

    private void updateQueueAndResetCalories(PriorityQueue<Long> priorityQueue, AtomicLong calories) {
        priorityQueue.add(calories.get());
        calories.set(0);
    }

    private void addLineToCalories(AtomicLong calories, String line) {
        try {
            long newIntake = Long.parseLong(line);
            calories.addAndGet(newIntake);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Unable to parse calorie line: \"" + line + "\"", nfe);
        }
    }

}
