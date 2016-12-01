package com.personal.myuitls.utils;

/**
 * 作者：maohongyu on 2016/11/30.
 * 邮箱：mao_hongyu@qq.com
 * 描述：字符工具类
 */
public class StringTools {

    private StringTools() {
        throw new UnsupportedOperationException(" u can not do the operation ..");
    }

    /**
     * 字符长度是否为0
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        boolean result = false;
        result = isNull(s) || s.length()==0;
        return result;
    }

    /**
     * 字符是否为空
     * @param s
     * @return
     */
    private static boolean isNull(String s) {
        boolean result =false;
        result = null == s;
        return result ;
    }

    /**
     * 是否全为空格；
     * @param s
     * @return
     */
    public static boolean isSpace(String s){
        boolean result = false;
        result = isNull(s) || s.trim().length()==0;
        return result;
    }

    /**
     * 字符长度
     * @param s
     * @return
     */
    public static int length(String s){
        int length = 0;
        if (!isEmpty(s)) {
            length = s.length();
        }
        return length;
    }

    /**
     *除了空格的长度
     * @param s
     * @return
     */
    public static int lengthWithOutSpace(String s){
        int length = 0;
        if (!isEmpty(s)) {
            length  = s.trim().length();
        }
        return length;
    }

    /**
     * 判断两个字符是否相等
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(CharSequence a , CharSequence b){
        if (a == b) return true;
        int length;
        if (null != a && null != b && (length =a.length())== b.length()) {

            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i)!= b.charAt(i)) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
