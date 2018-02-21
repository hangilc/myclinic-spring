package jp.chang.myclinic.practice.javafx.disease.end;

import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DateControl extends HBox {

    private static Logger logger = LoggerFactory.getLogger(DateControl.class);
    private static Consumer<MouseEvent> nop = evt -> {};

    private Consumer<MouseEvent> onWeekCallback = nop;
    private Consumer<MouseEvent> onTodayCallback = nop;
    private Consumer<MouseEvent> onMonthEndCallback = nop;
    private Consumer<MouseEvent> onLastMonthEndCallback = nop;

    public DateControl() {
        super(4);
        Hyperlink weekLink = new Hyperlink("週");
        Hyperlink todayLink = new Hyperlink("今日");
        Hyperlink monthEndLink = new Hyperlink("月末");
        Hyperlink lastMonthEndLink = new Hyperlink("先月末");
        weekLink.setOnMouseClicked(evt -> onWeekCallback.accept(evt));
        todayLink.setOnMouseClicked(evt -> onTodayCallback.accept(evt));
        monthEndLink.setOnMouseClicked(evt -> onMonthEndCallback.accept(evt));
        lastMonthEndLink.setOnMouseClicked(evt -> onLastMonthEndCallback.accept(evt));
        getChildren().addAll(
                weekLink,
                todayLink,
                monthEndLink,
                lastMonthEndLink
        );
    }

    public void setOnWeekCallback(Consumer<MouseEvent> cb){
        this.onWeekCallback = cb;
    }

    public void setOnTodayCallback(Consumer<MouseEvent> cb){
        this.onTodayCallback = cb;
    }

    public void setOnMonthEndCallback(Consumer<MouseEvent> cb){
        this.onMonthEndCallback = cb;
    }

    public void setOnLastMonthEndCallback(Consumer<MouseEvent> cb){
        this.onLastMonthEndCallback = cb;
    }

}
