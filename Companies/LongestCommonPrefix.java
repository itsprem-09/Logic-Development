public class LongestCommonPrefix {

     public static String findLongestPrefix(String[] strs) {
        String start = strs[0];
        int length = start.length();

        while (length > 0) {
            boolean allMatch = true;
            String part = start.substring(0, length);

            for (int i = 1; i < strs.length; i++) {
                if (!strs[i].startsWith(part)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                return part;
            }
            length--;
        }
        return "";
    }

    public static String optmized(String[] strs){
        String prefix = strs[0];

        for (int i = 1; i < strs.length; i++) {
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }

            if (prefix.isEmpty()) {
                return "";
            }
        }

        return prefix;
    }


    public static String findLongestCommonPrefix(String[] strs){
        if (strs == null || strs.length == 0) {
            return "";
        }

        String first = strs[0];

        for (int i = 0; i < first.length(); i++) {
            char ch = first.charAt(i);

            for (int j = 1; j < strs.length; j++) {
                // if index exceeds length OR mismatch
                if (i >= strs[j].length() || strs[j].charAt(i) != ch ) {
                    return first.substring(0, i);
                }
            }
        }

        return first;
    }

    public static void main(String[] args) {
        
        String[] arr1 = {"flowers", "flow", "fly", "flight"};
        System.out.println(findLongestCommonPrefix(arr1));  // fl

        String[] arr2 = {"dog", "cat", "animal", "monkey"};
        System.out.println(findLongestCommonPrefix(arr2));  // ""
    }
}
