package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public interface IntegrationService {

    void broadcastNewText(TextDTO text);
    void setOnNewText(Consumer<TextDTO> handler);

}
