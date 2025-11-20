class ReverseStringSpecialCharacters{

    public static String swap(String str, int i, int j) {
        char[] charArray = str.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    public static String reverseString(String str){
        int start = 0;
        int end = str.length() - 1;
        while (start <= end) {
            if (Character.isAlphabetic(str.charAt(start)) && Character.isAlphabetic(str.charAt(end))) {
                str = swap(str, start, end);
                start++;
                end--;
            } else if (!Character.isAlphabetic(str.charAt(start))) {
                start++;
            } else if (!Character.isAlphabetic(str.charAt(end))) {
                end--;
            }
        }
        return str;
    }

    public static void main(String[] args) {
        String str = "\"Ab,c,de!$\"";
        System.out.println(reverseString(str));
    }
}