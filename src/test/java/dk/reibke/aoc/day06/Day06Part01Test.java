package dk.reibke.aoc.day06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Day06Part01Test {

    public static Stream<Arguments> datastreamInputAndExpectations() {
        return Stream.of(
                Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 5),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 6),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11)
        );
    }

    @ParameterizedTest
    @MethodSource("datastreamInputAndExpectations")
    public void testDataStream(String dataStream, int expectedPosition) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(dataStream));
        Day06Part01.CommunicationSystem communicationSystem = new Day06Part01.CommunicationSystem(new Day06Part01.StartPacketMarker<>(4));

        Assertions.assertEquals(expectedPosition, communicationSystem.findPositionOfStartPacket(bufferedReader));
    }
}