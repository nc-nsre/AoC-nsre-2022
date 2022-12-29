package dk.reibke.aoc.day08;

import dk.reibke.aoc.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.reibke.aoc.CollectionUtility.*;
import static dk.reibke.aoc.CollectionUtility.isNotEqual;

class Day08Test {

    @Test
    public void testTreeLineObservationAscendingOrder() {
        Map<Integer, Day08.Tree> treeMap = Map.of(
                0, new Day08.Tree(3),
                1, new Day08.Tree(0),
                2, new Day08.Tree(3),
                3, new Day08.Tree(7),
                4, new Day08.Tree(3)
        );

        Day08.TreeLineObserver treeLineObserver = new Day08.TreeLineObserver();

        Set<Day08.Tree> treesFromOrder = treeLineObserver.findTreesFromOrder(treeMap, Day08.TreeLineObserver.AscendingOrder());

        Assertions.assertTrue(treesFromOrder.contains(treeMap.get(0)));
        Assertions.assertFalse(treesFromOrder.contains(treeMap.get(1)));
        Assertions.assertFalse(treesFromOrder.contains(treeMap.get(2)));
        Assertions.assertTrue(treesFromOrder.contains(treeMap.get(3)));
        Assertions.assertFalse(treesFromOrder.contains(treeMap.get(4)));
    }

    @Test
    public void testTreeLineObservationDescendingOrder() {
        Map<Integer, Day08.Tree> treeMap = Map.of(
                0, new Day08.Tree(3),
                1, new Day08.Tree(0),
                2, new Day08.Tree(3),
                3, new Day08.Tree(7),
                4, new Day08.Tree(3)
        );

        Day08.TreeLineObserver treeLineObserver = new Day08.TreeLineObserver();

        Set<Day08.Tree> treesFromOrder = treeLineObserver.findTreesFromOrder(treeMap, Day08.TreeLineObserver.DescendingOrder());

        Assertions.assertFalse(treesFromOrder.contains(treeMap.get(0)));
        Assertions.assertFalse(treesFromOrder.contains(treeMap.get(1)));
        Assertions.assertFalse(treesFromOrder.contains(treeMap.get(2)));
        Assertions.assertTrue(treesFromOrder.contains(treeMap.get(3)));
        Assertions.assertTrue(treesFromOrder.contains(treeMap.get(4)));
    }

