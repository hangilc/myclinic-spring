package jp.chang.myclinic.practice.javafx.parts;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import jp.chang.myclinic.practice.javafx.HandlerFX;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PageNav<T> {

    public interface Searcher<T> {
        CompletableFuture<List<T>> search(int page);
    }

    public static final int PAGE_RESET = -1;
    private IntegerProperty page = new SimpleIntegerProperty(PAGE_RESET);
    private IntegerProperty totalPage = new SimpleIntegerProperty(0);
    private Searcher<T> searcher = n ->
            CompletableFuture.completedFuture(Collections.emptyList());
    private ObjectProperty<List<T>> items = new SimpleObjectProperty<>(Collections.emptyList());

    public PageNav(Searcher searcher){
        this.searcher = searcher;
    }

    private void reset(){
        setPage(PAGE_RESET);
        setTotalPage(0);
    }

    public void setSearcher(Searcher<T> searcher){
        this.searcher = searcher;
        reset();
    }

    public void start(){
        setPage(0);
    }

    private void triggerLoad(){
        int page = getPage();
        if( page != PAGE_RESET ){
            searcher.search(page)
                    .thenAccept(result -> Platform.runLater(() -> {
                        items.setValue(result);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
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
