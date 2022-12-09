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

    public static enum Choice {
        Rock(1),
        Paper(2),
        Sciccor(3);

        private final int score;

        Choice(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public static Choice getChoiceFromLetter(String letter) {
            switch (letter.toUpperCase()) {
                case "A":
                case "X":
                    return Rock;
                case "B":
                case "Y":
                    return Paper;
                case "C":
                case "Z":
                    return Sciccor;
            }
            return null;
        }

        public MatchScore getMatchScore(Choice other) {
            if (this == other) {
                return MatchScore.Draw;
            } else if (this.getScore() - other.getScore() == -1) {
                return MatchScore.Loss;
            } else if (this == Sciccor && other == Rock) {
                return MatchScore.Loss;
            }
            return MatchScore.Win;
        }
    }

    public static enum MatchScore {
        Win(6),
        Draw(3),
        Loss(0);

        private final int matchPoints;

        MatchScore(int matchPoints) {
            this.matchPoints = matchPoints;
        }

        public int getMatchPoints() {
            return matchPoints;
        }
    }
}
