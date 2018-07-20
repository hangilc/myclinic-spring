package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.DrugModel;
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
    private VBox drugBox = new VBox(4);

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
        recordModel.getDrugs().addListener((ListChangeListener<DrugModel>) c -> {
            while(c.next()){
                boolean needReorder = false;
                for(DrugModel drugModel : c.getRemoved()){
                    int drugId = drugModel.getDrugId();
                    drugBox.getChildren().removeIf(node -> {
                        if( node instanceof RecordDrug ){
                            RecordDrug d = (RecordDrug)node;
                            return d.getDrugId() == drugId;
                        } else {
                            return false;
                        }
                    });
                }
                reIndexDrugs();
                for(DrugModel drugModel : c.getAddedSubList()){
                    long n = drugBox.getChildren().stream()
                            .filter(node ->  node instanceof RecordDrug)
                            .count();
                    RecordDrug recordDrug = new RecordDrug((int)n+1, drugModel);
                    drugBox.getChildren().add(recordDrug);
                }
            }
        });
        body.getRightBox().getChildren().addAll(
                createHoken(recordModel.hokenRepProperty()),
                drugBox
        );
        getChildren().addAll(title, body);
    }

    public int getVisitId() {
        return visitId;
    }

    public boolean isCurrent() {
        return title.isCurrent();
    }

    private Node createHoken(StringProperty hokenRepProperty){
        TextFlow textFlow = new TextFlow();
        javafx.scene.text.Text text = new javafx.scene.text.Text();
        text.textProperty().bind(hokenRepProperty);
        textFlow.getChildren().add(text);
        return textFlow;
    }

    private void reIndexDrugs(){
        int index = 1;
        for(Node node : drugBox.getChildren()){
            if( node instanceof RecordDrug ){
                RecordDrug d = (RecordDrug)node;
                d.setIndex(index++);
            }
        }
    }

}
