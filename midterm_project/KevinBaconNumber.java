import java.io.*;
import java.util.*;

public class KevinBaconNumber {
    private static final String INPUT_FILE = "IMDB_Input.txt"; // 电影数据库文件名
    private Map<String, List<String>> graph = new HashMap<>();
    private Map<String, Integer> baconNumbers = new HashMap<>();
    private Map<String, List<String>> paths = new HashMap<>();
    private int[] distribution = new int[11]; // 凯文·贝肯数分布，从0到10

    public static void main(String[] args) throws IOException {
        new KevinBaconNumber().process();
    }

    private void process() throws IOException {
        readGraph(INPUT_FILE);
        bfs("Bacon, Kevin");
        printDistribution();
        interactiveQuery();
    }

    private void readGraph(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("/");
                String movie = parts[0];
                List<String> actors = new ArrayList<>();
                for (int i = 1; i < parts.length; i++) {
                    String actor = parts[i].trim();
                    actors.add(actor);
                    graph.computeIfAbsent(actor, k -> new ArrayList<>()).add(movie);
                    for (String other : actors) {
                        if (!actor.equals(other)) {
                            graph.computeIfAbsent(actor, k -> new ArrayList<>()).add(other);
                            graph.computeIfAbsent(other, k -> new ArrayList<>()).add(actor);
                        }
                    }
                }
            }
        }
    }

    private void bfs(String root) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(root);
        baconNumbers.put(root, 0);
        visited.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String actor = queue.poll();
                List<String> neighbors = graph.get(actor);
                if (neighbors != null) {
                    for (String neighbor : neighbors) {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                            baconNumbers.put(neighbor, depth + 1);
                            paths.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(actor);
                        }
                    }
                }
            }
            depth++;
        }

        for (int i = 0; i < 11; i++) {
            distribution[i] = 0;
        }
        for (int number : baconNumbers.values()) {
            if (number < 11) {
                distribution[number]++;
            } else {
                distribution[10]++;
            }
        }
    }

    private void printDistribution() {
        System.out.println("Bacon number\tFrequency");
        System.out.println("------------------------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + "\t\t" + distribution[i]);
        }
        System.out.println("infinity\t" + distribution[10]);
    }

    private void interactiveQuery() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter an actor's name (or 'E' to exit):");
        String input;
        while ((input = br.readLine()) != null && !input.equals("E")) {
            if (baconNumbers.containsKey(input)) {
                int number = baconNumbers.get(input);
                System.out.println(input + " has a Bacon number of " + number);
                printPath(input);
            } else {
                System.out.println("Actor not found.");
            }
            System.out.println("Enter an actor's name (or 'E' to exit):");
        }
    }

    private void printPath(String actor) {
        List<String> path = new LinkedList<>(paths.get(actor));
        Collections.reverse(path);
        path.add(0, "Bacon, Kevin");
        System.out.print(actor + " was in \"" + graph.get(actor).get(0) + "\" with ");
        for (int i = 1; i < path.size(); i++) {
            String next = path.get(i);
            if (i < path.size() - 1) {
                System.out.print(next + ", who was in \"" + graph.get(path.get(i - 1)).get(0) + "\" with ");
            } else {
                System.out.print(next);
            }
        }
        System.out.println();
    }
}