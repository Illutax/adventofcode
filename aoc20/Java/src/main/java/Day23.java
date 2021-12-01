import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public class Day23 {
    public static void main(String[] args) {
        final String inputPart1 = "198753462";
        part1(inputPart1);
    }

    private static String part1(String input) {
        final int[] buffer = Arrays.stream(input.split("")).mapToInt(Integer::parseInt).toArray();
        doMoves(0, 100, buffer);
        return generateOrdering(buffer);
    }

    static String generateOrdering(int[] buffer) {
        StringBuilder ordering = new StringBuilder();
        int i = 0;
        boolean foundOne = false;
        while(ordering.length() < buffer.length-1) {
            if (!foundOne) {
                if (buffer[i++] == 1)
                    foundOne = true;
            } else {
                ordering.append(buffer[(i++) % buffer.length]);
            }
        }
        return ordering.toString();
    }

    static void doMoves(int currentCupIndex, int amountOfTurns, int[] buffer) {
        if (amountOfTurns-- > 0) {
            // pick up 3 cups after the currentCupIndex index
            // select destination where:
            final int destination = selectDestination(currentCupIndex, buffer);
            //  * destination = currentCupIndex - 1
            //      * if this index is not one of the picked up cups
            //      * otherwise try to assign -1 of that
            //      * if underflows to -1 try again on the biggest index (9 at first)
            shiftSelected(currentCupIndex, destination, buffer);
            //  * select a new current cup

            doMoves(currentCupIndex, amountOfTurns, buffer);
        }
    }

    static void shiftSelected(int selectedIndex, int destinationNumber,
                                      int[] buffer) {
        int selectedAmount = 0;
        int restAmount = 0;
        int[] selected = new int[3];
        int[] withoutSelected = new int[buffer.length-3];
        for (int i = 0; i < buffer.length; i++) {
            if (i >= selectedIndex && i <= (selectedIndex + 3) % buffer.length) {
                selected[selectedAmount++] = buffer[i];
            } else {
                withoutSelected[restAmount++] = buffer[i];
            }
        }
        int indexOfDestination = IntStream.range(0, buffer.length)
                        .filter(i -> buffer[i] == destinationNumber).findFirst()
                        .orElseThrow();
        //  * place the 3 picked up cups after the destination cup (same order)

        //shift by one
        if (false && indexOfDestination-selectedIndex == 4) {
            while (indexOfDestination > selectedIndex + 1) {
                swap(indexOfDestination, --indexOfDestination, buffer);
            }
        }
    }

    static int selectDestination(int selectedIndex, int[] buffer) {
        final List<Integer> tmp = new ArrayList<>();
        for (int ch: buffer) tmp.add(ch);
        for(int i=0; i< 4; ++i) tmp.remove(selectedIndex);

        int desiredDestination = buffer[selectedIndex];
        do {
            desiredDestination = Math.floorMod(desiredDestination - 1, 10);
        } while (!tmp.contains(desiredDestination));
        return desiredDestination;
    }

    private static void swap(int i, int j, int[] arr) {
        final int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }
}
