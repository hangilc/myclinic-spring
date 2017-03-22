package jp.chang.myclinic.mastermap;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Created by hangil on 2017/03/03.
 */
public class CodeMapEntryTest {
    @Test
    public void testCodeMapEntryShouldFindNew(){
        CodeMapEntry entry = new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 4));
        int code = entry.apply(1, LocalDate.of(2017, 3, 5));
        assertEquals("should pick up new code", 2, code);
    }

    @Test
    public void testCodeMapEntryShouldFindNewAtFirstDate(){
        CodeMapEntry entry = new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 4));
        int code = entry.apply(1, LocalDate.of(2017, 3, 4));
        assertEquals("should pick up new code", 2, code);
    }

    @Test
    public void testCodeMapEntryShouldReportOldCode(){
        CodeMapEntry entry = new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 4));
        int code = entry.apply(1, LocalDate.of(2017, 3, 3));
        assertEquals("should not return new code", 1, code);
    }

    @Test(expected = Exception.class)
    public void testInvalidSource(){
        CodeMapEntry.parse("");
    }

    @Test
    public void testSame(){
        assertEquals(611140694, Integer.parseInt("611140694"));
    }

    @Test
    public void testSource(){
        String src = "Y,611140694,2012-04-01,620098801 ロキソニン錠６０ｍｇ";
        CodeMapEntry e = CodeMapEntry.parse(src);
        assertEquals(611140694, e.getOldCode());
        assertEquals(620098801, e.getNewCode());
        assertEquals(LocalDate.of(2012, 4, 1), e.getValidFrom());
        assertEquals("ロキソニン錠６０ｍｇ", e.getComment());
    }

    @Test
    public void testSourceWithoutComment(){
        String src = "Y,611140694,2012-04-01,620098801";
        CodeMapEntry e = CodeMapEntry.parse(src);
        assertEquals(611140694, e.getOldCode());
        assertEquals(620098801, e.getNewCode());
        assertEquals(LocalDate.of(2012, 4, 1), e.getValidFrom());
        assertNull(null, e.getComment());
    }

    @Test
    public void testSourceEmptyComment(){
        String src = "Y,611140694,2012-04-01,620098801   ";
        CodeMapEntry e = CodeMapEntry.parse(src);
        assertEquals(611140694, e.getOldCode());
        assertEquals(620098801, e.getNewCode());
        assertEquals(LocalDate.of(2012, 4, 1), e.getValidFrom());
        assertEquals("", e.getComment());
    }
}
