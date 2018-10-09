package jp.chang.myclinic.util.value;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.value.date.DateFormLogic;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TestDateFormLogic extends LogicTestBase {

    @Test
    public void testDate() {
        DateFormLogic logic = new DateFormLogic();
        ObjectProperty<Gengou> gengouProp = new SimpleObjectProperty<>(Gengou.Heisei);
        StringProperty nenProp = new SimpleStringProperty("30");
        StringProperty monthProp = new SimpleStringProperty("10");
        StringProperty dayProp = new SimpleStringProperty("9");
        logic.setGengouSource(gengouProp);
        logic.setNenSource(nenProp);
        logic.setMonthSource(monthProp);
        logic.setDaySource(dayProp);
        LocalDate value = logic.getValue("TEST", em);
        assertTrue(em.hasNoError());
        assertEquals(LocalDate.of(2018, 10, 9), value);
    }

    @Test
    public void testNullGengou() {
        DateFormLogic logic = new DateFormLogic();
        ObjectProperty<Gengou> gengouProp = new SimpleObjectProperty<>(null);
        StringProperty nenProp = new SimpleStringProperty("30");
        StringProperty monthProp = new SimpleStringProperty("10");
        StringProperty dayProp = new SimpleStringProperty("9");
        logic.setGengouSource(gengouProp);
        logic.setNenSource(nenProp);
        logic.setMonthSource(monthProp);
        logic.setDaySource(dayProp);
        LocalDate value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test
    public void testInvalidNen() {
        DateFormLogic logic = new DateFormLogic();
        ObjectProperty<Gengou> gengouProp = new SimpleObjectProperty<>(Gengou.Heisei);
        StringProperty nenProp = new SimpleStringProperty("0");
        StringProperty monthProp = new SimpleStringProperty("10");
        StringProperty dayProp = new SimpleStringProperty("9");
        logic.setGengouSource(gengouProp);
        logic.setNenSource(nenProp);
        logic.setMonthSource(monthProp);
        logic.setDaySource(dayProp);
        LocalDate value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test
    public void testInvalidMonth() {
        DateFormLogic logic = new DateFormLogic();
        ObjectProperty<Gengou> gengouProp = new SimpleObjectProperty<>(Gengou.Heisei);
        StringProperty nenProp = new SimpleStringProperty("30");
        StringProperty monthProp = new SimpleStringProperty("13");
        StringProperty dayProp = new SimpleStringProperty("9");
        logic.setGengouSource(gengouProp);
        logic.setNenSource(nenProp);
        logic.setMonthSource(monthProp);
        logic.setDaySource(dayProp);
        LocalDate value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test
    public void testInvalidDay() {
        DateFormLogic logic = new DateFormLogic();
        ObjectProperty<Gengou> gengouProp = new SimpleObjectProperty<>(Gengou.Heisei);
        StringProperty nenProp = new SimpleStringProperty("30");
        StringProperty monthProp = new SimpleStringProperty("10");
        StringProperty dayProp = new SimpleStringProperty("32");
        logic.setGengouSource(gengouProp);
        logic.setNenSource(nenProp);
        logic.setMonthSource(monthProp);
        logic.setDaySource(dayProp);
        LocalDate value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

    @Test
    public void testInvalidDate() {
        DateFormLogic logic = new DateFormLogic();
        ObjectProperty<Gengou> gengouProp = new SimpleObjectProperty<>(Gengou.Heisei);
        StringProperty nenProp = new SimpleStringProperty("30");
        StringProperty monthProp = new SimpleStringProperty("9");
        StringProperty dayProp = new SimpleStringProperty("31");
        logic.setGengouSource(gengouProp);
        logic.setNenSource(nenProp);
        logic.setMonthSource(monthProp);
        logic.setDaySource(dayProp);
        LocalDate value = logic.getValue("TEST", em);
        assertTrue(em.hasError());
        assertNull(value);
    }

}
