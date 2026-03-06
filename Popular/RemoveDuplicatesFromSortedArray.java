package Popular;

import java.util.HashSet;

public class RemoveDuplicatesFromSortedArray {

    public static int removeDuplicates(int[] arr){
        HashSet<Integer> set = new HashSet<>();

        // to maintain the new size of an array
        int index = 0;

        for (int i = 0; i < arr.length; i++) {
            if (!set.contains(arr[i])) {
                set.add(arr[i]);
                arr[index] = arr[i];
                index++;
            }  
        }

        return index;
    }

    

    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 3, 4, 4, 4, 5, 5};
        
        int newSize = removeDuplicates(arr);

        for (int i = 0; i < newSize; i++) {
            System.out.print(arr[i]+" ");
        }
    }
}
