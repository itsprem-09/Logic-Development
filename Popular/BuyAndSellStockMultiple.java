package Popular;

public class BuyAndSellStockMultiple {

    public static int maxProfit(int[] prices){
        int max = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                max += prices[i] - prices[i - 1];
            }
        }

        return max;
    }

    public static void main(String[] args) {
        int prices[] = {100, 180, 260, 310, 40, 535, 695};

        System.out.println(maxProfit(prices));
    }
}
