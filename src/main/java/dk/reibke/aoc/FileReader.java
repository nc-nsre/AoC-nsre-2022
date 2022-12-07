package dk.reibke.aoc;

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
        URL resource = this.getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new FileNotFoundException("Unable to find: " + path);
        }
        Path filePath = Paths.get(resource.toURI());
        return Files.lines(filePath);
    }
}
