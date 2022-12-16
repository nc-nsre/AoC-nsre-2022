package dk.reibke.aoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileReader {

    public Stream<String> streamFile(String path) throws IOException, URISyntaxException {
        Path filePath = getPath(path);
        return Files.lines(filePath);
    }

    public BufferedReader streamFileAsReader(String path) throws IOException, URISyntaxException {
        Path filePath = getPath(path);
        return Files.newBufferedReader(filePath);
    }

    private Path getPath(String path) throws FileNotFoundException, URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new FileNotFoundException("Unable to find: " + path);
        }
        Path filePath = Paths.get(resource.toURI());
        return filePath;
    }
}
