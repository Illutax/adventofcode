package tech.dobler.aoc22;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Day07 {

    // formatter: off
    sealed interface Node permits DirectoryNode, FileNode {
        String name();
    }

    record DirectoryNode(String name, Map<String, Node> children) implements Node {
        DirectoryNode(String name, DirectoryNode parent) {
            this(name, new LinkedHashMap<>());
            addChild("..", parent);
        }

        DirectoryNode(String name) {
            this(name, new LinkedHashMap<>());
        }


        public Node getChildrenOfName(String destination) {
            if (!children.containsKey(destination))
                return null;
            return children.get(destination);
        }

        public void hydrateDirectorySizes(Map<DirectoryNode, Long> map) {
            for (var entry : this.children.entrySet()) { // NOSONAR
                if (entry.getKey().equals("..")) continue;
                if (!(entry.getValue() instanceof DirectoryNode dn)) continue;
                dn.hydrateDirectorySizes(map);
            }
            final var dirSize = calcSize();
            if (map.containsKey(this))
                throw new IllegalStateException("Already contains this node " + this + " with size " + map.get(this) + " current size: " + dirSize);
            map.put(this, dirSize);
        }

        private long calcSize() {
            return children.entrySet()
                    .stream()
                    .filter(it -> !it.getKey().equals(".."))
                    .mapToLong(it -> {
                        final var node = it.getValue();
                        if (node instanceof FileNode fn) {
                            return fn.size;
                        }
                        return ((DirectoryNode) node).calcSize();
                    }).sum();
        }

        private void addChild(String name, Node node) {
            children.put(name, node);
        }

        public void addChildFile(FileOutput file, DirectoryNode parent) {
            ensureNoNodeOfName(file.name);
            final var node = new FileNode(file.name, parent, file.size);
            addChild(file.name, node);
        }

        public void addChildDirectory(DirectoryOutput dir, DirectoryNode parent) {
            ensureNoNodeOfName(dir.name);
            final var node = new DirectoryNode(dir.name, parent);
            parent.addChild(dir.name, node);
        }

        private DirectoryNode getParent() {
            return (DirectoryNode) getChildrenOfName("..");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DirectoryNode that)) return false;

            return name.equals(that.name)
                    && children.size() == that.children.size()
                    && Objects.equals(getParent(), that.getParent())
                    && getPath().equals(that.getPath());
        }

        public String getPath() {
            if (name.equals("/")) return "~";
            assert this.getParent() != null;
            return this.getParent().getPath() + "/" + name; //NOSONAR
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[]{name, children.size(), getPath()});
        }

        @Override
        public String toString() {
            return "[" + name + "] " + children.size() + " children";
        }

        private void ensureNoNodeOfName(String nodeName) {
            for (var child : children.entrySet()) {
                if (child.getKey().equals(nodeName))
                    throw new IllegalStateException("Already contains node of name " + nodeName);
            }
        }

        @SuppressWarnings("unused")
        public String prettyPrint(boolean printExtras) {
            var sb = new StringBuilder();
            return prettyPrintInternal(sb, 0, printExtras, printExtras);
        }

        private String prettyPrintInternal(StringBuilder sb, int indentation, boolean shouldPrintDirSize, boolean shouldPrintFullPath) {
            sb.append("  ".repeat(indentation)).append("- ").append(name).append(" (dir");
            if (shouldPrintDirSize)
                sb.append(", size=").append(this.calcSize());
            if (shouldPrintFullPath)
                sb.append(", path=").append(this.getPath());
            sb.append(")").append(System.lineSeparator());
            for (var entry : children().entrySet()) {
                if (entry.getKey().equals("..")) continue;
                switch (entry.getValue()) {
                    case DirectoryNode dn ->
                            dn.prettyPrintInternal(sb, indentation + 1, shouldPrintDirSize, shouldPrintFullPath);
                    case FileNode fn -> sb.append(fn.prettyPrint(indentation + 1));
                    default -> throw new IllegalStateException("Unknown node " + entry.getValue());
                }
            }
            return sb.toString();
        }
    }

    record FileNode(String name, DirectoryNode parent, long size) implements Node {
        FileNode {
            parent.children.put(name, this);
        }

        public String prettyPrint(int indentation) {
            return "  ".repeat(indentation) + "-" + " " + name + " (file, size=" + size + ")" + System.lineSeparator();
        }
    }

    sealed interface Line permits Output, Command {
    }

    sealed interface Output extends Line permits DirectoryOutput, FileOutput {
        String name();
    }

    record DirectoryOutput(String name) implements Output {
    }

    record FileOutput(String name, long size) implements Output {
    }

    sealed interface Command extends Line permits ChangeDir, ListDir {
    }

    record ChangeDir(String destination) implements Command {
    }

    record ListDir() implements Command {
    }
    // @ formatter: on

    @SuppressWarnings("SameParameterValue")
    private static long findDirectoriesOfSize(DirectoryNode root, long sizeThreshold) {
        final var sizes = new HashMap<DirectoryNode, Long>();
        root.hydrateDirectorySizes(sizes);
        return sizes.values().stream()
                //.peek(it -> Util.printf("%-10s: %10s\n", it.getKey().name, it.getValue()))
                .mapToLong(l -> l)
                .filter(it -> it <= sizeThreshold)
                .sum();
    }

    private static long findDirectoryToDelete(DirectoryNode root, long sizeThreshold) {
        final var sizes = new HashMap<DirectoryNode, Long>();
        root.hydrateDirectorySizes(sizes);
        //.peek(it -> Util.printf("%-10s: %10s\n", it.getKey().name, it.getValue()))
        return sizes.values().stream()
                .filter(it -> it >= sizeThreshold)
                .sorted()
                .findFirst().orElseThrow();
    }

    private static List<Line> translate(List<String> input) {
        return input.stream().map(Day07::translateLine).toList();
    }

    // have to be its own method https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8298566
    // otherwise one inner return internal should be cast explicit, to Line, not the outer switch expression,
    // or otherwise it crashes while compiling
    private static Line translateLine(String line) {
        final var args = line.split(" ");
        final var cmd = args[0];
        final var sub = args[1];
        Optional<String> param = args.length > 2 ? args[2].describeConstable() : Optional.empty();
        return switch (cmd) {
            case "$" -> switch (sub) {
                case "ls" -> new ListDir();
                case "cd" -> new ChangeDir(param.orElseThrow());
                default -> throw new IllegalArgumentException(line);
            };
            case "dir" -> new DirectoryOutput(sub);
            default -> new FileOutput(sub, Long.parseLong(cmd));
        };
    }

    private int parseTree(List<Line> input, int index, DirectoryNode parent) {
        while (index < input.size()) {
            var line = input.get(index);
            switch (line) {
                case ListDir ignored -> {
                    while ((++index < input.size() ? input.get(index) : null) instanceof Output out) {
                        switch (out) { // NOSONAR
                            case DirectoryOutput dir -> parent.addChildDirectory(dir, parent);
                            case FileOutput file -> parent.addChildFile(file, parent);
                        }
                    }
                }
                case ChangeDir cd -> {
                    var node = parent.getChildrenOfName(cd.destination);
                    index = parseTree(input, ++index, (DirectoryNode) node);
                }
                default -> throw new IllegalStateException("Unexpected internal: " + line);
            }
        }
        return index;
    }

    public long part1(List<String> input) {
        List<Line> lines = translate(input);
        final var root = new DirectoryNode("/");
        parseTree(lines, 1, root);
        Util.print(root.prettyPrint(true));
        return findDirectoriesOfSize(root, 100_000L);
    }

    public long part2(List<String> input) {
        List<Line> lines = translate(input);
        final var root = new DirectoryNode("/");
        parseTree(lines, 1, root);
        Util.print(root.prettyPrint(true));
        final long sizeInUse = root.calcSize();
        final long freeSpace = 70_000_000L - sizeInUse;
        final long requiredSpace = 30_000_000L - freeSpace;
        Util.printf("Currently using %d\nFree space: %d\nRequired space: %d\n\n", sizeInUse, freeSpace, requiredSpace);
        return findDirectoryToDelete(root, requiredSpace);
    }
}