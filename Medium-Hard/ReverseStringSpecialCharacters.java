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

    public static String reverseStringMethod2(String str){
        StringBuilder sb = new StringBuilder();

        // add all alphabetic characters
        for (int i = 0; i < str.length(); i++) {
            if (Character.isAlphabetic(str.charAt(i))) {
                sb.append(str.charAt(i));
            }
        }

        // reverse them
        sb.reverse();

        StringBuilder result = new StringBuilder();
        int index = 0;

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isAlphabetic(ch)) {
                result.append(sb.charAt(index));
                index++;
            }
            else{
                result.append(ch);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String str = "\"Ab,c,de!$\"";
        System.out.println(reverseString(str));
        System.out.println(reverseStringMethod2(str));
    }
}