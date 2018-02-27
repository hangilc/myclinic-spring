package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class SimplePageNav extends HBox {

    private static Logger logger = LoggerFactory.getLogger(SimplePageNav.class);
    private PageNav pageNav;
    private Text currentPageText = new Text("");
    private Text totalPageText = new Text("");
    private Consumer<Integer> onPageChangeCallback = p -> {};

    public SimplePageNav(int currentPage, int totalPage) {
        this.pageNav = new PageNav(currentPage, totalPage);
        Hyperlink gotoFirstLink = new Hyperlink("≪");
        Hyperlink gotoPrevLink = new Hyperlink("<");
        Hyperlink gotoNextLink = new Hyperlink(">");
        Hyperlink gotoLastLink = new Hyperlink("≫");
        gotoFirstLink.setOnAction(evt -> pageNav.gotoFirst());
        gotoPrevLink.setOnAction(evt -> pageNav.advance(-1));
        gotoNextLink.setOnAction(evt -> pageNav.advance(1));
        gotoLastLink.setOnAction(evt -> pageNav.gotoLast());
        pageNav.setOnResetCallback(() -> {
            currentPageText.setText("");
            totalPageText.setText("");
        });
        pageNav.setOnPageChangeCallback(onPageChangeCallback);
        currentPageText.setText("" + currentPage);
        totalPageText.setText("" + totalPage);
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

    public void setOnPageChangeCallback(Consumer<Integer> onPageChangeCallback) {
        this.onPageChangeCallback = onPageChangeCallback;
    }
    
}
