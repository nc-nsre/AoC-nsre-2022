package dk.reibke.aoc.day10;

import dk.reibke.aoc.FileReader;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Day10Test {

    public static class AllMonitor implements Day10.CycleObserver {

        private final Map<Long, Long> cycleValueMap = new HashMap<>();

        @Override
        public void observe(long cycle, long value) {
            cycleValueMap.put(cycle, value);
        }
    }

    @Test
    public void testSimpleSample() {

        AllMonitor allMonitor = new AllMonitor();
        Day10.ClockCircuit clockCircuit = new Day10.ClockCircuit(List.of(allMonitor));

        clockCircuit.runCommand(new Day10.Operation(Day10.Command.NOOP, 0));
        clockCircuit.runCommand(new Day10.Operation(Day10.Command.ADDX, 3));
        clockCircuit.runCommand(new Day10.Operation(Day10.Command.ADDX, -5));

        Assertions.assertEquals(1, allMonitor.cycleValueMap.get(0L));
        Assertions.assertEquals(1, allMonitor.cycleValueMap.get(1L));
        Assertions.assertEquals(1, allMonitor.cycleValueMap.get(2L));
        Assertions.assertEquals(1, allMonitor.cycleValueMap.get(3L));
        Assertions.assertEquals(4, allMonitor.cycleValueMap.get(4L));
        Assertions.assertEquals(4, allMonitor.cycleValueMap.get(5L));

    }

    @Test
    public void testLargeSample() throws IOException, URISyntaxException {
        AllMonitor allMonitor = new AllMonitor();
        Day10.SignalStrengthObserver signalStrengthObserver = new Day10.SignalStrengthObserver();
        Day10.ClockCircuit clockCircuit = new Day10.ClockCircuit(List.of(allMonitor, signalStrengthObserver));

        new FileReader().streamFile("day10/part01")
                .map(Day10.Operation::fromLine)
                .forEach(clockCircuit::runCommand);

        Assertions.assertEquals(420, allMonitor.cycleValueMap.get(20L) * 20L);
        Assertions.assertEquals(1140, allMonitor.cycleValueMap.get(60L) * 60L);
        Assertions.assertEquals(1800, allMonitor.cycleValueMap.get(100L) * 100L);
        Assertions.assertEquals(2940, allMonitor.cycleValueMap.get(140L) * 140L);
        Assertions.assertEquals(2880, allMonitor.cycleValueMap.get(180L) * 180L);
        Assertions.assertEquals(3960, allMonitor.cycleValueMap.get(220L) * 220L);

        Assertions.assertEquals(13140, signalStrengthObserver.getSignalStrength());
    }

    @Test
    public void testCrtView() throws IOException, URISyntaxException {
        Day10.CrtMonitor crtMonitor = new Day10.CrtMonitor();
        AllMonitor allMonitor = new AllMonitor();
        Day10.ClockCircuit clockCircuit = new Day10.ClockCircuit(List.of(crtMonitor, allMonitor));

        new FileReader().streamFile("day10/part01")
                .map(Day10.Operation::fromLine)
                .forEach(clockCircuit::runCommand);

        String expectedOutput = new FileReader().streamFile("day10/part02")
                .collect(Collectors.joining("\n"));

        String crtView = crtMonitor.getScreens().stream()
                .map(Day10.CrtView::getView)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Missing value"));

        Assertions.assertEquals(expectedOutput, crtView);
    }
}