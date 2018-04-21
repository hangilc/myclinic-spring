package jp.chang.myclinic.pharma.javafx.prevtechou;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDrugDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.GuiUtil;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import jp.chang.myclinic.pharma.javafx.lib.Nav;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

class PrevTechouRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(PrevTechouRoot.class);
    private TextField inputField = new TextField();
    private PatientInfo patientInfo = new PatientInfo();
    private Nav nav;
    private VBox disp = new VBox(4);
    private int patientId = 0;
    private PatientDTO currentPatient;

    PrevTechouRoot() {
        super(4);
        getChildren().addAll(
                createSearchInput(),
                patientInfo,
                createNav(),
                createList()
        );
    }

    private Node createSearchInput() {
        HBox hbox = new HBox(4);
        inputField.getStyleClass().add("search-text-input");
        Button searchButton = new Button("検索");
        searchButton.setOnAction(evt -> doSearch());
        hbox.getChildren().addAll(inputField, searchButton);
        return hbox;
    }

    private Node createNav() {
        nav = new Nav() {
            @Override
            protected void onPage(int page) {
                loadPage(page);
            }
        };
        return nav;
    }

    private Node createList() {
        ScrollPane scroll = new ScrollPane(disp);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("result-list-scroll");
        return scroll;
    }

    private void setResultList(List<VisitDrugDTO> visitDrugs) {
        List<Item> items = visitDrugs.stream()
                .map(visitDrug -> new Item(visitDrug, currentPatient))
                .collect(Collectors.toList());
        disp.getChildren().setAll(items);
    }

    private void loadPage(int page) {
        if (patientId > 0 && currentPatient != null) {
            Service.api.pageVisitDrug(patientId, page)
                    .thenAccept(result -> Platform.runLater(() -> {
                        nav.set(page, result.totalPages);
                        setResultList(result.visitDrugs);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doSearch() {
        String text = inputField.getText().trim();
        try {
            this.patientId = Integer.parseInt(text);
            Service.api.getPatient(patientId)
                    .thenAccept(patient -> Platform.runLater(() -> {
                        patientInfo.setPatient(patient);
                        currentPatient = patient;
                        loadPage(0);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } catch (NumberFormatException ex) {
            GuiUtil.alertError("患者番号の入力が不適切です。");
        }
    }

}
