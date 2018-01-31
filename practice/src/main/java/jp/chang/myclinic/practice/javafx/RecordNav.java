package jp.chang.myclinic.practice.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.practice.lib.PracticeLib;

public class RecordNav extends TextFlow {

    private IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private IntegerProperty currentPage = new SimpleIntegerProperty(0);
    private Hyperlink gotoFirstLink = new Hyperlink("最初");
    private Hyperlink gotoPrevLink = new Hyperlink("前へ");
    private Hyperlink gotoNextLink = new Hyperlink("次へ");
    private Hyperlink gotoLastLink = new Hyperlink("最後");
    private Text currentPageText = new Text();
    private Text totalPagesText = new Text();

    public RecordNav(){
        totalPages.addListener((obs, oldValue, newValue) -> {
            totalPagesText.setText("" + newValue.intValue());
        });
        currentPage.addListener((obs, oldValue, newValue) -> {
            currentPageText.setText("" + (newValue.intValue()+1));
        });
        currentPageText.setText("" + (currentPage.getValue()+1));
        totalPagesText.setText(totalPages.getValue().toString());
        totalPages.addListener((obs, oldValue, newValue) -> {
            int total = newValue.intValue();
            if( total > 1 ){
                if( getChildren().size() == 0 ){
                    getChildren().addAll(
                            gotoFirstLink,
                            new Text(" "),
                            gotoPrevLink,
                            new Text(" "),
                            gotoNextLink,
                            new Text(" "),
                            gotoLastLink,
                            new Text(" ["),
                            currentPageText,
                            new Text("/"),
                            totalPagesText,
                            new Text("]")
                    );
                }
            } else {
                getChildren().clear();
            }
        });
        gotoFirstLink.setOnAction(event -> PracticeLib.gotoFirstRecordPage());
        gotoPrevLink.setOnAction(event -> PracticeLib.gotoPrevRecordPage());
        gotoNextLink.setOnAction(event -> PracticeLib.gotoNextRecordPage());
        gotoLastLink.setOnAction(event -> PracticeLib.gotoLastRecordPage());
    }

    public int getTotalPages() {
        return totalPages.get();
    }

    public IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages.set(totalPages);
    }

    public int getCurrentPage() {
        return currentPage.get();
    }

    public IntegerProperty currentPageProperty() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage.set(currentPage);
    }

}
