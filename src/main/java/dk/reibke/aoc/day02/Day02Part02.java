package dk.reibke.aoc.day02;

import dk.reibke.aoc.FileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day02Part02 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day02/part01");

        Day02Part02 day02Part01 = new Day02Part02();
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
        MatchScore matchScore = MatchScore.getMatchScoreFromLetter(splitLine[1]);

        return new Round(opponentChoice, matchScore);
    }

    public int getTotalScore(List<Round> rounds) {
        Integer totalScore = rounds.stream()
                .map(Round::getScore)
                .reduce(0, Integer::sum);

        return totalScore;
    }

    public static class Round {

        private final Choice opponentChoice;
        private final MatchScore matchScore;

        public Round(Choice opponentChoice, MatchScore matchScore) {
            this.opponentChoice = opponentChoice;
            this.matchScore = matchScore;
        }

        public int getScore() {
            Choice choice = opponentChoice.getChoiceToMatchResult(this.matchScore);

            int choiceScore = choice.getScore();
            int matchScore = this.matchScore.getMatchPoints();

            return choiceScore + matchScore;
        }

    }

}
