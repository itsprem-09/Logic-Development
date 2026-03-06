package Popular;

public class MaximumRepatingSubstring {

    public static int maxRepating(String sequence, String word){
        if(!sequence.contains(word)) return 0;
        int k = 1;
        String str = word+word; 
        while(sequence.contains(str)){
            k++;
            str += word;
        }
        return k;
    }

    public static void main(String[] args) {
        String sequence = "ababc";
        String word = "ab";

        System.out.println(maxRepating(sequence, word));
    }
    
}