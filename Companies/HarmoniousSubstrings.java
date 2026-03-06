import java.util.ArrayList;
import java.util.List;

public class HarmoniousSubstrings {

    public static List<String> findHarmonious(String s){
        List<String> ans = new ArrayList<>();

        int n = s.length();

        for (int start = 0; start < n; start++) {
            
            int freq[] = new int[26];

            for (int end = start; end < n; end++) {
                char ch = s.charAt(end);
                freq[ch - 'A']++;

                int max = 0;
                int min = Integer.MAX_VALUE;

                for (int i = 0; i < freq.length; i++) {
                    if (freq[i] > 0) {
                        max = Math.max(max, freq[i]);
                        min = Math.min(min, freq[i]);
                    }
                }

                if (max - min <= 1) {
                    ans.add(s.substring(start, end + 1));
                }
            }
        }
        
        ans.sort((a, b) -> Integer.compare(a.length(), b.length()));
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(findHarmonious("ABA"));
    }
}
