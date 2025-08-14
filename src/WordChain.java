import java.io.*;
import java.util.*;

public class WordChain {

    public static void main(String[] args) throws IOException {
        String fileName = "files/words-250.txt";
        String fileNameGraph = "files/words-250-graph.txt";
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        BufferedReader graphPath = new BufferedReader(new InputStreamReader(new FileInputStream(fileNameGraph)));
        ArrayList<String> words = new ArrayList<String>();
        while (true) {
            String word = graphPath.readLine();
            if (word == null) { break; }
            assert word.length() == 5;
            words.add(word.trim());
        }
        Map<String, LinkedList<String>> graph = buildGraph(words);

        while (true) {
            String line = r.readLine();
            if (line == null) {
                break;
            }
            assert line.length() == 11;
            String start = line.substring(0, 5);
            String goal = line.substring(6, 11);
            int result = bfs(graph, start, goal);
            System.out.println(result);
        }
    }

    private static Map<String, LinkedList<String>> buildGraph(ArrayList<String> words) {
        // Skapar en graf representerad av en Map där varje ord är kopplat till en länkad lista av anslutna ord
        Map<String, LinkedList<String>> graph = new HashMap<>();

        for (String word : words) {
            graph.put(word, new LinkedList<>());

            // går igenom ord
            for (String otherWord : words) {
                // kollar så det inte är samma ord och om det går att connecta orden
                if (!word.equals(otherWord) && canConnect(word, otherWord)) {
                    graph.get(word).add(otherWord);
                }
            }
        }
        return graph;
    }

    private static boolean canConnect(String word1, String word2) {

        // delar upp word1 i en lista av chars minus den första charen
        List<Character> remainingChars = new ArrayList<>();
        for (char ch : word1.substring(1).toCharArray()) {
            remainingChars.add(ch);
        }

        // kollar om word2 char är samma som de word1
        for (char ch : word2.toCharArray()) {
            if (remainingChars.contains(ch)) {
                remainingChars.remove(Character.valueOf(ch));
            }
        }

        // kollar så att alla chars från word1 har använts
        return remainingChars.isEmpty();
    }




    private static int bfs(Map<String, LinkedList<String>> graph, String start, String goal) {
        // länkadlista använder queue interface så det går att ha de som en queue
        Queue<String> queue = new LinkedList<>();
        List<String> visited = new LinkedList<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.add(start);
        visited.add(start);
        distance.put(start, 0);


        while (!queue.isEmpty()) {
            // Hämta och ta bort den första noden från kön
            String current = queue.poll();

            // Om den nuvarande noden är målet returnera avståndet
            if (current.equals(goal)) {
                return distance.get(current);
            }

            LinkedList<String> neighbors = graph.get(current);

            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    // Om grannen inte har besökts lägg till den i kön, markera som besökt och uppdatera avståndet
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        distance.put(neighbor, distance.get(current) + 1);
                    }
                }
            }
        }
        return -1;
    }
}
