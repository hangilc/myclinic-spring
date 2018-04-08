package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

abstract class ByDateNav extends HBox implements Nav {

    private static Logger logger = LoggerFactory.getLogger(ByDateNav.class);
    private int patientId;
    private int currentPage;
    private int totalPages;
    private Text currentPageText = new Text("");
    private Text totalPagesText = new Text("");
    private Text nameText = new Text("");
    private Hyperlink prevLink = new Hyperlink("<");
    private Hyperlink nextLink = new Hyperlink(">");

    ByDateNav(){
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        setAlignment(Pos.CENTER_LEFT);
        prevLink.setOnAction(evt -> gotoPrev());
        nextLink.setOnAction(evt -> gotoNext());
    }

    void setPatient(int patientId, String name) {
        this.patientId = patientId;
        nameText.setText("(" + name + ")");
        getChildren().setAll(
                currentPageText,
                new Label("/"),
                totalPagesText,
                prevLink,
                nextLink,
                nameText
        );
    }

    void reset(){
        this.patientId = 0;
        this.currentPage = 0;
        this.totalPages = 0;
        nameText.setText("");
        getChildren().clear();
    }

    @Override
    public void trigger() {
        fetchPage(currentPage);
    }

    private void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
        this.currentPageText.setText("" + (currentPage+1));
    }

    private void setTotalPages(int totalPages){
        this.totalPages = totalPages;
        this.totalPagesText.setText("" + totalPages);
        if( totalPages == 0 ){
            this.currentPageText.setText("0");
        }
    }

    private void gotoPrev(){
        gotoPage(currentPage - 1);
    }

    private void gotoNext(){
        gotoPage(currentPage + 1);
    }

    private void gotoPage(int page){
        if( page != currentPage && page >= 0 && page < totalPages ){
            fetchPage(page);
        }
    }

    private void fetchPage(int page){
        if( patientId == 0 ){
            onPage(Collections.emptyList(), null);
        } else {
            Service.api.listVisitTextDrugForPatient(patientId, page)
                    .thenAccept(result -> Platform.runLater(() -> {
                        onPage(result.visitTextDrugs, null);
                        setCurrentPage(result.page);
                        setTotalPages(result.totalPages);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }
}
