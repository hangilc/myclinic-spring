package jp.chang.myclinic.pharma.javafx.prevtechou;

import javafx.scene.control.Label;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Title extends Label {

    private static Logger logger = LoggerFactory.getLogger(Title.class);

    Title(String visitedAt) {
        getStyleClass().add("record-title");
        setMaxWidth(Double.MAX_VALUE);
        setText(DateTimeUtil.sqlDateTimeToKanji(visitedAt,
                DateTimeUtil.kanjiFormatter1, DateTimeUtil.kanjiFormatter6));
    }

}
