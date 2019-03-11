package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.TextPersistence;
import jp.chang.myclinic.dto.TextDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TextPersistenceMysql implements TextPersistence {

    @Override
    public void deleteText(int textId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public void updateText(TextDTO text) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public int enterText(TextDTO text) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public TextDTO getText(int textId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public List<TextDTO> listText(int visitId) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
