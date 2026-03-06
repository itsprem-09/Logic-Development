import java.util.HashMap;

public class CountDuplicates {

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
