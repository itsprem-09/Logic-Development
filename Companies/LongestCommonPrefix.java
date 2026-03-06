public class LongestCommonPrefix {

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
