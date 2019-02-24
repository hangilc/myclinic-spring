package jp.chang.myclinic.practice.javafx.text;

import jp.chang.myclinic.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public interface TextLib {

    default CompletableFuture<Integer> enterText(TextDTO text){ throw new RuntimeException("Not implemented"); }

}
