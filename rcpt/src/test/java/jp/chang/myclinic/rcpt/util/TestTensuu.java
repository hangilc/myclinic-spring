package jp.chang.myclinic.rcpt.util;

import jp.chang.myclinic.util.RcptUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTensuu {

    @Test
    public void yakuzaiTensuu(){
        assertEquals(1, RcptUtil.touyakuKingakuToTen(10));
        assertEquals(1, RcptUtil.touyakuKingakuToTen(15));
        assertEquals(2, RcptUtil.touyakuKingakuToTen(15.1));
        assertEquals(2, RcptUtil.touyakuKingakuToTen(25));
        assertEquals(3, RcptUtil.touyakuKingakuToTen(25.2));
        assertEquals(31, RcptUtil.touyakuKingakuToTen(315));
    }
}
