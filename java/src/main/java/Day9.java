import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

@Slf4j
public class Day9 {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        final String path = "java/input9.txt";
        final CircularFifoBuffer buffer = new CircularFifoBuffer();
        Files.lines(Paths.get(path)).mapToInt(Integer::parseInt).forEach(data -> {
            if (buffer.size() < buffer.maxSize() || areTwoDifferentAddendsInPreamble(buffer, data))
                buffer.add(data);
            else {
                log.error("Not in preamble: {}", data);
            }
        });
    }

    public static boolean areTwoDifferentAddendsInPreamble(Collection<Integer> preamble, int number) {
        return preamble.stream().anyMatch(data1 -> preamble.contains(number - data1) && data1 != number - data1);
    }
}
