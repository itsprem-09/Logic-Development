public class DecimalToHexaDecimal {

    public static int decimalToHexaDecimal(int n){
        String hexValues = "0123456789ABCDEF";
        StringBuilder hexaDecimal = new StringBuilder();

        while (n > 0) {
            int rem = n % 16;
            hexaDecimal.append(hexValues.charAt(rem));
            n /= 16;
        }

        return Integer.parseInt(hexaDecimal.reverse().toString());
    }

    public static int hexaDecimalToDecimal(int n){
        int decimal = 0;
        int power = 0;

        while (n > 0) {
            int rem = n % 10;
            decimal += rem * Math.pow(16, power);
            power++;
            n /= 10;
        }

        return decimal;
    }

    public static void main(String[] args) {
        
    }
}
