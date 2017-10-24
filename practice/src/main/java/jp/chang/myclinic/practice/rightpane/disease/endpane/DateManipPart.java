package jp.chang.myclinic.practice.rightpane.disease.endpane;

import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.lib.dateinput.DateInput;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// TODO: fix end of last month
class DateManipPart extends JPanel {
    DateManipPart(DateInput dateInput){
        setLayout(new MigLayout("insets 0", "", ""));
        Link weekLink = new Link("週");
        weekLink.setCallback(evt -> doAdvanceWeek(dateInput, evt.isShiftDown()));
        Link todayLink = new Link("今日");
        todayLink.setCallback(evt -> {
            dateInput.setValue(LocalDate.now());
        });
        Link endOfMonthLink = new Link("月末");
        endOfMonthLink.setCallback(evt -> doEndOfMonth(dateInput));
        Link endOfLastMonthLink = new Link("先月末");
        endOfLastMonthLink.setCallback(evt -> doEndOfLastMonth(dateInput));
        add(weekLink);
        add(new JLabel("|"));
        add(todayLink);
        add(new JLabel("|"));
        add(endOfMonthLink);
        add(new JLabel("|"));
        add(endOfLastMonthLink);
    }

    private void doAdvanceWeek(DateInput dateInput, boolean reverse){
        dateInput.getValue()
                .ifError(errs -> alert("日付の設定が不適切です。"))
                .ifPresent(date -> {
                    LocalDate newDate = date.plus(reverse ? -1 : 1, ChronoUnit.WEEKS);
                    dateInput.setValue(newDate);
                });
    }

    private void doEndOfMonth(DateInput dateInput){
        dateInput.getValue()
                .ifError(errs -> alert("日付の設定が不適切です。"))
                .ifPresent(date -> {
                    int day = date.lengthOfMonth();
                    LocalDate newDate = date.withDayOfMonth(day);
                    dateInput.setValue(newDate);
                });
    }

    private void doEndOfLastMonth(DateInput dateInput){
        dateInput.getValue()
                .ifError(errs -> alert("日付の設定が不適切です。"))
                .ifPresent(date -> {
                    LocalDate lastMonth = LocalDate.now().plus(-1, ChronoUnit.MONTHS);
                    int day = lastMonth.lengthOfMonth();
                    LocalDate newDate = lastMonth.withDayOfMonth(day);
                    dateInput.setValue(newDate);
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
