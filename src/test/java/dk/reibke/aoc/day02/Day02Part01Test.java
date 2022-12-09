package dk.reibke.aoc.day02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Day02Part01Test {

    @Test
    public void testTotalScore() {
        Stream<String> lines = Stream.of("A Y",
                "B X",
                "C Z");

        Day02Part01 day02Part01 = new Day02Part01();
        List<Day02Part01.Round> allRounds = day02Part01.createAllRounds(lines);
        int totalScore = day02Part01.getTotalScore(allRounds);

        Assertions.assertEquals(15 ,totalScore);
    }
}