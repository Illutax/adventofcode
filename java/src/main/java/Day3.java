import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Day3 {

    public static void main(String[] args) throws IOException {
        File file = new File("input3.txt");
        int[][] grid = createGrid(file);

        assert 176 == q1(grid);
			assert 1452545024 < q2(grid);
    }

    private static int q1(int[][] grid) {
        int amountOfTrees = getAmountOfTrees(grid, new Point(3, 1));
        System.out.println(amountOfTrees);

        return amountOfTrees;
    }

    private static long q2(int[][] grid) {
        final int amountOfTrees1 = getAmountOfTrees(grid, new Point(1, 1));
        final int amountOfTrees2 = getAmountOfTrees(grid, new Point(3, 1));
        final int amountOfTrees3 = getAmountOfTrees(grid, new Point(5, 1));
        final int amountOfTrees4 = getAmountOfTrees(grid, new Point(7, 1));
        final int amountOfTrees5 = getAmountOfTrees(grid, new Point(1, 2));
        long amountOfTrees =
                amountOfTrees1
                        * amountOfTrees2
                        * amountOfTrees3
                        * amountOfTrees4
                        * amountOfTrees5;
        System.out.println(amountOfTrees);
        assert amountOfTrees > 1452545024;
        return amountOfTrees;
    }

    private static int[][] createGrid(File file) throws IOException {
        int[][] grid = new int[323][]; // y-x
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int row = 0;
            for (String line; (line = br.readLine()) != null; ++row) {
                grid[row] = Arrays.stream(line.split(""))
                        .mapToInt(c -> c.equals(".") ? 0 : 1)
                        .toArray();
            }

            // Debug
            //			System.out.println("Amount of rows: " + row);
            //			for (int[] r : grid)
            //			{
            //				for (int c : r)
            //				{
            //					System.out.print(c);
            //				}
            //				System.out.print("\n");
            //			}
        }
        return grid;
    }

    private static int getAmountOfTrees(int[][] grid, Point slope) {
        int amountOfTrees = 0;
        Point init = new Point(0, 0);
        while (init.y + slope.y <= 323) {
            amountOfTrees += grid[init.y][init.x];
            wrappedTranslate(init, slope);
        }
        return amountOfTrees;
    }

    private static void wrappedTranslate(Point pos, Point slope) {
        pos.translate(slope.x, slope.y);
        pos.move(pos.x % 31, pos.y);
    }
}
