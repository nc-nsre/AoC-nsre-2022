package dk.reibke.aoc.day06;

import dk.reibke.aoc.FileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;

public class Day06Part01 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        BufferedReader bufferedReader = new FileReader().streamFileAsReader("day06/part01");
        CommunicationSystem communicationSystem = new CommunicationSystem(new StartPacketMarker<>(14));

        System.out.println(communicationSystem.findPositionOfStartPacket(bufferedReader));
    }

    public static class CommunicationSystem {
        private final StartPacketMarker<Integer> startPacketMarker;

        public CommunicationSystem(StartPacketMarker<Integer> startPacketMarker) {
            this.startPacketMarker = startPacketMarker;
        }

        int findPositionOfStartPacket(BufferedReader reader) throws IOException {
            int position = 1;
            while (true) {
                int read = reader.read();
                if (read == -1) {
                    throw new RuntimeException("End of Reader reached");
                }
                if (startPacketMarker.isMark(read)) {
                    return position;
                }
                position++;
            }
        }
    }

    public static class StartPacketMarker<T> {
        private final int markerSize;

        private final LinkedList<T> buffer;

        public StartPacketMarker(int markerSize) {
            this.markerSize = markerSize;
            buffer = new LinkedList<>();
        }

        boolean isMark(T data) {
            if (buffer.size() >= markerSize) {
                buffer.removeFirst();
            }

            buffer.add(data);
            return new HashSet<T>(buffer).size() == buffer.size() && buffer.size() == markerSize;
        }
    }
}
