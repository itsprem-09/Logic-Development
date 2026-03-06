package Popular;

public class NinjaTraining {

    public static int maxPoints(int[][] points){
        int running = points[0][0];
        int fighting = points[0][1];
        int learning = points[0][2];

        for (int i = 0; i < points.length; i++) {
            int newRunning = points[i][0] + Math.max(fighting, learning);
            int newFighting = points[i][1] + Math.max(running, learning);
            int newLearning = points[i][2] + Math.max(running, fighting);

            running = newRunning;
            fighting = newFighting;
            learning = newLearning;
        }

        return Math.max(running, Math.max(fighting, learning));
    }

    public static void main(String[] args) {
        int[][] points = {
            {1,2,5},
            {3,1,1},
            {3,3,3}
        };

        System.out.println(maxPoints(points));
    }
}
