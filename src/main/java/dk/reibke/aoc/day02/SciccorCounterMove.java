package dk.reibke.aoc.day02;

public class SciccorCounterMove implements CounterMove {

    @Override
    public Choice draw() {
        return Choice.Sciccor;
    }

    @Override
    public Choice win() {
        return Choice.Rock;
    }

    @Override
    public Choice lose() {
        return Choice.Paper;
    }
}
