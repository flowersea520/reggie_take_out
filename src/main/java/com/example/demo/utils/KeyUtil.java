package com.example.demo.utils;

import java.util.Random;

// 简单的一个注释，linux课程
public class KeyUtil {
    public static String keyUtils() {
        String str="0123456789";
        StringBuilder st=new StringBuilder(4);
        for(int i=0;i<4;i++){
            char ch=str.charAt(new Random().nextInt(str.length()));
            st.append(ch);
        }
        String findkey=st.toString().toLowerCase();
        return findkey;
    }
}