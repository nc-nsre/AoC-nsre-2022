package dk.reibke.aoc.day09;

import dk.reibke.aoc.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

class Day09Test {


    @Test
    public void testHeadMoveUp() {
        Day09.Knot head = new Day09.Knot(0,0);

        Day09.Knot newHead = head.move(Day09.Movement.UP);

        Assertions.assertEquals(new Day09.Knot(0, 1), newHead);
    }

    @Test
    public void testHeadMoveDown() {
        Day09.Knot head = new Day09.Knot(0,0);

        Day09.Knot newHead = head.move(Day09.Movement.DOWN);

        Assertions.assertEquals(new Day09.Knot(0, -1), newHead);
    }

    @Test
    public void testHeadMoveLeft() {
        Day09.Knot head = new Day09.Knot(0,0);

        Day09.Knot newHead = head.move(Day09.Movement.LEFT);

        Assertions.assertEquals(new Day09.Knot(-1, 0), newHead);
    }

    @Test
    public void testHeadMoveRight() {
        Day09.Knot head = new Day09.Knot(0,0);

        Day09.Knot newHead = head.move(Day09.Movement.RIGHT);

        Assertions.assertEquals(new Day09.Knot(1, 0), newHead);
    }

    @Test
    public void testTailNoMoveTowards() {
        Day09.Knot head = new Day09.Knot(2,0);
        Day09.Knot tail = new Day09.Knot(0,0);

        Day09.Knot newTail = tail.moveTowards(head);

        Assertions.assertEquals(new Day09.Knot(1,0), newTail);
    }

    @Test
    public void testTailNoMoveTowardsDiagonal() {
        Day09.Knot head = new Day09.Knot(2,1);
        Day09.Knot tail = new Day09.Knot(0,0);

        Day09.Knot newTail = tail.moveTowards(head);

        Assertions.assertEquals(new Day09.Knot(1,1), newTail);
    }

    @Test
    public void testRopeUpdateDiagonal() {
        Day09.Knot head = new Day09.Knot(1,1);
        Day09.Knot tail = new Day09.Knot(0,0);

        Day09.Rope rope = new Day09.Rope(List.of(head, tail));

        Day09.Rope updatedRope = rope.update(Day09.Movement.RIGHT);

        Assertions.assertEquals(new Day09.Knot(2,1), updatedRope.head());
        Assertions.assertEquals(new Day09.Knot(1,1), updatedRope.tail());
    }

    @Test
    public void testRopeUpdateStretch() {
        Day09.Knot head = new Day09.Knot(0,1);
        Day09.Knot tail = new Day09.Knot(0,0);

        Day09.Rope rope = new Day09.Rope(List.of(head, tail));

        Day09.Rope updatedRope = rope.update(Day09.Movement.UP);

        Assertions.assertEquals(new Day09.Knot(0,2), updatedRope.head());
        Assertions.assertEquals(new Day09.Knot(0,1), updatedRope.tail());
    }

    @Test
    public void testRopeUpdateNoTailMovement() {
        Day09.Knot head = new Day09.Knot(1,1);
        Day09.Knot tail = new Day09.Knot(0,0);

        Day09.Rope rope = new Day09.Rope(List.of(head, tail));

        Day09.Rope updatedRope = rope.update(Day09.Movement.LEFT);

        Assertions.assertEquals(new Day09.Knot(0,1), updatedRope.head());
        Assertions.assertEquals(new Day09.Knot(0,0), updatedRope.tail());
    }

    @Test
    public void testVisitedTailCells() throws IOException, URISyntaxException {
        List<String> lines = new FileReader().streamFile("day09/part01").toList();

        Day09.LineInterpreter lineInterpreter = new Day09.LineInterpreter();
        int uniqueVisitedCells = lineInterpreter.runLines(lines, Day09.Rope.initialRope(2)).stream()
                .map(Day09.Rope::tail)
                .collect(Collectors.toSet())
                .size();

        Assertions.assertEquals(13, uniqueVisitedCells);
    }

    @Test
    public void testVisitedTailCells10KnotsCase1() throws IOException, URISyntaxException {
        List<String> lines = new FileReader().streamFile("day09/part01").toList();

        Day09.LineInterpreter lineInterpreter = new Day09.LineInterpreter();
        int uniqueVisitedCells = lineInterpreter.runLines(lines, Day09.Rope.initialRope(10)).stream()
                .map(Day09.Rope::tail)
                .collect(Collectors.toSet())
                .size();

        Assertions.assertEquals(1, uniqueVisitedCells);
    }

    @Test
    public void testVisitedTailCells10KnotsCase2() throws IOException, URISyntaxException {
        List<String> lines = new FileReader().streamFile("day09/part02").toList();

        Day09.LineInterpreter lineInterpreter = new Day09.LineInterpreter();
        int uniqueVisitedCells = lineInterpreter.runLines(lines, Day09.Rope.initialRope(10)).stream()
                .map(Day09.Rope::tail)
                .collect(Collectors.toSet())
                .size();

        Assertions.assertEquals(36, uniqueVisitedCells);
    }
}