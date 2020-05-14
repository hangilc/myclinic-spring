package jp.chang.myclinic.romaji;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRomaji {

    @Test
    public void testTsuDakuon() {
        String src = "づ";
        String result = Romaji.toRomaji(src);
        assertEquals("zu", result);
    }

}
