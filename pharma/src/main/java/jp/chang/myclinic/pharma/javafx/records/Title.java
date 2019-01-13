package jp.chang.myclinic.pharma.javafx.records;

import javafx.scene.control.Label;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Title extends Label {

    private static Logger logger = LoggerFactory.getLogger(Title.class);

    Title(String visitedAt) {
        getStyleClass().add("record-title");
        setMaxWidth(Double.MAX_VALUE);
        setText(new KanjiDateRepBuilder(DateTimeUtil.parseSqlDateTime(visitedAt))
            .format1().str(" ").format6().build());
//        setText(DateTimeUtil.sqlDateTimeToKanji(visitedAt,
//                DateTimeUtil.kanjiFormatter1, DateTimeUtil.kanjiFormatter6));
    }

}
