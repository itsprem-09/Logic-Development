public class DecimalToOctal {

    public static int decimalToOctal(int n){
        String octValues = "01234567";
        StringBuilder octal = new StringBuilder();

        while (n > 0) {
            int rem = n % 8;
            octal.append(octValues.charAt(rem));
            n /= 8;
        }

        return Integer.parseInt(octal.reverse().toString());
    }

    public static int octalToDecimal(int n){
        int decimal = 0;
        int power = 0;

        while (n > 0) {
            int rem = n % 10;
            decimal += rem * Math.pow(8, power);
            power++;
            n /= 10;
        }

        return decimal;
    }

    public static void main(String[] args) {
        
    }
}
