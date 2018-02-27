package jp.chang.myclinic.practice.javafx.parts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.function.Consumer;

public class PageNav {

    private IntegerProperty page = new SimpleIntegerProperty(0);
    private IntegerProperty totalPage = new SimpleIntegerProperty(0);
    private Consumer<Integer> onPageChangeCallback = p -> {};
    private Runnable onResetCallback = () -> {};

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

    public void setOnPageChangeCallback(Consumer<Integer> onPageChangeCallback) {
        this.onPageChangeCallback = onPageChangeCallback;
    }

    public void setOnResetCallback(Runnable onResetCallback) {
        this.onResetCallback = onResetCallback;
    }

    public void reset(){
        setPage(0);
        setTotalPage(0);
        onResetCallback.run();
    }

    private void invokeOnPageChangeCallback(){
    }

    public void gotoPage(int page){
        if( page >= 0 && page < getTotalPage() && page != getPage() ){
            setPage(page);
            onPageChangeCallback.accept(getPage());
        }
    }

    public void gotoFirst(int page){
        gotoPage(0);
    }

    public void gotoLast(){
        gotoPage(getTotalPage() - 1);
    }

    public void advance(int n){
        gotoPage(getPage() + n);
    }

}
