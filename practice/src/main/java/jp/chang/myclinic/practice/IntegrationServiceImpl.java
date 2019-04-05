package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.IntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class IntegrationServiceImpl implements IntegrationService {

    private Consumer<TextDTO> onNewTextHandler = t -> {};

    @Override
    public void broadcastNewText(TextDTO text) {
        onNewTextHandler.accept(text);
    }

    @Override
    public void setOnNewText(Consumer<TextDTO> handler) {
        this.onNewTextHandler = handler;
    }
}
