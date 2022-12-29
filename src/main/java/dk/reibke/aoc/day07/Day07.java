package dk.reibke.aoc.day07;

import dk.reibke.aoc.FileReader;
import dk.reibke.aoc.StreamUtility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day07 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Stream<String> lines = new FileReader().streamFile("day07/part01");

        Day07.LineInterpreter lineInterpreter = new Day07.LineInterpreter();
        Day07.Directory directory = lineInterpreter.generateStructure(lines);

        findSumOfDirectoriesBelow100k(directory);
        findLeastAmountToRemove(directory);
    }

    private static void findLeastAmountToRemove(Directory directory) {
        System.out.println("== Find least amount to remove ==");
        long totalDiskSpace = 7_0000_000L;
        long usedSpace = directory.recursiveFileSize();

        long requiredFreeSpace = 3_0000_000L;
        long actualFreeSpace = totalDiskSpace - usedSpace;
        long leastAmountToRemove = requiredFreeSpace - actualFreeSpace;

        System.out.println("used Space: " + usedSpace);
        System.out.println("actual Free space: " + actualFreeSpace);
        System.out.println("Least amount required to be removed: " + leastAmountToRemove);

        long leastAboveToRemove = directory.listDirectories().stream()
                .mapToLong(Directory::recursiveFileSize)
                .filter(value -> value >= leastAmountToRemove)
                .min()
                .orElse(0);

        System.out.println("Least amount possible to remove: " + leastAboveToRemove);
    }

    private static void findSumOfDirectoriesBelow100k(Directory directory) {
        System.out.println("== Finding sum of directories below 100k size ==");
        long actualSum = directory.listDirectories().stream()
                .mapToLong(Directory::recursiveFileSize)
                .filter(value -> value <= 100_000L)
                .sum();

        System.out.println("Sum of directories below 100K: " + actualSum);
    }

    public interface Command {
        Directory action(List<String> lines, Directory currentDirectory);
    }

    public static class CD implements Command {

        @Override
        public Directory action(List<String> lines, Directory currentDirectory) {
            if (lines.size() != 1) {
                throw new RuntimeException("Only 1 line allowed for command, lines: [" + lines.stream().collect(Collectors.joining(", ")) + "]");
            }

            String line = lines.get(0);
            if (line.endsWith("..")) {
                return currentDirectory.getParent();
            }
            Pattern pattern = Pattern.compile("\\$ cd (?<directory>[/\\.a-zA-Z]+)");
            Matcher matcher = pattern.matcher(line);
            if(!matcher.find()){
                throw new RuntimeException("Unable to find directory for command: " + line);
            }
            String directory = matcher.group("directory");

            if (currentDirectory != null) {
                return currentDirectory.updateSubDirectory(directory);
            }
            return new Directory(null, directory);
        }
    }

    public static class LS implements Command {

        @Override
        public Directory action(List<String> lines, Directory currentDirectory) {
            lines.stream().skip(1)
                    .forEach(line -> update(currentDirectory, line));

            return currentDirectory;
        }

        private void update(Directory currentDirectory, String line) {
            if(line.startsWith("dir")) {
                currentDirectory.updateSubDirectory(line);
            } else {
                Pattern pattern = Pattern.compile("(?<filesize>\\d+) (?<filename>\\w+)");
                Matcher matcher = pattern.matcher(line);
                if(!matcher.find()){
                    throw new RuntimeException("Unable to process file for output: " + line);
                }
                File file = new File(Long.parseLong(matcher.group("filesize")), matcher.group("filename"));

                currentDirectory.updateFile(file);
            }
        }
    }

    public static class LineInterpreter {

        private static final Map<String, Command> commandMap = Map.of(
                "cd", new CD(),
                "ls", new LS()
        );

        public List<List<String>> partition(Stream<String> lines) {
            return lines.collect(StreamUtility.partionOnFilter((blockStep -> LineType.inferType(blockStep.step()) == LineType.Command)));
        }

        public Directory generateStructure(Stream<String> lines) {
            List<List<String>> partioned = partition(lines);

            Directory currentDirectory = null;
            for (List<String> part: partioned){
                Command command = getCommand(part.get(0));
                currentDirectory = command.action(part, currentDirectory);
            }
            return currentDirectory.findTopDirectory();
        }

        private Command getCommand(String line) {
            Pattern pattern = Pattern.compile("\\$ (?<command>\\w+)( (?<directory>\\w+))?");
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                return commandMap.get(matcher.group("command"));
            }

            throw new RuntimeException("No command found");
        }

        private boolean isDirectory(String line) {
            return line.startsWith("dir");
        }
    }

    public enum LineType {
        Command, Output;

        public static LineType inferType(String line) {
            return line.startsWith("$") ? Command : Output;
        }
    }

    public static class Directory {

        private final Directory parent;
        private final Map<String, Directory> subDirectory;
        private final Map<String, File> files;
        private final String directoryName;

        public Directory(Directory parent, String directoryName) {
            this.parent = parent;
            this.directoryName = directoryName;
            subDirectory = new HashMap<>();
            files = new HashMap<>();
        }

        public void updateFolder(List<File> files) {
            files.forEach(file -> this.files.put(file.getFileName(), file));
        }

        public Directory updateSubDirectory(String directory) {
            subDirectory.putIfAbsent(directory, new Directory(this, directory));
            return subDirectory.get(directory);
        }

        public long recursiveFileSize() {
            long currentFolderFileSize = files.values().stream()
                    .mapToLong(File::getFileSize)
                    .sum();

            long subDirectoryFileSizes = subDirectory.values().stream()
                    .mapToLong(Directory::recursiveFileSize)
                    .sum();

            return currentFolderFileSize + subDirectoryFileSizes;
        }

        public Directory getParent() {
            return this.parent;
        }

        public void updateFile(File file) {
            files.put(file.getFileName(), file);
        }

        public List<Directory> listDirectories() {
            List<Directory> directories = new ArrayList<>();
            directories.add(this);
            subDirectory.values().stream()
                    .map(Directory::listDirectories)
                    .forEach(directories::addAll);

            return directories;
        }

        public Directory findTopDirectory() {
            if (parent != null) {
                return parent.findTopDirectory();
            }
            return this;
        }
    }

    public static class File {

        private final long fileSize;
        private final String fileName;

        public File(long fileSize, String fileName) {
            this.fileSize = fileSize;
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public String getFileName() {
            return fileName;
        }
    }

}
