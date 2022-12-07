package dk.reibke.aoc.day01;


import dk.reibke.aoc.FileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Day01Part01 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day01/part01");
        long highestCalories = new Day01Part01().findHighestCalorie(lines);

        System.out.println(highestCalories);
    }

    public long findHighestCalorie(Stream<String> lines) {

        AtomicLong calories = new AtomicLong();
        AtomicLong maxCalories = new AtomicLong(calories.get());

        lines.forEachOrdered(line -> {
            if (line.isBlank()) {
                tryUpdateMaxCalorieAndResetCalories(calories, maxCalories);
            } else {
                addLineToCalories(calories, line);
            }
        });

        tryUpdateMaxCalorieAndResetCalories(calories, maxCalories);

        return maxCalories.get();
    }

    private void addLineToCalories(AtomicLong calories, String line) {
        try {
            long newIntake = Long.parseLong(line);
            calories.addAndGet(newIntake);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Unable to parse calorie line: \"" + line + "\"", nfe);
        }
    }

    private void tryUpdateMaxCalorieAndResetCalories(AtomicLong calories, AtomicLong maxCalories) {
        if (maxCalories.get() < calories.get()) {
            maxCalories.set(calories.get());
        }
        calories.set(0);
    }
}
