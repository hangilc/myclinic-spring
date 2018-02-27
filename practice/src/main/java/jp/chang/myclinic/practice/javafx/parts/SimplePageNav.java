package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePageNav extends HBox {

    private static Logger logger = LoggerFactory.getLogger(SimplePageNav.class);
    private Text currentPageText = new Text("");
    private Text totalPageText = new Text("");

    public SimplePageNav(PageNav pageNav) {
        Hyperlink gotoFirstLink = new Hyperlink("≪");
        Hyperlink gotoPrevLink = new Hyperlink("<");
        Hyperlink gotoNextLink = new Hyperlink(">");
        Hyperlink gotoLastLink = new Hyperlink("≫");
        gotoFirstLink.setOnAction(evt -> pageNav.gotoFirst());
        gotoPrevLink.setOnAction(evt -> pageNav.advance(-1));
        gotoNextLink.setOnAction(evt -> pageNav.advance(1));
        gotoLastLink.setOnAction(evt -> pageNav.gotoLast());
        pageNav.pageProperty().addListener((obs, oldValue, newValue) -> {
            int pageValue = newValue.intValue();
            String text = pageValue == PageNav.PAGE_RESET ? "" : "" + (pageValue + 1);
            currentPageText.setText(text);
        });
        pageNav.totalPageProperty().addListener((obs, oldValue, newValue) -> {
            int totalValue = newValue.intValue();
            totalPageText.setText("" + totalValue);
        });
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

}
