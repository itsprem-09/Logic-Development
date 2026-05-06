import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class CountDuplicates {

    public static int duplicate(int[] arr){
        Arrays.sort(arr);

        int cnt = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                cnt++;

                while (i < arr.length && arr[i] == arr[i - 1]) {
                    i++;
                }
            }
        }

        return cnt;
    }

    public static int totalDuplicates(int[] arr){
        HashSet<Integer> seen = new HashSet<>();
        HashSet<Integer> duplicates = new HashSet<>();

        for (int i = 0; i < arr.length; i++) {
            if (!seen.add(arr[i])) {
                duplicates.add(arr[i]);
            }
        }
        return duplicates.size();
    }

    public static int countDuplicate(int[] arr){
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
        }

        int cnt = 0;
        for (int key : map.keySet()) {
            if (map.get(key) > 1) {
                cnt++;
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 1, 2};

        System.out.println(countDuplicate(arr));
    }
}
