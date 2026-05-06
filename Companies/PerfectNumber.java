public class PerfectNumber {

    // this works for O(n)
    public boolean isPerfect(int num){
        if (num <= 1) {
            return false;
        }

        int sum = 0;

        for (int i = 1; i <= num/2; i++) {
            if (num % i == 0) {
                sum += i;
            }
        }

        return sum == num;
    }

    // this works for O(sqrt(n))
    public boolean isPerfectOptimized(int num){
        if (num <= 1) {
            return false;
        }

        int sum = 1;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                sum += i;

                if (i != num / i) {
                    sum += num / i;
                }
            }
        }
        return sum == num;
    }


}
