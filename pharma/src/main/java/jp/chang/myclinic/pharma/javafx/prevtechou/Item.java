package jp.chang.myclinic.pharma.javafx.prevtechou;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitDrugDTO;
import jp.chang.myclinic.pharma.javafx.printing.Printing;
import jp.chang.myclinic.util.DrugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Item extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Item.class);

    Item(VisitDrugDTO visitDrug, PatientDTO patient) {
        super(0);
        getChildren().add(createTitle(visitDrug.visit));
        addDrugs(visitDrug.drugs);
        getChildren().add(createCommands(visitDrug.drugs, patient));
    }

    private Node createTitle(VisitDTO visit){
        return new Title(visit.visitedAt);
    }

    private void addDrugs(List<DrugFullDTO> drugs){
        int index = 1;
        getChildren().add(new Label("Rp)"));
        for(DrugFullDTO drug: drugs){
            String text = String.format("%d) %s", index, DrugUtil.drugRep(drug));
            index += 1;
            Label label = new Label(text);
            label.setWrapText(true);
            getChildren().add(label);
        }
    }

    private Node createCommands(List<DrugFullDTO> drugs, PatientDTO patient){
        HBox hbox = new HBox(4);
        Button printButton = new Button("印刷");
        printButton.setOnAction(evt -> doPrint(drugs, patient));
        hbox.getChildren().addAll(printButton);
        return hbox;
    }

    private void doPrint(List<DrugFullDTO> drugs, PatientDTO patient){
        Printing.previewTechou(drugs, patient);
    }

}
