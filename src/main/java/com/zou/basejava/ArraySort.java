package com.zou.basejava;

//https://pdai.tech/md/algorithm/alg-sort-overview.html
public class ArraySort {
    public static void main(String[] args) {
        int[] arr = new int[]{8, 3, 1, 2, 7, 10, 5, 6, 4};
        //bubbleSort(arr);
        //selectSort(arr);
        //insertSort(arr);
        //quickSort(arr, 0, arr.length - 1);
        //bucketSort(arr,15);
        mergesort(arr, 0, arr.length - 1);
        printArray(arr);
    }

    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }

    /**
     * 把n个待排序的元素看成为一个有序表和一个无序表。开始时有序表中只包含1个元素，无序表中包含有n-1个元素，
     * 排序过程中每次从无序表中取出第一个元素，将它插入到有序表中的适当位置，使之成为新的有序表，重复n-1次可完成排序过程
     *
     * @param arr
     */
    public static void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (arr[j] > temp) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            arr[j + 1] = temp;
        }
    }

    /**
     * 选择一个基准数，通过一趟排序将要排序的数据分割成独立的两部分；其中一部分的所有数据都比另外一部分的所有数据都要小。
     * 然后，再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列
     *
     * @param arr
     * @param l
     * @param r
     */
    public static void quickSort(int[] arr, int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            int temp = arr[l];

            while (i < j) {
                while (i < j && arr[j] >= temp) {
                    j--;
                }
                if (i < j) {
                    arr[i] = arr[j];
                    i++;
                }
                while (i < j && arr[i] <= temp) {
                    i++;
                }
                if (i < j) {
                    arr[j] = arr[i];
                    j--;
                }
            }
            arr[i] = temp;
            quickSort(arr, l, i - 1);
            quickSort(arr, i + 1, r);
        }

    }

    /**
     * 桶排序：新建一个桶数组，index为arr的value,value为arr相同value的次数
     * 然后从小到大遍历桶就行
     *
     * @param a
     * @param max
     */
    public static void bucketSort(int[] a, int max) {
        int[] buckets;

        if (a == null || max < 1)
            return;

        buckets = new int[max];

        for (int i = 0; i < a.length; i++)
            buckets[a[i]]++;

        for (int i = 0, j = 0; i < max; i++) {
            while ((buckets[i]--) > 0) {
                a[j++] = i;
            }
        }
        buckets = null;
    }

    //堆排序：即二叉树排序的中序遍历

    //归并排序
    public static void mergesort(int[] arr, int left, int right) {
        if (left >= right)
            return;
        int center = (left + right) / 2;
        mergesort(arr, left, center);
        mergesort(arr, center + 1, right);
        merge(arr, left, center, right);
    }

    private static void merge(int[] arr, int left, int center, int right) {
        //临时数组
        int[] tmpArr = new int[arr.length];
        //右边第一个元素
        int mid = center + 1;
        //临时数组的第一个下标
        int i = left;
        // 缓存左数组第一个元素的索引
        int tmp = left;

        while (left <= center && mid <= right) {
            //两个数组取最小的放入临时数组
            if (arr[left] <= arr[mid]) {
                tmpArr[i++] = arr[left++];
            } else {
                tmpArr[i++] = arr[mid++];
            }
        }

        while (left <= center) {
            tmpArr[i++] = arr[left++];
        }

        while (mid <= right) {
            tmpArr[i++] = arr[mid++];
        }

        //将临时数组内容拷贝会原来的数组
        while (tmp <= right) {
            arr[tmp] = tmpArr[tmp];
            tmp++;
        }
    }


    private static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        System.out.println("==========================");
    }
}
