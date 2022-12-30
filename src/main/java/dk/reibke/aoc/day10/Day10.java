package dk.reibke.aoc.day10;

import dk.reibke.aoc.FileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        SignalStrengthObserver signalStrengthObserver = new SignalStrengthObserver();
        CrtMonitor crtMonitor = new CrtMonitor();
        ClockCircuit clockCircuit = new ClockCircuit(List.of(signalStrengthObserver, crtMonitor));

        System.out.println("== Finding Summed Signal Strength ==");
        new FileReader().streamFile("day10/part01")
                .map(Day10.Operation::fromLine)
                .forEach(clockCircuit::runCommand);

        System.out.println("Final summed signal strength: " + signalStrengthObserver.getSignalStrength());
        System.out.println("== Printing CRT views ==");
        
        List<CrtView> crtViews = crtMonitor.getScreens();
        crtViews.stream()
                .map(CrtView::getView)
                .map(view -> {
                    String breaker = IntStream.range(0, 40).mapToObj(i -> "=").collect(Collectors.joining());
                    return breaker + "\n" + view + "\n" + breaker;
                })
                .forEach(System.out::println);

    }

    public static class CrtMonitor implements CycleObserver {
        private long spritePosition = 1;

        String currentLine = "";

        List<String> currentCrtView = new ArrayList<>();

        List<CrtView> views = new ArrayList<>();

        @Override
        public void observe(long cycle, long value) {
            if (cycle == 0) {
                return;
            }
            draw(cycle, value);
            if (cycle % 40 == 0) {
                reposition();
            }
        }

        private void reposition() {
            currentCrtView.add(currentLine);
            currentLine = "";
            if (currentCrtView.size() >= 6) {
                views.add(new CrtView(currentCrtView));
                currentCrtView = new ArrayList<>();
            }
        }

        private void draw(long cycle, long value) {
            spritePosition = value % 40;
            if (isOverlappingSprite((cycle - 1) % 40)) {
                currentLine = currentLine + "#";
            } else {
                currentLine = currentLine + ".";
            }
        }

        private boolean isOverlappingSprite(long cycle) {
            return cycle >= spritePosition - 1 && cycle <= spritePosition + 1;
        }

        public List<CrtView> getScreens() {
            return this.views;
        }
    }

    public record CrtView(List<String> view) {

        public String getView() {
            return String.join("\n", view);
        }
    }

    public static class SignalStrengthObserver implements CycleObserver {

        private long signalStrength = 0;

        @Override
        public void observe(long cycle, long value) {
            if ((cycle - 20L) % 40L == 0) {
                signalStrength = signalStrength + (value * cycle);
            }
        }

        public long getSignalStrength() {
            return signalStrength;
        }
    }

    public static class ClockCircuit {

        private long value;
        private long cycleCounter;

        private final List<CycleObserver> cycleObserverList;

        public ClockCircuit(List<CycleObserver> cycleObserverList) {
            this.value = 1;
            this.cycleCounter = 0;
            this.cycleObserverList = cycleObserverList;
            this.cycleObserverList.forEach(cycleObserver -> cycleObserver.observe(this.cycleCounter, this.value));
        }

        public void runCommand(Operation operation) {
            switch (operation.command()) {
                case NOOP -> cycle();
                case ADDX -> {
                    cycle();
                    cycle();
                    value = value + operation.Value();
                }
            }
        }

        private void cycle() {
            cycleCounter = cycleCounter + 1;
            cycleObserverList.forEach(cycleObserver -> cycleObserver.observe(cycleCounter, value));
        }
    }

    public interface CycleObserver {
        void observe(long cycle, long value);
    }

    public record Operation(Command command, long Value) {

        public static Operation fromLine(String line) {
            String[] parts = line.split(" ");

            Command command = Command.fromKey(parts[0]);
            if (parts.length > 1) {
                long value = Long.parseLong(parts[1]);

                return new Operation(command, value);
            }
            return new Operation(command, 0);
        }
    }

    public enum Command {
        NOOP("noop"), ADDX("addx");

        private static Map<String, Command> keyMap = Arrays.stream(Command.values()).collect(Collectors.toMap(Command::getKey, command -> command));
        private final String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public static Command fromKey(String key) {
            return keyMap.get(key);
        }
    }
}
