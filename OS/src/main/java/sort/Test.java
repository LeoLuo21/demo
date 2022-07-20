package sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author leo
 * @date 20220718 16:47:44
 */
public class Test {
    public static void testSort(Sort sort,int... numbers) {
        int cycle;
        if (numbers != null && numbers.length > 0) {
            cycle = numbers[0];
        }else {
            cycle = 1000;
        }
        int[] arr = new int[cycle];
        Random random = new Random();
        for (int i = 0; i < cycle; i++) {
            arr[i] = random.nextInt(cycle);
        }
        sort.sort(arr);
        System.out.println("isSorted(arr) = " + isSorted(arr));
    }
    
    public static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i+1]) return false; 
        }
        return true;
    }
}
