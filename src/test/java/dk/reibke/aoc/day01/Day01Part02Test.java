package dk.reibke.aoc.day01;

import dk.reibke.aoc.FileReader;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

class Day01Part02Test {

    @org.junit.jupiter.api.Test
    void testFindHighestCalorie() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day01/part01_test01");
        long highestCalories = new Day01Part02().findTopNHighestCalorie(lines, 3);

        Assertions.assertEquals(45000, highestCalories);
    }
}