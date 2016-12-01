package com.personal.myuitls;

import com.personal.myuitls.utils.StringTools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        System.out.println(StringTools.isEmpty(null));
        System.out.println(StringTools.isEmpty("f"));
        System.out.println(StringTools.equals("a","a"));
        System.out.println(StringTools.equals("a","b"));

    }
}