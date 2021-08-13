package com.orilx.utils;

import java.util.Random;

/**
 * 按需求生成随机数
 */
public class RollUtil {
    private RollUtil(){}

    /**
     * 返回包括传入参数以内的从0开始的随机整数
     * @param maxNum 随机数上界
     * @return 随机数
     */
    public static int roll(int maxNum){
        return new Random().nextInt(maxNum + 1);
    }

    /**
     * 返回范围内随机数
     * @param minNum 最小值
     * @param maxNum 最大值
     * @return 随机数
     */
    public static int roll(int minNum, int maxNum){
        if(minNum == maxNum){
            return maxNum;
        }
        return new Random().nextInt(maxNum - minNum) + minNum;
    }
}
