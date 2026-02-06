// Given a time in the format of hh:mm (12-hour format)
// 0 < hh < 12, 0 <= mm < 60. The task is to convert it into words as shown:
// Input: h = 5, m = 0
// Output: five o' clock
// Input: h = 6, m = 24
// Output: twenty four minutes past six
// 5:00 → five o’ clock
// 5:01 → one minute past five
// 5:10 → ten minutes past five
// 5:15 → quarter past five
// 5:30 → half past five
// 5:40 → twenty minutes to six
// 5:45 → quarter to six
// 5:47 → thirteen minutes to six
// 5:28 → twenty eight minutes past five

public class TimeInWords {

    static String[] words = {
        "", "one", "two", "three", "four", "five", "six",
        "seven", "eight", "nine", "ten", "eleven", "twelve",
        "thirteen", "fourteen", "fifteen", "sixteen",
        "seventeen", "eighteen", "nineteen", "twenty",
        "twenty one", "twenty two", "twenty three",
        "twenty four", "twenty five", "twenty six",
        "twenty seven", "twenty eight", "twenty nine"
    };

    public static String timeInWords(int h, int m) {

        if (m == 0) {
            return words[h] + " o' clock";
        }

        if (m == 15) {
            return "quarter past " + words[h];
        }

        if (m == 30) {
            return "half past " + words[h];
        }

        if (m == 45) {
            return "quarter to " + words[(h % 12) + 1];
        }

        if (m < 30) {
            return words[m] + (m == 1 ? " minute past " : " minutes past ") + words[h];
        }

        return words[60 - m] + " minutes to " + words[(h % 12) + 1];
    }

    public static void main(String[] args) {
        System.out.println(timeInWords(5, 0));
        System.out.println(timeInWords(5, 1));
        System.out.println(timeInWords(5, 10));
        System.out.println(timeInWords(5, 15));
        System.out.println(timeInWords(5, 30));
        System.out.println(timeInWords(5, 40));
        System.out.println(timeInWords(5, 45));
        System.out.println(timeInWords(5, 47));
        System.out.println(timeInWords(5, 28));
    }
}
