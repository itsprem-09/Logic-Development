// 101.110 (binary)
// = (1×2² + 0×2¹ + 1×2⁰) + (1×2⁻¹ + 1×2⁻² + 0×2⁻³)
// = (4 + 0 + 1) + (0.5 + 0.25 + 0)
// = 5.75

public class BinaryToInteger {
    public static double binaryToDecimal(String bin) {
        double result = 0.0;

        // Split integer and fractional parts
        String[] parts = bin.split("\\.");

        // Integer part
        String intPart = parts[0];
        int power = 0;
        for (int i = intPart.length() - 1; i >= 0; i--) {
            if (intPart.charAt(i) == '1') {
                result += Math.pow(2, power);
            }
            power++;
        }

        // Fractional part (if exists)
        if (parts.length == 2) {
            String fracPart = parts[1];
            double fracPower = -1;

            for (int i = 0; i < fracPart.length(); i++) {
                if (fracPart.charAt(i) == '1') {
                    result += Math.pow(2, fracPower);
                }
                fracPower--;
            }
        }

        return result;
    }
}
