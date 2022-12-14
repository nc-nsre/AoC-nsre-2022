package dk.reibke.aoc.day02;

import dk.reibke.aoc.FileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day02Part01 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day02/part01");

        Day02Part01 day02Part01 = new Day02Part01();
        List<Round> allRounds = day02Part01.createAllRounds(lines);
        int totalScore = day02Part01.getTotalScore(allRounds);

        System.out.println(totalScore);
    }

    public List<Round> createAllRounds(Stream<String> lines) {
        List<Round> rounds = lines.map(this::createRound)
                .collect(Collectors.toList());

        return rounds;
    }

    public Round createRound(String line) {
        // Lines assumed to be 1 Character, followed by 1 space, followed by 1 Character
        // Example: "A X", "B Z"
        String[] splitLine = line.split(" ");

        Choice opponentChoice = Choice.getChoiceFromLetter(splitLine[0]);
        Choice myChoice = Choice.getChoiceFromLetter(splitLine[1]);

        return new Round(opponentChoice, myChoice);
    }

    public int getTotalScore(List<Round> rounds) {
        Integer totalScore = rounds.stream()
                .map(Round::getScore)
                .reduce(0, Integer::sum);

        return totalScore;
    }

    public static class Round {

        private final Choice opponentChoice;
        private final Choice myChoice;

        public Round(Choice opponentChoice, Choice myChoice) {
            this.opponentChoice = opponentChoice;
            this.myChoice = myChoice;
        }

        public int getScore() {
            int choiceScore = this.myChoice.getScore();
            int matchPoint = myChoice.getMatchScore(opponentChoice).getMatchPoints();

            return choiceScore + matchPoint;
        }
    }
}
