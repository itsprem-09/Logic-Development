package Gemini;

import java.util.Arrays;

public class ShoppingCartDiscount {

    public static void calculateDiscount(int[] prices){
        int[] ans = new int[prices.length];

        for (int i = 0; i < prices.length - 1; i++) {
            int min = prices[i];
            for (int j = i + 1; j < prices.length; j++) {
                if (prices[j] < min) {
                    min = prices[j];
                }
            }
            if (min == prices[i]) {
                ans[i] = prices[i];
            }
            else{
                ans[i] = prices[i] - min;
            }
        }

        ans[ans.length - 1] = prices[prices.length - 1];

        System.out.println(Arrays.toString(ans));
    }

    public static void main(String[] args) {
        int[] prices = {8, 4, 6, 2, 3};

        calculateDiscount(prices);
    }
}
