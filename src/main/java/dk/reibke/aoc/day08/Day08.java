package dk.reibke.aoc.day08;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import dk.reibke.aoc.CollectionUtility.NextIterator;
import dk.reibke.aoc.FileReader;
import dk.reibke.aoc.CollectionUtility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

import static dk.reibke.aoc.CollectionUtility.*;

public class Day08 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        List<String> strings = new FileReader().streamFile("day08/part01").toList();

        Day08.Forrest forrest = new Day08.Forrest(strings);

        System.out.println("== Finding visible Trees ==");
        Set<Day08.Tree> trees = forrest.totalVisibleTrees();
        System.out.println("Visible trees: " + trees.size());

        System.out.println("== Finding best Tree House Scenic view");
        TreeHouseLocation treeHouseLocation = forrest.bestTreeHouseLocation();
        System.out.println("Best scenic view possible: " + treeHouseLocation);
    }

    public static class Forrest {
        private final Table<Integer, Integer, Tree> trees;
        private final int rowSize;
        private final int columnSize;

        public Forrest(List<String> forrestLines) {
            trees = TreeBasedTable.create();
            populateForrest(forrestLines);
            rowSize = trees.rowKeySet().size();
            columnSize = trees.columnKeySet().size();
        }

        private void populateForrest(List<String> lines) {
            for (int row = 0; row < lines.size(); row++) {
                List<Character> treeLine = lines.get(row).chars().mapToObj(c -> (char) c).toList();
                for (int column = 0; column < treeLine.size(); column++) {
                    trees.put(row, column, new Tree(Character.digit(treeLine.get(column), 10)));
                }
            }
        }

        public Set<Tree> eastVisibleTrees() {
            return trees.rowMap().values().stream()
                    .map(column -> new TreeLineObserver().findTreesFromOrder(column, TreeLineObserver.DescendingOrder()))
                    .collect(SetCollector());
        }



        public Set<Tree> northVisibleTrees() {
            return trees.columnMap().values().stream()
                    .map(column -> new TreeLineObserver().findTreesFromOrder(column, TreeLineObserver.AscendingOrder()))
                    .collect(SetCollector());
        }

        public Set<Tree> westVisibleTrees() {
            return trees.rowMap().values().stream()
                    .map(column -> new TreeLineObserver().findTreesFromOrder(column, TreeLineObserver.AscendingOrder()))
                    .collect(SetCollector());
        }

        public Set<Tree> southVisibleTrees() {
            return trees.columnMap().values().stream()
                    .map(column -> new TreeLineObserver().findTreesFromOrder(column, TreeLineObserver.DescendingOrder()))
                    .collect(SetCollector());
        }

        public Set<Tree> totalVisibleTrees() {
            return Stream.of(
                    southVisibleTrees(),
                    westVisibleTrees(),
                    northVisibleTrees(),
                    eastVisibleTrees()
            ).collect(SetCollector());
        }

        public Tree getTree(int row, int column) {
            return trees.get(row, column);
        }

        public List<TreeHouseLocation> scenicScores() {
            return locatedTrees().stream()
                    .map(this::findScenicScore)
                    .toList();
        }

        public TreeHouseLocation bestTreeHouseLocation() {
            return scenicScores().stream()
                    .max(Comparator.comparingInt(TreeHouseLocation::getScenicScore))
                    .orElse(null);
        }

        private TreeHouseLocation findScenicScore(LocatedTree tree) {
            return new TreeHouseLocation(tree, trees.row(tree.getRow()), trees.column(tree.getColumn()));
        }

        public List<LocatedTree> locatedTrees() {
            return trees.rowMap().entrySet().stream()
                    .map(row -> row.getValue().entrySet().stream()
                            .map(e -> new LocatedTree(e.getValue(), row.getKey(), e.getKey())).toList()
                    ).collect(ListCollector());

        }

        Map<Integer, Tree> getRow(int row) {
            return trees.row(row);
        }

        Map<Integer, Tree> getColumn(int column) {
            return trees.column(column);
        }
    }

    public static class TreeHouseLocation {

        private final int scenicScore;
        private final LocatedTree locatedTree;

        public TreeHouseLocation(LocatedTree locatedTree, Map<Integer, Tree> row, Map<Integer, Tree> column) {
            this.locatedTree = locatedTree;
            if (isOnEdge(locatedTree, row.size(), column.size())) {
                this.scenicScore = 0;
            } else {
                int eastScore = findScore(locatedTree, row, new NextIterator<>(locatedTree.getColumn(), countUp(), isNotEqual(row.size() - 1)));
                int westScore = findScore(locatedTree, row, new NextIterator<>(locatedTree.getColumn(), countDown(), isNotEqual(0)));
                int northScore = findScore(locatedTree, column, new NextIterator<>(locatedTree.getRow(), countDown(), isNotEqual(0)));
                int southScore = findScore(locatedTree, column, new NextIterator<>(locatedTree.getRow(), countUp(), isNotEqual(column.size() - 1)));

                this.scenicScore = eastScore * westScore * northScore * southScore;
            }
        }

        public int findScore(LocatedTree locatedTree, Map<Integer, Tree> treeLine, CollectionUtility.NextIterator<Integer> nextIterator) {
            int score = 0;
            while(nextIterator.hasNext()) {

                int i = nextIterator.next();
                Tree nextTree = treeLine.get(i);
                score = score + 1;
                if (nextTree == null) {
                    throw new IndexOutOfBoundsException("Tree index out of bound: " + i);
                }
                if (nextTree.getHeight() >= locatedTree.getTree().getHeight()) {
                    break;
                }
            }
            return score;
        }

        private boolean isOnEdge(LocatedTree tree, int rowSize, int columnSize) {
            return tree.getRow() <= 0 || tree.getRow() >= rowSize - 1 || tree.getColumn() <= 0 || tree.getColumn() >= columnSize - 1;
        }

        public int getScenicScore() {
            return scenicScore;
        }

        public LocatedTree getLocatedTree() {
            return locatedTree;
        }

        @Override
        public String toString() {
            return "TreeHouseLocation{" +
                    "scenicScore=" + scenicScore +
                    ", locatedTree=" + locatedTree +
                    '}';
        }
    }

    public static class TreeLineObserver {

        public static Comparator<Map.Entry<Integer, Tree>> AscendingOrder() {
            return Comparator.comparingInt(Map.Entry::getKey);
        }

        public static Comparator<Map.Entry<Integer, Tree>> DescendingOrder() {
            Comparator<Map.Entry<Integer, Tree>> comparator = Comparator.comparingInt(Map.Entry::getKey);
            return comparator.reversed();
        }

        public Set<Tree> findTreesFromOrder(Map<Integer, Tree> column, Comparator<Map.Entry<Integer, Tree>> ordering) {
            Set<Tree> visibleTrees = new HashSet<>();

            int highestSoFar = -1;
            List<Map.Entry<Integer, Tree>> entries = new ArrayList<>(column.entrySet().stream().toList());
            entries.sort(ordering);
            for (Map.Entry<Integer, Tree> treeEntry : entries) {
                Tree tree = treeEntry.getValue();
                if (highestSoFar >= tree.height) {
                    continue;
                }

                visibleTrees.add(tree);
                highestSoFar = tree.height;
            }

            return visibleTrees;
        }
    }

    public static class Tree {
        private final int height;
        private final UUID uuid;

        public Tree(int height) {
            this.height = height;
            uuid = UUID.randomUUID();
        }

        public int getHeight() {
            return height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tree tree)) return false;
            return height == tree.height && Objects.equals(uuid, tree.uuid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(height, uuid);
        }

        @Override
        public String toString() {
            return "Tree{" +
                    "height=" + height +
                    ", uuid=" + uuid +
                    '}';
        }
    }

    public static class LocatedTree {

        private final int row;
        private final int column;
        private final Tree tree;

        public LocatedTree(Tree tree, int row, int column) {
            this.tree = tree;
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public Tree getTree() {
            return tree;
        }

        @Override
        public String toString() {
            return "LocatedTree{" +
                    "row=" + row +
                    ", column=" + column +
                    ", tree=" + tree +
                    '}';
        }
    }
}
