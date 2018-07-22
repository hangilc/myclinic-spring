package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.*;
import jp.chang.myclinic.utilfx.TwoColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Record extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Record.class);

    private int visitId;
    private RecordTitle title;
    private TwoColumn body = new TwoColumn(4);
    private VBox drugBox = new VBox(4);
    private VBox shinryouBox = new VBox(0);
    private VBox conductBox = new VBox(4);
    private Text paymentText = new Text();

    Record(RecordModel recordModel) {
        this.visitId = recordModel.getVisitId();
        this.title = new RecordTitle(recordModel);
        paymentText.textProperty().bind(recordModel.paymentRepProperty());
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
        recordModel.getShinryouList().addListener((ListChangeListener<ShinryouModel>) c -> {
            while(c.next()){
                for(ShinryouModel shinryouModel: c.getRemoved()){
                    int shinryouId = shinryouModel.getShinryouId();
                    shinryouBox.getChildren().removeIf(node -> {
                        if( node instanceof RecordShinryou ){
                            RecordShinryou r = (RecordShinryou)node;
                            return r.getShinryouId() == shinryouId;
                        } else {
                            return false;
                        }
                    });
                }
                for(ShinryouModel shinryouModel: c.getAddedSubList()){
                    RecordShinryou recordShinryou = new RecordShinryou(shinryouModel.getShinryouId(),
                            shinryouModel.getShinryoucode(), shinryouModel.getRep());
                    addShinryou(recordShinryou);
                }
            }
        });
        recordModel.getConducts().addListener((ListChangeListener<ConductModel>) c -> {
            while(c.next()){
                for(ConductModel conductModel: c.getRemoved()){
                    int conductId = conductModel.getConductId();
                    conductBox.getChildren().removeIf(node -> {
                        if( node instanceof RecordConduct ){
                            RecordConduct rc = (RecordConduct)node;
                            return rc.getConductId() == conductId;
                        } else {
                            return false;
                        }
                    });
                }
                for(ConductModel conductModel: c.getAddedSubList()){
                    conductBox.getChildren().add(new RecordConduct(conductModel));
                }
            }
        });
        body.getRightBox().getChildren().addAll(
                createHoken(recordModel.hokenRepProperty()),
                drugBox,
                shinryouBox,
                conductBox,
                new TextFlow(paymentText)
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

    private void addShinryou(RecordShinryou recordShinryou){
        int i = 0;
        int n = shinryouBox.getChildren().size();
        for(;i<n;i++){
            Node node = shinryouBox.getChildren().get(i);
            RecordShinryou r = (RecordShinryou)node;
            if( r.getShinryoucode() > recordShinryou.getShinryoucode() ){
                break;
            }
        }
        if( i < n ){
            shinryouBox.getChildren().add(i, recordShinryou);
        } else {
            shinryouBox.getChildren().add(recordShinryou);
        }
    }

}
