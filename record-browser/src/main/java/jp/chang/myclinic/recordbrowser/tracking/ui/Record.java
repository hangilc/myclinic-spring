package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.recordbrowser.tracking.model.RecordModel;
import jp.chang.myclinic.recordbrowser.tracking.model.TextModel;
import jp.chang.myclinic.utilfx.TwoColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Record extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Record.class);

    private int visitId;
    private RecordTitle title;
    private TwoColumn body = new TwoColumn(4);

    Record(RecordModel recordModel) {
        this.visitId = recordModel.getVisitId();
        this.title = new RecordTitle(recordModel);
        recordModel.getTexts().addListener((ListChangeListener<TextModel>) c -> {
            while (c.next()) {
                for (TextModel textModel : c.getRemoved()) {
                    int textId = textModel.getTextId();
                    body.getLeftBox().getChildren().removeIf(node -> {
                        if (node instanceof RecordText) {
                            RecordText t = (RecordText) node;
                            return t.getTextId() == textId;
                        } else {
                            return false;
                        }
                    });
                }
                for (TextModel textModel : c.getAddedSubList()) {
                    RecordText recordText = new RecordText(textModel);
                    body.getLeftBox().getChildren().add(recordText);
                }
            }
        });
        getChildren().addAll(title, body);
    }

    public int getVisitId() {
        return visitId;
    }

    public boolean isCurrent() {
        return title.isCurrent();
    }
}
