package jp.chang.myclinic.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @Test
    public void trimTest(){
        assertEquals("a b", StringUtil.trimSpaces(" 　\ta b  \t"));
    }
}
