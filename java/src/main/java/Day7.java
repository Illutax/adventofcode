import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {

    public static void main(String[] args) {
//        q1(parseRules());
        q2(parseRules());
    }

    private static void q1(Map<String, Map<String, Integer>> rules) {
        var sum = new HashSet<String>();
        var prevLevel = Collections.singleton("shiny gold");
        while(!prevLevel.isEmpty()) {
            var thisLevel = new HashSet<String>();
            prevLevel.forEach(key -> {
                var keys = new HashSet<String>();
                rules.keySet().forEach(ruleKey -> {
                    final Map<String, Integer> r = rules.get(ruleKey);
                    if (r.containsKey(key)) keys.add(ruleKey);
                });
                thisLevel.addAll(keys);
            });
            prevLevel = thisLevel;
            sum.addAll(thisLevel);
        }
        int result = sum.size();
        assert result == 224 : "correct";
        System.out.println(result);
    }

    private static void q2(Map<String, Map<String, Integer>> rules) {
        var sum = new HashSet<String>();
        var level = Collections.singleton("shiny gold");
        while(!level.isEmpty()) {
            var amount = scanRulesForLevel(rules, level);
            sum.addAll(level);
        }
        int result = sum.size();
        assert result == 224 : "correct";
        System.out.println(result);
    }

    private static Map<String, Integer> scanRulesForLevel(Map<String, Map<String, Integer>> sourceRules, Set<String> prevLevel) {
        var thisLevel = new HashSet<String>();
        var rulesOfInterest = new HashMap<String, Integer>();
        for (String key : prevLevel) {
            var sourceKeys = new HashSet<String>();
            for (String ruleKey : sourceRules.keySet()) {
                final Map<String, Integer> r = sourceRules.get(ruleKey);
                if (r.containsKey(key)) {
                    sourceKeys.add(ruleKey);
                    rulesOfInterest.put(key, r.get(key));
                }
            }
            thisLevel.addAll(sourceKeys);
        }
        prevLevel.clear();
        prevLevel.addAll(thisLevel);
        return rulesOfInterest;
    }

    private static Map<String, Map<String, Integer>> parseRules() {
        Map<String, Map<String, Integer>> m = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("input7.txt")))) {
            for (String line; (line = br.readLine()) != null; ) {
                parseAndPutRule(m, line);
            }
        } catch (IOException ignored) {
        }
        return m;
    }

    private static void parseAndPutRule(Map<String, Map<String, Integer>> m, String line) {
        //                         pale turquoise bags contain    3 muted cyan        bags,     5 striped teal      bags.
        assert line.matches("^([a-z]+[ ]){2}bags contain[ ]((\\d[ ]([a-z]+[ ]){2}bags|1[ ]([a-z]+[ ]){2}bag),[ ])*((\\d[ ]([a-z]+[ ]){2}|no other[ ])bags|1[ ]([a-z]+[ ]){2}bag).$");

        final var r = line.split(" bags contain ");
        final var source = r[0];
        if (m.containsKey(source)) throw new IllegalArgumentException("Duplicate key " + source);
        m.put(source, parseRule(r[1].split(", ")));
    }

    private static HashMap<String, Integer> parseRule(String[] r) {
        final var rules = new HashMap<String, Integer>();
        for (var t : r) {
            if (t.equals("no other bags."))
                continue;
            final var amount = t.split(" ")[0];
            final String key = Arrays.stream(t.split(" "))
                    .filter(s -> !s.matches("\\d|bag(s)?(.)?"))
                    .collect(Collectors.joining(" "));
            if (rules.containsKey(key)) throw new IllegalArgumentException("Duplicate key " + key);
            rules.put(key, Integer.parseInt(amount));
        }
        return rules;
    }
}
