package dk.reibke.aoc.day02;

public class RockCounterMove implements CounterMove {

    @Override
    public Choice draw() {
        return Choice.Rock;
    }

    @Override
    public Choice win() {
        return Choice.Paper;
    }

    @Override
    public Choice lose() {
        return Choice.Sciccor;
    }
}
