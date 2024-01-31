package com.zou.algorithm.SlidingWindow;


import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author leonard
 * @Description 滑动窗口算法
 * https://www.cnblogs.com/huansky/p/13488234.html
 *
 * https://leetcode-cn.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/
 *      * 输入：s = "abciiidef", k = 3
 *      * 输出：3
 *      * 解释：子字符串 "iii" 包含 3 个元音字母。
 * @date 2021-12-07 10:15
 */
public class MainClass {
    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("eyq");
        System.out.println("result:"+maxVowels(sb.toString(),3945));

        System.out.println("costTime: "+(System.currentTimeMillis()-t)+" ms");
    }


    /**
     *
     * @param s
     * @param k 固定窗口大小
     * @return
     * result:823
     * costTime: 23 ms
     */
    public static int maxVowels(String s, int k) {
        int right =0;
        int sum = 0;
        int max = 0;
        while (right < s.length()) {
            sum += isVowel(s.charAt(right)) ;
            right++;

            //如果符合要求，说明窗口构造完成
            if (right >=k) {
                max = Math.max(max, sum);

                //窗口移动
                sum -= isVowel(s.charAt(right-k));
            }
        }
        return max;
    }


    /**
     *
     * @param s
     * @param k
     * @return
     *  result:823
     *  costTime: 9244 ms
     * tip:此写法会超时，需改写
     */
    public static int maxVowels2(String s, int k) {
        if (StringUtils.isEmpty(s)) return 0;
        if (s.length() <= 3) return countVowels(s);

        int count = 0;
        int left = 0;
        int right = k-1;

        while (right < s.length()) {
            int temp = countVowels(s.substring(left,right+1));
            if(count<temp){
                count = temp;
            }
            left++;
            right++;
        }
        return count;
    }

    private static int countVowels(String str){
        int count = 0;
        List<Character> characters = Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U');
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (characters.contains(chars[i])) {//isVowel(chars[i])==1 characters.contains(chars[i])
                count++;
            }
        }
        return count;
    }

    public static int isVowel(char ch) {
        return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' ? 1 : 0;
    }
}
