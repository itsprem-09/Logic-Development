import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SentenceSimilarity {

    // Calculate similarity score for one sentence
    public static int calculateScore(String sentence, String secret) {

        String[] words = sentence.split(" ");
        int totalScore = 0;

        for (String word : words) {

            if (word.length() == secret.length()) {
                for (int i = 0; i < secret.length(); i++) {

                    if (Character.toLowerCase(word.charAt(i)) ==
                        Character.toLowerCase(secret.charAt(i))) {
                        totalScore++;
                    }
                }
            }
        }

        return totalScore;
    }

    public static void main(String[] args) {
        String secret = "cat";

        String[] sentences = {
                "I love my pet",
                "A car ran fast",
                "He bought a hat",
                "The cat sat on the mat"
        };

        int n = sentences.length;

        int[] scores = new int[n];

        // Calculate similarity scores
        for (int i = 0; i < n; i++) {
            scores[i] = calculateScore(sentences[i], secret);
        }

        System.out.println(Arrays.toString(scores));

        HashMap<Integer, String> map = new HashMap<>();

        for (int i = 0; i < scores.length; i++) {
            map.put(scores[i], sentences[i]);
        }

        System.out.println(map.toString());
        
        ArrayList<String> list = new ArrayList<>();

        for(int key : map.keySet()){
            list.add(map.get(key));
        }

        StringBuilder sb = new StringBuilder();

        for (int i = list.size() - 1; i >= 0; i--) {
            sb.append(list.get(i)+" ");
        }

        System.out.println(sb.toString().trim());
    }
}
