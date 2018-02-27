package jp.chang.myclinic.practice.javafx.parts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PageNav {

    public interface RequestHandler {
        void handle(int page, Runnable cb);
    }

    public static final int NO_PAGE = -1;
    private IntegerProperty page = new SimpleIntegerProperty(NO_PAGE);
    private IntegerProperty totalPage = new SimpleIntegerProperty(0);
    private RequestHandler requestHandler = (page, cb) -> cb.run();

    public void setRequestHandler(RequestHandler requestHandler){
        this.requestHandler = requestHandler;
    }

    public void reset(int totalPage){
        start(NO_PAGE, totalPage);
    }

    public void start(int page, int totalPage){
        setPage(page);
        setTotalPage(totalPage);
    }

    public int getPage() {
        return page.get();
    }

    public IntegerProperty pageProperty() {
        return page;
    }

    public void setPage(int page) {
        this.page.set(page);
    }

    public int getTotalPage() {
        return totalPage.get();
    }

    public IntegerProperty totalPageProperty() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage.set(totalPage);
    }

    public void gotoPage(int page){
        if( page >= 0 && page < getTotalPage() && page != getPage() ){
            requestHandler.handle(page, () -> setPage(page));
        }
    }

    public void gotoFirst(){
        gotoPage(0);
    }

    public void gotoLast(){
        gotoPage(getTotalPage() - 1);
    }

    public void advance(int n){
        gotoPage(getPage() + n);
    }

}
