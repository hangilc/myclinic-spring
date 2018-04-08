package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jp.chang.myclinic.dto.IyakuhincodeNameDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

abstract class ByDrugNav extends VBox implements Nav {

    private static Logger logger = LoggerFactory.getLogger(ByDrugNav.class);
    private int iyakuhincode;
    private String drugName;
    private int currentPage;
    private int totalPages;
    private int patientId;
    private Label patientNameLabel = new Label("");
    private Text currentPageText = new Text("");
    private Label sepLabel = new Label("/");
    private Text totalPagesText = new Text("");
    private Hyperlink prevLink = new Hyperlink("<");
    private Hyperlink nextLink = new Hyperlink(">");
    private Button gotoSummaryButton = new Button("薬剤一覧にもどる");
    private VBox workarea = new VBox(4);

    ByDrugNav() {
        gotoSummaryButton.setOnAction(evt -> doGotoSummary());
        prevLink.setOnAction(evt -> doPrevLink());
        nextLink.setOnAction(evt -> doNextLink());
        getChildren().addAll(
                patientNameLabel,
                workarea
        );
    }

    void setPatient(int patientId, String patientName) {
        this.patientId = patientId;
        patientNameLabel.setText("(" + patientName + ")");
    }

    void reset() {
        this.patientId = 0;
        this.currentPage = 0;
        this.totalPages = 0;
        patientNameLabel.setText("");
        workarea.getChildren().clear();
    }

    private void doGotoSummary(){
        this.iyakuhincode = 0;
        this.drugName = "";
        this.currentPage = 0;
        this.totalPages = 0;
        trigger();
    }

    private void doPrevLink(){
        doGotoPage(currentPage - 1);
    }

    private void doNextLink(){
        doGotoPage(currentPage + 1);
    }

    private void doGotoPage(int page){
        if( currentPage != page && page >= 0 && page < totalPages ){
            this.currentPage = page;
            trigger();
        }
    }

    private void showSummary() {
        Service.api.listIyakuhinForPatient(patientId)
                .thenAccept(result -> Platform.runLater(() -> {
                    renderSummary(result);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void renderSummary(List<IyakuhincodeNameDTO> items) {
        VBox vbox = new VBox(0);
        vbox.getStyleClass().add("drug-list-for-patient");
        items.forEach(item -> {
            Hyperlink link = new Hyperlink(item.name);
            link.setOnAction(evt -> doSelectDrug(item.iyakuhincode, item.name));
            HBox hbox = new HBox(0);
            hbox.setBorder(null);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().addAll(new Label("・"), link);
            vbox.getChildren().add(hbox);
        });
        workarea.getChildren().setAll(vbox);
    }

    private void doSelectDrug(int iyakuhincode, String drugName) {
        this.iyakuhincode = iyakuhincode;
        this.drugName = drugName;
        this.currentPage = 0;
        trigger();
    }

    private void renderPageNav(int currentPage, int totalPages) {
        setCurrentPage(currentPage);
        setTotalPages(totalPages);
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(
                currentPageText,
                sepLabel,
                totalPagesText,
                prevLink,
                nextLink,
                gotoSummaryButton
        );
        workarea.getChildren().setAll(
                new Label(drugName),
                hbox
        );
    }

    private void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        this.currentPageText.setText("" + (currentPage + 1));
    }

    private void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        this.totalPagesText.setText("" + totalPages);
        if (totalPages == 0) {
            this.currentPageText.setText("0");
        }
    }

    @Override
    public void trigger() {
        if (iyakuhincode == 0) {
            onPage(Collections.emptyList(), null);
            showSummary();
        } else {
            Service.api.listVisitTextDrugByPatientAndIyakuhincode(patientId, iyakuhincode, currentPage)
                    .thenAccept(result -> Platform.runLater(() -> {
                        onPage(result.visitTextDrugs, null);
                        renderPageNav(result.page, result.totalPages);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }
}
