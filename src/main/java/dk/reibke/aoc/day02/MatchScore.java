package dk.reibke.aoc.day02;

public enum MatchScore {
    Win(6),
    Draw(3),
    Loss(0);

    private final int matchPoints;

    MatchScore(int matchPoints) {
        this.matchPoints = matchPoints;
    }

    public static MatchScore getMatchScoreFromLetter(String letter) {
        switch (letter.toUpperCase()) {
            case "X": return Loss;
            case "Y": return Draw;
            case "Z": return Win;
        }
        return null;
    }

    public int getMatchPoints() {
        return matchPoints;
    }
}
