package jp.chang.myclinic.util;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {

    @Test
    public void trimTest(){
        assertEquals("a b", StringUtil.trimSpaces(" 　\ta b  \t"));
    }
}
