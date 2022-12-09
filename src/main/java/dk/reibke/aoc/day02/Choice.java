package dk.reibke.aoc.day02;

public enum Choice {
    Rock(1, new RockCounterMove()),
    Paper(2, new PaperCounterMove()),
    Sciccor(3, new SciccorCounterMove());

    private final int score;
    private final CounterMove counterMove;

    Choice(int score, CounterMove counterMove) {
        this.score = score;
        this.counterMove = counterMove;
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

    public Choice getChoiceToMatchResult(MatchScore matchScore) {
        switch (matchScore) {

            case Win -> {
                return counterMove.win();
            }
            case Draw -> {
                return counterMove.draw();
            }
            case Loss -> {
                return counterMove.lose();
            }
        }
        throw new RuntimeException("MatchScore not found: {" + matchScore.toString() + "}");
    }
}
