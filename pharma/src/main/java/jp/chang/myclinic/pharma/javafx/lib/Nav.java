package jp.chang.myclinic.pharma.javafx.lib;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nav extends HBox {

    private static Logger logger = LoggerFactory.getLogger(Nav.class);
    private int currentPage;
    private int totalPages;
    private Label stateLabel = new Label("");

    public Nav() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        Hyperlink prevLink = new Hyperlink("<");
        Hyperlink nextLink = new Hyperlink(">");
        prevLink.setOnAction(evt -> gotoPage(currentPage-1));
        nextLink.setOnAction(evt -> gotoPage(currentPage+1));
        getChildren().addAll(
                prevLink,
                nextLink,
                stateLabel
        );
        updateVisibility();
    }

    public void set(int page, int totalPages){
        this.currentPage = page;
        this.totalPages = totalPages;
        updateStateLabel();
        updateVisibility();
    }

    protected void onPage(int page){

    }

    private void gotoPage(int page){
        if( page != currentPage && page >= 0 && page < totalPages ){
            onPage(page);
        }
    }

    private void updateVisibility(){
        if( totalPages > 1 ){
            setManaged(true);
            setVisible(true);
        } else {
            setManaged(false);
            setVisible(false);
        }
    }

    private void updateStateLabel(){
        String text = String.format("%d/%d", currentPage+1, totalPages);
        stateLabel.setText(text);
    }

}
