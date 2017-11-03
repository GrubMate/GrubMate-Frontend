package com.example.grubmate.grubmate.utilities;

/**
 * Created by tianhangliu on 11/3/17.
 */

public class ArrayUtilities {
    public static String join(String[] list, String separtor) {
        String result = "";
        if (list==null||list.length<1) return result;
        result+=list[0];
        for(int i = 1; i<list.length; i++) {
            result+=separtor;
            result+=list[i];
        }
        return result;
    };
}
