import java.util.*;

public class DominantNumber{
    public static void main(String[] args){
        int[] arr = {1, 2, 3, 1, 1, 4, 5, 1, 1, 1};

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
        }

        int uniqueCount = map.size();

        List<Integer> ans = new ArrayList<>();

        for (int key : map.keySet()) {
            if (map.get(key) > uniqueCount) {
                ans.add(key);
            }
        }

        System.out.println(ans);
    }
}