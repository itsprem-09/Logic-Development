import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MaxPrice {

    public static void sortArray(int[] prices, String[] categories){
        for (int i = 0; i < prices.length - 1; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                if (prices[i] < prices[j]) {
                    int t = prices[i];
                    prices[i] = prices[j];
                    prices[j] = t;

                    String t1 = categories[i];
                    categories[i] = categories[j];
                    categories[j] = t1;
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] arr = {"600:Electronics", "350:Sports", "250:Beauty", "150:Books"};

        int n = 3;
        int k = 1;

        int[] prices = new int[arr.length];
        String[] categories = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            String[] spilted = arr[i].split(":");

            prices[i] = Integer.parseInt(spilted[0]);
            categories[i] = spilted[1]; 
        }

        sortArray(prices, categories);

        int ans = 0;

        Map<String, Integer> categoryCount = new HashMap<>();

        for (int i = 0; i < prices.length; i++) {
            if (n <= 0) {
                break;
            }

            // Get the current count of the category
            int count = categoryCount.getOrDefault(categories[i], 0);

            // If the count is less than k, add the price to the answer and update the count
            if (count < k) {
                ans += prices[i];
                categoryCount.put(categories[i], count + 1);
                n--;
            }   
        }

        System.out.println(ans);
    }
}
