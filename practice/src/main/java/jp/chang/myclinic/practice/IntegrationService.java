package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class IntegrationService {

    private Consumer<TextDTO> onNewTextHandler = t -> {};

    public void broadcastNewText(TextDTO text){
        onNewTextHandler.accept(text);
    }

    public void onNewText(Consumer<TextDTO> handler){
        this.onNewTextHandler = handler;
    }

}
