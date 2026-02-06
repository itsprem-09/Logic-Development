import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WinnersAndLoosers {

    public List<List<Integer>> findWinnersAndOneLossers(int[][] arr){
        Map<Integer, Integer> map = new HashMap<>();

        List<Integer> noLoss = new ArrayList<>();
        List<Integer> oneLoss = new ArrayList<>();

        for (int[] match : arr) {
            int winner = match[0];
            int losser = match[1];

            map.putIfAbsent(winner, 0);

            map.put(losser, map.getOrDefault(losser, 0)+1);
        }

        for(Map.Entry<Integer, Integer> entry : map.entrySet()){
            int player = entry.getKey();
            int lossCount = entry.getValue();

            if (lossCount == 0) {
                noLoss.add(player);
            }
            else if (lossCount == 1) {
                oneLoss.add(player);
            }
        }

        List<List<Integer>> result = new ArrayList<>();
        result.add(noLoss);
        result.add(oneLoss);

        return result;

    }

    public static void main(String[] args) {
        


    }
}
