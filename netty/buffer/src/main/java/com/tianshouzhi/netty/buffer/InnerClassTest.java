package com.tianshouzhi.netty.buffer;

/**
 * Created by tianshouzhi on 2018/4/8.
 */
public class InnerClassTest {
    public static void main(String[] args) {
        InnerClassTest innerClassTest = new InnerClassTest(){};
        InnerClassTest innerClassTest1 = new InnerClassTest(){};
        System.out.println(innerClassTest.getClass());
    }

}
