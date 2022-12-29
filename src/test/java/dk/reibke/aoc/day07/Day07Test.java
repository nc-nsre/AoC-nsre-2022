package dk.reibke.aoc.day07;

import dk.reibke.aoc.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

class Day07Test {

    @Test
    public void testSegmentation() {
        Stream<String> lines = Stream.of("$ cd /",
                "$ ls",
                "dir a",
                "14848514 b.txt",
                "8504156 c.dat",
                "dir d",
                "$ cd a",
                "$ ls",
                "dir e",
                "29116 f",
                "2557 g",
                "62596 h.lst");

        Day07.LineInterpreter lineInterpreter = new Day07.LineInterpreter();
        List<List<String>> partition = lineInterpreter.partition(lines);

        Assertions.assertEquals(4, partition.size());
        Assertions.assertEquals(1, partition.get(0).size());
        Assertions.assertEquals(5, partition.get(1).size());
        Assertions.assertEquals(1, partition.get(2).size());
        Assertions.assertEquals(5, partition.get(3).size());
    }

    @Test
    public void testDirectoriesBelow100k() throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day07/part01");

        Day07.LineInterpreter lineInterpreter = new Day07.LineInterpreter();
        Day07.Directory directory = lineInterpreter.generateStructure(lines);

        long actualSum = directory.listDirectories().stream()
                .mapToLong(Day07.Directory::recursiveFileSize)
                .filter(value -> value <= 100_000L)
                .sum();

        Assertions.assertEquals(95437, actualSum);
    }

}