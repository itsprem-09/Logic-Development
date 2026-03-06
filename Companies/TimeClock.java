class Time {
    int hour;
    int min;
    int sec;

    public Time(int hour, int min, int sec) {
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public void addSeconds(int sec){
        this.sec += sec;
        while (this.sec >= 60) {
            this.sec -= 60;
            this.min += 1;
        }
        while (this.min >= 60) {
            this.min -= 60;
            this.hour += 1;
        }

        System.out.println("Time After Adding "+sec+" is : ");
        printTime();
    }

    public void subtractTime(int sec){
        this.sec -= sec;
        while (this.sec < 0) {
            this.sec += 60;
            this.min -= 1;
        }
        while (this.min < 0) {
            this.min += 60;
            this.hour -= 1;
        }

        if (this.hour < 0) {
            this.hour = 0;
            this.min = 0;
            this.sec = 0;
        }

        System.out.println("Time After Substracting "+sec+" is : ");
        printTime();
    }

    public void printTime(){
        System.out.println(this.hour+":"+this.min+":"+this.sec);
    }
    
}

public class TimeClock {
    public static void main(String[] args) {
        Time t = new Time(2, 30, 45);
        t.addSeconds(50);
        t.subtractTime(30);
    }
}
