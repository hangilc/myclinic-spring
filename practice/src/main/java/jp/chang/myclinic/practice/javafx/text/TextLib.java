package jp.chang.myclinic.practice.javafx.text;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.TextDTO;
import org.jetbrains.annotations.Contract;
import retrofit2.http.Body;

import java.util.concurrent.CompletableFuture;

public interface TextLib {

    default CompletableFuture<Integer> enterText(TextDTO text){ return notImplemented(); }
    default CompletableFuture<Boolean> updateText(@Body TextDTO textDTO){ return notImplemented(); }
    default CompletableFuture<Boolean> deleteText(int textId){ return notImplemented(); }

    default <T> CompletableFuture<T> notImplemented(){ throw new RuntimeException("not implemented"); }
}
