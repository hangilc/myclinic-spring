package jp.chang.myclinic.practice.javafx.parts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PageNav {

    private IntegerProperty page = new SimpleIntegerProperty(0);
    private IntegerProperty totalPage = new SimpleIntegerProperty(0);
    public static final int PAGE_RESET = -1;

    public PageNav(int currentPage, int totalPage){
        setPage(currentPage);
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

    public void reset(){
        setPage(PAGE_RESET);
        setTotalPage(0);
    }

    public void gotoPage(int page){
        if( page >= 0 && page < getTotalPage() && page != getPage() ){
            setPage(page);
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
