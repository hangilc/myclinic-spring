package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.*;
import jp.chang.myclinic.utilfx.TwoColumn;

import java.util.ArrayList;
import java.util.List;

public class Record extends VBox {

    private TwoColumn body = new TwoColumn(4);
    private VBox drugBox = new VBox();
    private VBox shinryouBox = new VBox();
    private VBox conductBox = new VBox();
    private List<RecordShinryou> shinryouList = new ArrayList<>();
    private List<RecordConduct> conducts = new ArrayList<>();

    public Record(Visit visit){
        getChildren().addAll(
                new RecordTitle(visit),
                body
        );
        addHoken(visit);
        body.getRightBox().getChildren().addAll(drugBox, shinryouBox, conductBox, createCharge(visit));
        bindText(visit);
        bindDrug(visit);
        bindShinryou(visit);
        bindConduct(visit);
    }

    private void bindText(Visit visit){
        visit.getTexts().addListener((ListChangeListener<Text>) c -> {
            while( c.next() ) {
                for (Text item : c.getRemoved()) {
                    body.getLeftBox().getChildren().removeIf(rec -> {
                        final int textId = item.getTextId();
                        if (rec instanceof RecordText) {
                            RecordText recordText = (RecordText) rec;
                            return recordText.getTextId() == textId;
                        } else {
                            return false;
                        }
                    });
                }
                for (Text item : c.getAddedSubList()) {
                    RecordText rec = new RecordText(item);
                    body.getLeftBox().getChildren().add(rec);
                }
            }
        });
    }

    private void bindDrug(Visit visit){
        visit.getDrugs().addListener((ListChangeListener<Drug>) c -> {
            while( c.next() ){
                for(Drug item: c.getRemoved() ){
                    RecordDrug recordDrug = findRecordDrug(item.getDrugId());
                    if( recordDrug != null ){
                        drugBox.getChildren().remove(recordDrug);
                        reIndexDrugs();
                    }
                }
                for(Drug item: c.getAddedSubList()){
                    int index = visit.getDrugs().indexOf(item);
                    RecordDrug rec = new RecordDrug(index+1, item);
                    drugBox.getChildren().add(rec);
                }
            }
        });
    }

    private void bindShinryou(Visit visit){
        visit.getShinryouList().addListener(new ListChangeListener<Shinryou>() {
            @Override
            public void onChanged(Change<? extends Shinryou> c) {
                while( c.next() ){
                    for(Shinryou item: c.getRemoved()){
                        RecordShinryou rec = findRecordShinryou(item.getShinryouId());
                        if( rec != null ){
                            shinryouBox.getChildren().remove(rec);
                        }
                    }
                    for(Shinryou item: c.getAddedSubList()){
                        RecordShinryou rec = new RecordShinryou(item);
                        int i = insertingShinryouIndex(item.getShinryoucode());
                        if( i < 0 ){
                            shinryouBox.getChildren().add(rec);
                        } else {
                            shinryouBox.getChildren().add(i, rec);
                        }
                    }
                }
            }
        });
    }

    private void bindConduct(Visit visit){
        visit.getConducts().addListener((ListChangeListener<Conduct>) c -> {
            while( c.next() ){
                for(Conduct item: c.getAddedSubList()){
                    RecordConduct rec = new RecordConduct(item);
                    conductBox.getChildren().add(rec);
                }
            }
        });
    }

    private RecordShinryou findRecordShinryou(int shinryouId){
        for(Node node: shinryouBox.getChildren()){
            RecordShinryou rec = (RecordShinryou)node;
            if( rec.getShinryouId() == shinryouId ){
                return rec;
            }
        }
        return null;
    }

    private int insertingShinryouIndex(int shinryoucode){
        int n = shinryouBox.getChildren().size();
        for(int i=0;i<n;i++){
            Node node = shinryouBox.getChildren().get(i);
            RecordShinryou rec = (RecordShinryou)node;
            if( rec.getShinryoucode() > shinryoucode) {
                return i;
            }
        }
        return -1;
    }

    private void reIndexDrugs(){
        int index = 1;
        for(Node node: drugBox.getChildren()){
            if( node instanceof RecordDrug ){
                RecordDrug recordDrug = (RecordDrug)node;
                recordDrug.updateIndex(index++);
            }
        }
    }

    private RecordDrug findRecordDrug(int drugId){
        for(Node node: drugBox.getChildren()){
            if( node instanceof RecordDrug ){
                RecordDrug recordDrug = (RecordDrug)node;
                if( recordDrug.getDrugId() == drugId ){
                    return recordDrug;
                }
            }
        }
        return null;
    }

    private void addHoken(Visit visit){
        TextFlow textFlow = new TextFlow();
        javafx.scene.text.Text text = new javafx.scene.text.Text();
        text.textProperty().bind(visit.hokenRepProperty());
        textFlow.getChildren().add(text);
        body.getRightBox().getChildren().add(textFlow);
    }

    private Node createCharge(Visit visit){
        Label label = new Label();
        label.getStyleClass().add("record-charge");
        label.textProperty().bind(visit.getCharge().repProperty());
        return label;
    }
}
