package sort;

import java.util.Arrays;

/**
 * @author leo
 * @date 20220716 20:12:58
 */
public class QuickSort implements Sort{
    int partition(int[] arr, int lo, int hi) {
        int v = arr[lo];
        int i = lo + 1;
        int j = hi;
        while (true) {
            while (v >= arr[i]) {
                if (i == hi) break;
                i++;
            }
            while (v <= arr[j]) {
                if (j == lo) break;
                j--;
            }
            if (i >= j) break;
            if (arr[i] > arr[j]) {
                swap(arr,i,j);
            }
        }
        swap(arr,lo,j);
        return j;
    }

    public void sort(int[] arr) {
        if (arr == null) return;
        sort(arr,0,arr.length - 1);
    }

    void sort(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int j = partition(arr, lo, hi);
        sort(arr, lo,j -1 );
        sort(arr,j + 1, hi);
    }

    void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        Test.testSort(new QuickSort(),100000);
        System.out.println();
    }
}
