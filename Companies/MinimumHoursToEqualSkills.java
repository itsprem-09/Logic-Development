public class MinimumHoursToEqualSkills {

    public static int minHours(int[] skills){
        int minDifference = Integer.MAX_VALUE;

        for (int i = 0; i < skills.length - 1; i++) {
            for (int j = i + 1; j < skills.length; j++) {
                int diff = Math.abs(skills[i] - skills[j]);
                minDifference = Math.min(minDifference, diff);
            }
        }

        return minDifference;
    }

    public static void main(String[] args) {
        int[] skills = {5, 3, 5, 8};
        System.out.println(minHours(skills));
    }
}
