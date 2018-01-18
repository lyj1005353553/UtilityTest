package com.github.lazy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.lazylibrary.util.DateUtil;
import com.github.lazylibrary.util.RandomUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tv_content, tv_random, tv_random2;

    String arrays[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_random = (TextView) findViewById(R.id.tv_random);
        tv_random2 = (TextView) findViewById(R.id.tv_random2);

        int day = DateUtil.getDay(new Date());

        tv_content.setText("今天是：" + day);
        tv_random.setText("生成的随机数:" + RandomUtils.getRandomStr(arrays, 3) + "");

        tv_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_random.setText("生成的随机数:" + RandomUtils.getRandomStr(arrays, 3) + "");
            }
        });

        long startTime = System.currentTimeMillis();

        //执行方法
        int[] reult1 = randomCommon(0, 100000000, 10);
        for (int i : reult1) {
            System.out.println(i);
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("方案1===============执行时间：" + excTime + "ms");

        //方案三
        HashSet<Integer> set = new HashSet<Integer>();
        randomSet(0, 100000000, 10, set);
        for (int j : set) {
            System.out.println(j);
        }

        long endTime3 = System.currentTimeMillis();
        float excTime3 = (float) (endTime3 - startTime) / 1000;
        System.out.println("方案3===============执行时间：" + excTime3 + "ms");

        //方案2
        int[] reult2 = randomArray(0, 10000000, 10);
        for (int i : reult2) {
            System.out.println(i);
        }
        long endTime2 = System.currentTimeMillis();
        float excTime2 = (float) (endTime2 - startTime) / 1000;
        System.out.println("方案2===============执行时间：" + excTime2 + "ms");


        tv_random2.setText("一亿的随机数产生10个考虑执行顺序：\n\t方案1耗时："
                + excTime + "s" + "\n\t方案2耗时："
                + excTime2 + "s" + "\n\t方案3耗时："
                + excTime3 + "s");
    }


    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    /**
     * 随机指定范围内N个不重复的数
     * 利用HashSet的特征，只能存放不同的值
     *
     * @param min              指定范围最小值
     * @param max              指定范围最大值
     * @param n                随机数个数
     * @param HashSet<Integer> set 随机数结果集
     */
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }

    /**
     * ★这是效率最慢的一个
     * 随机指定范围内N个不重复的数
     * 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
     * 然后从len-2里随机产生下一个随机数，如此类推
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n   随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }
}
