package dk.reibke.aoc.day02;

public class PaperCounterMove implements CounterMove {

    @Override
    public Choice draw() {
        return Choice.Paper;
    }

    @Override
    public Choice win() {
        return Choice.Sciccor;
    }

    @Override
    public Choice lose() {
        return Choice.Rock;
    }
}
