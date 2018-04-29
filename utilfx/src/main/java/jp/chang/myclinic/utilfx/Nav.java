package jp.chang.myclinic.utilfx;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Nav extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(Nav.class);

    private static NavHandler nopHandler = (page, cb) -> {};
    private int currentPage;
    private int totalPages;
    private Label stateLabel = new Label("");
    private NavHandler handler = nopHandler;

    public Nav() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        Hyperlink firstLink = new Hyperlink("最初");
        Hyperlink prevLink = new Hyperlink("前へ");
        Hyperlink nextLink = new Hyperlink("次へ");
        Hyperlink lastLink = new Hyperlink("最後");
        firstLink.setOnAction(evt -> gotoPage(0));
        prevLink.setOnAction(evt -> gotoPage(currentPage-1));
        nextLink.setOnAction(evt -> gotoPage(currentPage+1));
        lastLink.setOnAction(evt -> gotoPage(totalPages-1));
        getChildren().addAll(
                firstLink,
                prevLink,
                nextLink,
                lastLink,
                stateLabel
        );
        updateVisibility();
    }

    public void reset(){
        set(0, 0);
        this.handler = nopHandler;
    }

    public void setHandler(NavHandler handler){
        this.handler = handler;
    }

    public void trigger(){
        onPage(currentPage);
    }

    private void set(int page, int totalPages){
        this.currentPage = page;
        this.totalPages = totalPages;
        updateStateLabel();
        updateVisibility();
    }

    private void onPage(int page){
        handler.trigger(page, this::set);
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
