import java.util.*;

public class FindPlayers {

    public static List<List<Integer>> findWinners(int[][] matches) {

        Map<Integer, Integer> lossCount = new HashMap<>();

        // Count losses
        for (int[] match : matches) {
            int winner = match[0];
            int loser = match[1];

            // Ensure winner is tracked (0 losses initially)
            lossCount.putIfAbsent(winner, 0);

            // Increment loss count for loser
            lossCount.put(loser, lossCount.getOrDefault(loser, 0) + 1);
        }

        List<Integer> noLoss = new ArrayList<>();
        List<Integer> oneLoss = new ArrayList<>();

        // Categorize players
        for (Map.Entry<Integer, Integer> entry : lossCount.entrySet()) {
            int player = entry.getKey();
            int losses = entry.getValue();

            if (losses == 0) {
                noLoss.add(player);
            } else if (losses == 1) {
                oneLoss.add(player);
            }
        }

        // Sort results
        Collections.sort(noLoss);
        Collections.sort(oneLoss);

        List<List<Integer>> result = new ArrayList<>();
        result.add(noLoss);
        result.add(oneLoss);

        return result;
    }

    public static void main(String[] args) {
        int[][] matches = {
            {1,3},{2,3},{3,6},{5,6},{5,7},
            {4,5},{4,8},{4,9},{10,4},{10,9}
        };

        System.out.println(findWinners(matches));
    }
}
