import java.io.*;
import java.util.*;

public class KevinBaconGame {

    // 图的邻接表表示，每个演员对应一组与其合作过的演员
    private static Map<String, Set<String>> actorGraph = new HashMap<>();
    // 保存每个演员的 Bacon 数字
    private static Map<String, Integer> baconNumbers = new HashMap<>();
    // 保存每个演员到 Kevin Bacon 的最短路径
    private static Map<String, List<String>> actorPaths = new HashMap<>();
    private static final String KEVIN_BACON = "Bacon, Kevin";

    public static void main(String[] args) {
        // 读取电影数据库文件
        String movieDatabaseFile = "IMDB_Input.txt";

        // 构建演员关系图
        readMovieDatabase(movieDatabaseFile);

        // 计算每个演员的 Bacon 数字
        calculateKevinBaconNumbers();

        // 打印 Bacon 数字的分布情况
        printBaconNumberDistribution();

        // 处理用户输入
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter an actor name (or 'E' to exit):");
            String actor = scanner.nextLine();
            if (actor.equals("E")) {
                break;
            }
            printActorBaconNumber(actor);
        }
    }

    // 读取电影数据库并构建图
    private static void readMovieDatabase(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("/");
                String movie = parts[0].trim();
                for (int i = 1; i < parts.length; i++) {
                    String actor = parts[i].trim();
                    if (!actorGraph.containsKey(actor)) {
                        actorGraph.put(actor, new HashSet<>());
                    }
                    // 为每个演员创建与其他演员的边
                    for (int j = i + 1; j < parts.length; j++) {
                        String coActor = parts[j].trim();
                        actorGraph.get(actor).add(coActor);
                        if (!actorGraph.containsKey(coActor)) {
                            actorGraph.put(coActor, new HashSet<>());
                        }
                        actorGraph.get(coActor).add(actor);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 计算所有演员的 Bacon 数字，使用 BFS 算法
    private static void calculateKevinBaconNumbers() {
        // BFS 初始化
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();

        // Kevin Bacon 的 Bacon 数字是 0
        queue.add(KEVIN_BACON);
        visited.add(KEVIN_BACON);
        baconNumbers.put(KEVIN_BACON, 0);
        predecessors.put(KEVIN_BACON, null);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentBaconNumber = baconNumbers.get(current);

            for (String neighbor : actorGraph.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    baconNumbers.put(neighbor, currentBaconNumber + 1);
                    predecessors.put(neighbor, current);
                }
            }
        }

        // 构建每个演员的路径
        for (String actor : visited) {
            List<String> path = new ArrayList<>();
            String current = actor;
            while (current != null && !current.equals(KEVIN_BACON)) {
                path.add(current);
                current = predecessors.get(current);
            }
            Collections.reverse(path);
            actorPaths.put(actor, path);
        }
    }

    // 打印 Bacon 数字的频率分布
    private static void printBaconNumberDistribution() {
        Map<Integer, Integer> distribution = new HashMap<>();
        for (int baconNumber : baconNumbers.values()) {
            distribution.put(baconNumber, distribution.getOrDefault(baconNumber, 0) + 1);
        }
        // 计算无法到达 Kevin Bacon 的演员
        int infinityCount = baconNumbers.size() - distribution.values().stream().mapToInt(Integer::intValue).sum();
        distribution.put(Integer.MAX_VALUE, infinityCount);

        // 输出频率分布
        System.out.println("Bacon number    Frequency");
        System.out.println("-------------------------");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("%12d        %d\n", i, distribution.getOrDefault(i, 0));
        }
        System.out.printf("   infinity        %d\n", distribution.getOrDefault(Integer.MAX_VALUE, 0));
    }

    // 输出某个演员的 Bacon 数字和最短路径
    private static void printActorBaconNumber(String actor) {
        if (!baconNumbers.containsKey(actor)) {
            System.out.println(actor + " has an infinity Bacon number.");
            return;
        }
        System.out.println(actor + " has a Bacon number of " + baconNumbers.get(actor));
        List<String> path = actorPaths.get(actor);
        for (int i = 0; i < path.size() - 1; i++) {
            String movie = findMovieBetween(path.get(i), path.get(i + 1));
            System.out.println(path.get(i) + " was in \"" + movie + "\" with " + path.get(i + 1));
        }
    }

    // 找到两个演员之间的电影
    private static String findMovieBetween(String actor1, String actor2) {
        for (String movie : actorGraph.keySet()) {
            Set<String> actors = actorGraph.get(movie);
            if (actors.contains(actor1) && actors.contains(actor2)) {
                return movie;
            }
        }
        return "Unknown Movie";
    }
}
