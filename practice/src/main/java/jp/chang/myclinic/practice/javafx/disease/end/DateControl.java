package jp.chang.myclinic.practice.javafx.disease.end;

import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateControl extends HBox {

    private static Logger logger = LoggerFactory.getLogger(DateControl.class);

    public DateControl() {
        super(4);
        Hyperlink weekLink = new Hyperlink("週");
        Hyperlink todayLink = new Hyperlink("今日");
        Hyperlink monthEndLink = new Hyperlink("月末");
        Hyperlink lastMonthEndLink = new Hyperlink("先月末");
        getChildren().addAll(
                weekLink,
                todayLink,
                monthEndLink,
                lastMonthEndLink
        );
    }

}
