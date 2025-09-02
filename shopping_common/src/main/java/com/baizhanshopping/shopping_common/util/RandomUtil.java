package com.baizhanshopping.shopping_common.util;

import java.util.Random;

public class RandomUtil {
    public static String buildCheckCode(int length){
        String str="0123456789";
        Random random = new Random();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < length ;i++) {
            char c=str.charAt(random.nextInt(str.length()));
            sb.append(c);
        }
        return sb.toString();
    }
}
