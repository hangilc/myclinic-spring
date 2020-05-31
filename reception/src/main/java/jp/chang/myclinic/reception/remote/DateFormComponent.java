package jp.chang.myclinic.reception.remote;

import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DateFormComponent implements ComponentFinder, TextComponent {

    private static Logger logger = LoggerFactory.getLogger(DateFormComponent.class);
    private DateForm dateForm;

    public DateFormComponent(DateForm dateForm) {
        this.dateForm = dateForm;
    }

    @Override
    public void setComponentText(String text) {
        try {
            LocalDate date = DateTimeUtil.parseSqlDate(text);
            GengouNenPair gn = KanjiDate.yearToGengou(date);
            dateForm.getGengouInput().setValue(gn.gengou);
            dateForm.getNenInput().setText(String.valueOf(gn.nen));
            dateForm.getMonthInput().setText(String.valueOf(date.getMonth()));
            dateForm.getDayInput().setText(String.valueOf(date.getDayOfMonth()));
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String getComponentText() {
        Gengou g = dateForm.getGengouInput().getValue();
        try {
            int nen = Integer.parseInt(dateForm.getNenInput().getText());
            int month = Integer.parseInt(dateForm.getMonthInput().getText());
            int day = Integer.parseInt(dateForm.getDayInput().getText());
            int year = KanjiDate.gengouToYear(g, nen);
            LocalDate date = LocalDate.of(year, month, day);
            return DateTimeUtil.toSqlDate(date);
        } catch(Exception ex){
            return null;
        }
    }

    @Override
    public Object findComponent(String selector) {
        if( selector == null ){
            return null;
        }
        switch(selector){
            case "Gengou": return dateForm.getGengouInput();
            case "Nen": return dateForm.getNenInput();
            case "Month": return dateForm.getMonthInput();
            case "Day": return dateForm.getDayInput();
            default: return null;
        }
    }
}
