package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.dto.TextDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TextPersistenceMysql {

    public void deleteText(int textId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public void updateText(TextDTO text) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public int enterText(TextDTO text) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public TextDTO getText(int textId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public List<TextDTO> listText(int visitId) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
