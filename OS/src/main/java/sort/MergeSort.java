package sort;

import java.util.Arrays;

/**
 * @author leo
 * @date 20220718 17:14:24
 */
public class MergeSort implements Sort{

    @Override
    public void sort(int[] arr) {
        sort(arr,0,arr.length -1);
    }

    void sort(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int mid = lo + (hi-lo) / 2;
        sort(arr,lo,mid);
        sort(arr,mid+1,hi);
        merge(arr,lo,mid,hi);
    }

    void merge(int[] arr, int lo, int mid, int hi) {
        int[] a = new int[hi-lo+1];
        int i = lo;
        int j = mid + 1;
        int k = 0;
        while (k < a.length) {
            if (i > mid) {
                a[k++] = arr[j++];
            }else if(j > hi) {
                a[k++] = arr[i++];
            }else if(arr[i] < arr[j]) {
                a[k++] = arr[i++];
            }else {
                a[k++] = arr[j++];
            }
        }
        k = 0;
        for (int l = lo; l <= hi; l++) {
            arr[l] = a[k++];
        }
    }

    public static void main(String[] args) {
        Test.testSort(new MergeSort(),100000000);
    }

}
