package jp.chang.myclinic.practice.javafx.globalsearch;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Nav extends HBox {

    private static Logger logger = LoggerFactory.getLogger(Nav.class);
    private int currentPage = 0;
    private int totalPage = 0;
    private Text currentPageText = new Text("");
    private Text totalPageText = new Text("");

    Nav() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        Hyperlink gotoFirstLink = new Hyperlink("最初");
        Hyperlink gotoPrevLink = new Hyperlink("前へ");
        Hyperlink gotoNextLink = new Hyperlink("次へ");
        Hyperlink gotoLastLink = new Hyperlink("最後");
        gotoFirstLink.setOnAction(evt -> gotoFirst());
        gotoPrevLink.setOnAction(evt -> gotoPrev());
        gotoNextLink.setOnAction(evt -> gotoNext());
        gotoLastLink.setOnAction(evt -> gotoLast());
        getChildren().addAll(
                gotoFirstLink,
                gotoPrevLink,
                gotoNextLink,
                gotoLastLink,
                currentPageText,
                new Label("/"),
                totalPageText
        );
    }

    void reset(){
        set(0, 0);
    }

    void set(int currentPage, int totalPage){
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        updateDisp();
    }

    private void updateDisp(){
        currentPageText.setText("" + (currentPage + 1));
        totalPageText.setText("" + totalPage);
        setActive(totalPage > 1);
    }

    private void setActive(boolean active){
        setVisible(active);
        setManaged(active);
    }

    private void gotoFirst(){
        gotoPage(0);
    }

    private void gotoPrev(){
        gotoPage(currentPage - 1);
    }

    private void gotoNext(){
        gotoPage(currentPage + 1);
    }

    private void gotoLast(){
        gotoPage(totalPage - 1);
    }

    private void gotoPage(int page){
        if( page != currentPage && page >= 0 && page < totalPage ){
            onPage(page);
        }
    }

    protected void onPage(int page){

    }

}
