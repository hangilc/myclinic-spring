package jp.chang.myclinic.practice.javafx.text;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class TextRequirement {

    public interface TextRestService {
        CompletableFuture<Integer> enterText(TextDTO text);
        CompletableFuture<Boolean> updateText(TextDTO textDTO);
        CompletableFuture<Boolean> deleteText(int textId);
    }

    public interface TextMainPaneService {
        int getCurrentOrTempVisitId();
        void broadcastNewText(TextDTO newText);
    }

    public TextRestService restService;
    public TextMainPaneService mainPaneService;
    public ShohousenRequirement shohousenRequirement;

    public TextRequirement(){

    }

    public TextRequirement(TextRestService restService, TextMainPaneService mainPaneService,
                           ShohousenRequirement shohousenRequirement){
        this.restService = restService;
        this.mainPaneService = mainPaneService;
        this.shohousenRequirement = shohousenRequirement;
    }

}
