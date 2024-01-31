package com.zou.utils;

import com.alibaba.fastjson.JSON;
import com.zou.dataobject.ListNode;
import com.zou.pojo.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author leonard
 * @date 2022/6/10
 * @Description 各种杂七杂八工具类
 */
public class GenericUtil {
    public static void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    public static void printListNodes(ListNode<Integer> head) {
        ListNode<Integer> cur = head;
        while (cur.getNext() != null) {
            System.out.print(cur.getVal() + "->");
            cur = cur.getNext();
        }
        System.out.println(cur.getVal());
    }


    //冒泡排序
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {//n-1趟
            for (int j = i; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr[j], arr[j + 1]);
                }
            }
        }
    }

    public static void bubbleSort(int[] arr, String desc) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {//n-1趟
            for (int j = i; j < n - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    swap(arr[j], arr[j + 1]);
                }
            }
        }
    }

    private static void swap(int a, int b) {
        int t = a;
        a = b;
        b = t;
    }

    /**
     * @param fileJson     /templates/Person.json
     * @param responseType Person.class
     * @param <T>
     * @return
     */
    public static <T> T jsonFileToObject(String fileJson, Class<T> responseType) {
        StringBuilder sb = new StringBuilder();
        InputStream resourceAsStream = null;
        BufferedReader reader = null;
        try {
            resourceAsStream = GenericUtil.class.getResourceAsStream(fileJson);
            reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }
        return JSON.parseObject(sb.toString(), responseType);
    }

    public static void main(String[] args) {
        Person person = GenericUtil.jsonFileToObject("/templates/Person.json", Person.class);
        System.out.println(person.getName());
    }


}