    @Test
    public void testPopulatingForrest() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
    }

    @Test
    public void TestVisibleTreesEast() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Set<Day08.Tree> trees = forrest.eastVisibleTrees();

        Assertions.assertEquals(11, trees.size());
    }

    @Test
    public void TestVisibleTreesNorth() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Set<Day08.Tree> trees = forrest.northVisibleTrees();

        Assertions.assertEquals(10, trees.size());
    }

    @Test
    public void TestVisibleTreesWest() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Set<Day08.Tree> trees = forrest.westVisibleTrees();

        Assertions.assertEquals(11, trees.size());
    }

    @Test
    public void TestVisibleTreesSouth() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Set<Day08.Tree> trees = forrest.southVisibleTrees();

        Assertions.assertEquals(8, trees.size());
    }

    @Test
    public void TestVisibleTreesTotal() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Set<Day08.Tree> trees = forrest.totalVisibleTrees();

        Assertions.assertEquals(21, trees.size());
    }

    @Test
    public void testSpecificTrees() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Set<Day08.Tree> trees = forrest.totalVisibleTrees();

        Assertions.assertTrue(trees.contains(forrest.getTree(1,1)));
        Assertions.assertTrue(trees.contains(forrest.getTree(1,2)));
        Assertions.assertFalse(trees.contains(forrest.getTree(1,3)));
        Assertions.assertTrue(trees.contains(forrest.getTree(2,1)));
        Assertions.assertFalse(trees.contains(forrest.getTree(2,2)));
        Assertions.assertFalse(trees.contains(forrest.getTree(3,1)));
        Assertions.assertTrue(trees.contains(forrest.getTree(3,2)));
        Assertions.assertFalse(trees.contains(forrest.getTree(3,3)));

    }

    @Test
    public void testTreeHouseScenicScoreCaseOneDetailed() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.LocatedTree locatedTree = new Day08.LocatedTree(forrest.getTree(1,2), 1,2);

        Map<Integer, Day08.Tree> row = forrest.getRow(1);
        Map<Integer, Day08.Tree> column = forrest.getColumn(2);

        Day08.TreeHouseLocation treeHouseLocation = new Day08.TreeHouseLocation(locatedTree, row, column);

        Assertions.assertEquals(4, treeHouseLocation.getScenicScore());
    }

    @Test
    public void testTreeHouseScenicScoreCaseOneDetailed_east() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.LocatedTree locatedTree = new Day08.LocatedTree(forrest.getTree(1,2), 1,2);

        Map<Integer, Day08.Tree> row = forrest.getRow(1);
        Map<Integer, Day08.Tree> column = forrest.getColumn(2);

        Day08.TreeHouseLocation treeHouseLocation = new Day08.TreeHouseLocation(locatedTree, row, column);

        int eastScore = treeHouseLocation.findScore(locatedTree, row, new NextIterator<>(locatedTree.getColumn(), countUp(), isNotEqual(row.size() - 1)));

        Assertions.assertEquals(2, eastScore);
    }

    @Test
    public void testTreeHouseScenicScoreCaseOneDetailed_west() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.LocatedTree locatedTree = new Day08.LocatedTree(forrest.getTree(1,2), 1,2);

        Map<Integer, Day08.Tree> row = forrest.getRow(1);
        Map<Integer, Day08.Tree> column = forrest.getColumn(2);

        Day08.TreeHouseLocation treeHouseLocation = new Day08.TreeHouseLocation(locatedTree, row, column);

        int westScore = treeHouseLocation.findScore(locatedTree, row, new NextIterator<>(locatedTree.getColumn(), countDown(), isNotEqual(0)));

        Assertions.assertEquals(1, westScore);
    }

    @Test
    public void testTreeHouseScenicScoreCaseOneDetailed_north() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.LocatedTree locatedTree = new Day08.LocatedTree(forrest.getTree(1,2), 1,2);

        Map<Integer, Day08.Tree> row = forrest.getRow(1);
        Map<Integer, Day08.Tree> column = forrest.getColumn(2);

        Day08.TreeHouseLocation treeHouseLocation = new Day08.TreeHouseLocation(locatedTree, row, column);

        int northScore = treeHouseLocation.findScore(locatedTree, column, new NextIterator<>(locatedTree.getRow(), countDown(), isNotEqual(0)));

        Assertions.assertEquals(1, northScore);
    }

    @Test
    public void testTreeHouseScenicScoreCaseOneDetailed_south() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.LocatedTree locatedTree = new Day08.LocatedTree(forrest.getTree(1,2), 1,2);

        Map<Integer, Day08.Tree> row = forrest.getRow(1);
        Map<Integer, Day08.Tree> column = forrest.getColumn(2);

        Day08.TreeHouseLocation treeHouseLocation = new Day08.TreeHouseLocation(locatedTree, row, column);

        int southScore = treeHouseLocation.findScore(locatedTree, column, new NextIterator<>(locatedTree.getRow(), countUp(), isNotEqual(column.size() - 1)));

        Assertions.assertEquals(2, southScore);
    }

    @Test
    public void testTreeHouseScenicScoreCaseOne() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.TreeHouseLocation treeHouseLocation = forrest.scenicScores()
                .stream()
                .filter(house -> house.getLocatedTree().getRow() == 1 && house.getLocatedTree().getColumn() == 2)
                .toList()
                .get(0);

        Assertions.assertEquals(4, treeHouseLocation.getScenicScore());
    }

    @Test
    public void testTreeHouseScenicScoreCaseTwo() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.TreeHouseLocation treeHouseLocation = forrest.scenicScores()
                .stream()
                .filter(house -> house.getLocatedTree().getRow() == 3 && house.getLocatedTree().getColumn() == 2)
                .toList()
                .get(0);

        Assertions.assertEquals(8, treeHouseLocation.getScenicScore());
    }

    @Test
    public void testTreeHouseBestScenicScore() throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);
        Day08.TreeHouseLocation treeHouseLocation = forrest.bestTreeHouseLocation();

        Assertions.assertEquals(8, treeHouseLocation.getScenicScore());
        Assertions.assertEquals(forrest.getTree(3,2), treeHouseLocation.getLocatedTree().getTree());
    }
}