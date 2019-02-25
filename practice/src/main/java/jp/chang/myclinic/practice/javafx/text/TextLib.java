package jp.chang.myclinic.practice.javafx.text;

import jp.chang.myclinic.dto.TextDTO;
import retrofit2.http.Body;

import java.util.concurrent.CompletableFuture;

public interface TextLib {

    default CompletableFuture<Integer> enterText(TextDTO text){ throw new RuntimeException("not implemented"); }
    default CompletableFuture<Boolean> updateText(@Body TextDTO textDTO){ throw new RuntimeException("not implemented"); }
    default CompletableFuture<Boolean> deleteText(int textId){ throw new RuntimeException("not implemented"); }
    default int getCurrentVisitId(){ throw new RuntimeException("not implemented"); }
    default int getTempVisitId(){ throw new RuntimeException("not implemented"); }
    default int getCurrentOrTempVisitId(){
        int id = getCurrentVisitId();
        if( id > 0 ){
            return id;
        } else {
            return getTempVisitId();
        }
    }
}
