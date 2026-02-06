public class NumberOfWayToSelectBuilding {

    public static int numOfWays(String str){
        int n = str.length();
        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (str.charAt(i) == str.charAt(j)) {
                    continue;
                }

                for (int k = j + 1; k < n; k++) {
                    if (str.charAt(j) != str.charAt(k)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        System.out.println(numOfWays("001101")); // 6

    }
}
