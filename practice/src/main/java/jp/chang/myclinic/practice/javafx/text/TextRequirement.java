package jp.chang.myclinic.practice.javafx.text;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenRequirement;

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

    public TextRequirement copy(){
        return new TextRequirement(restService, mainPaneService, shohousenRequirement);
    }

    public static class RestServiceDelegate implements TextRestService {

        private TextRestService base;

        public RestServiceDelegate(TextRestService base){
            this.base = base;
        }

        @Override
        public CompletableFuture<Integer> enterText(TextDTO text) {
            return base.enterText(text);
        }

        @Override
        public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
            return base.updateText(textDTO);
        }

        @Override
        public CompletableFuture<Boolean> deleteText(int textId) {
            return base.deleteText(textId);
        }
    }

    public static class MainPaneServiceDelegate implements TextMainPaneService {
        private TextMainPaneService base;

        public MainPaneServiceDelegate(TextMainPaneService base) {
            this.base = base;
        }

        @Override
        public int getCurrentOrTempVisitId() {
            return base.getCurrentOrTempVisitId();
        }

        @Override
        public void broadcastNewText(TextDTO newText) {
            base.broadcastNewText(newText);
        }
    }

}
