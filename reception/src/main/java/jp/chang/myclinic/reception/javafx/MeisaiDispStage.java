package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;

import java.util.List;

public class MeisaiDispStage extends Stage {

    public MeisaiDispStage(PatientDTO patient, VisitDTO visit, MeisaiDTO meisai){
        setTitle("明細の詳細");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        root.getChildren().addAll(
                makePatientRow(patient),
                makeVisitDateRow(visit),
                new MeisaiDetailPane(meisai),
                makeTotalRow(meisai.sections),
                makeButtons()
        );
        setScene(new Scene(root));
    }

    private Node makePatientRow(PatientDTO patient){
        String text = String.format("(%d) %s %s", patient.patientId, patient.lastName, patient.firstName);
        return new Label(text);
    }

    private Node makeVisitDateRow(VisitDTO visit){
        String text = DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt,
                DateTimeUtil.kanjiFormatter1, DateTimeUtil.kanjiFormatter4);
        return new Label(text);
    }

    private Node makeTotalRow(List<MeisaiSectionDTO> sections){
        int total = 0;
        for(MeisaiSectionDTO section: sections){
            for(SectionItemDTO item: section.items){
                total += item.tanka * item.count;
            }
        }
        String text = String.format("総点：%,d", total);
        return new Label(text);
    }

    private Node makeButtons(){
        HBox buttons = new HBox(4);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> close());
        buttons.getChildren().add(okButton);
        return buttons;
    }
}
