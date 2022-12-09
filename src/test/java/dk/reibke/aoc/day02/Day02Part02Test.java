package dk.reibke.aoc.day02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

class Day02Part02Test {

    @Test
    void testTotalScore() {
        Stream<String> lines = Stream.of("A Y",
                "B X",
                "C Z");

        Day02Part02 day02Part02 = new Day02Part02();
        List<Day02Part02.Round> allRounds = day02Part02.createAllRounds(lines);
        int totalScore = day02Part02.getTotalScore(allRounds);

        Assertions.assertEquals(12, totalScore);
    }

    @Test
    void testRockDraw() {
        Day02Part02.Round round = new Day02Part02.Round(Choice.Rock, MatchScore.Draw);
        int score = round.getScore();

        Assertions.assertEquals(4, score);
    }

    @Test
    void testPaperLoss() {
        Day02Part02.Round round = new Day02Part02.Round(Choice.Paper, MatchScore.Loss);
        int score = round.getScore();

        Assertions.assertEquals(1, score);
    }

    @Test
    void testSciccorWin() {
        Day02Part02.Round round = new Day02Part02.Round(Choice.Sciccor, MatchScore.Win);
        int score = round.getScore();

        Assertions.assertEquals(7, score);
    }
}