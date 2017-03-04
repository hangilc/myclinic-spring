package jp.chang.myclinic.mastermap;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertSame;

/**
 * Created by hangil on 2017/03/04.
 */
public class CodeMapTest {

    @Test
    public void testPickup(){
        CodeMap map = new CodeMap();
        map.addEntry(new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 3)));
        int result = map.resolve(1, LocalDate.of(2017, 3, 4));
        assertSame("convert to new code", 2, result);
    }

    @Test
    public void testNoPickup(){
        CodeMap map = new CodeMap();
        map.addEntry(new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 3)));
        int result = map.resolve(1, LocalDate.of(2017, 3, 2));
        assertSame("keep original code", 1, result);
    }

    @Test
    public void testKeepNoChangeCode(){
        CodeMap map = new CodeMap();
        map.addEntry(new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 3)));
        int result = map.resolve(10, LocalDate.of(2017, 3, 4));
        assertSame("keep original code", 10, result);
    }

    @Test
    public void testPickupAtTheTransitionDate(){
        CodeMap map = new CodeMap();
        map.addEntry(new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 3)));
        int result = map.resolve(1, LocalDate.of(2017, 3, 3));
        assertSame("convert to new code", 2, result);
    }

    @Test
    public void testPickupWithMultipleEntries(){
        CodeMap map = new CodeMap();
        map.addEntry(new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 3)));
        map.addEntry(new CodeMapEntry(3, 4, LocalDate.of(2017, 3, 3)));
        map.addEntry(new CodeMapEntry(5, 6, LocalDate.of(2017, 3, 3)));
        int result = map.resolve(3, LocalDate.of(2017, 3, 4));
        assertSame("convert to new code", 4, result);
    }

    @Test
    public void testNoSort(){
        CodeMap map = new CodeMap();
        map.addEntry(new CodeMapEntry(2, 3, LocalDate.of(2017, 4, 3)));
        map.addEntry(new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 3)));
        map.addEntry(new CodeMapEntry(3, 4, LocalDate.of(2017, 5, 3)));
        int result = map.resolve(1, LocalDate.of(2017, 4, 4));
        assertSame("test no sort", 2, result);
    }

    @Test
    public void testSort(){
        CodeMap map = new CodeMap();
        map.addEntry(new CodeMapEntry(2, 3, LocalDate.of(2017, 4, 3)));
        map.addEntry(new CodeMapEntry(1, 2, LocalDate.of(2017, 3, 3)));
        map.addEntry(new CodeMapEntry(3, 4, LocalDate.of(2017, 5, 3)));
        map.sortByDate();
        int result = map.resolve(1, LocalDate.of(2017, 4, 4));
        assertSame("test no sort", 3, result);
    }

}
