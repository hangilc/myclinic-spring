package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.TextPersistence;
import jp.chang.myclinic.dto.TextDTO;

import java.util.HashMap;
import java.util.Map;

public class TextPersistenceMock implements TextPersistence {

    private Map<Integer, TextDTO> registry = new HashMap<>();
    private int serialId = 1;

    @Override
    public int enterText(TextDTO text) {
        int textId = serialId++;
        text.textId = textId;
        registry.put(textId, text);
        return textId;
    }

    @Override
    public TextDTO getText(int textId) {
        return registry.get(textId);
    }

    @Override
    public void updateText(TextDTO text) {
        if( !registry.containsKey(text.textId) ){
            throw new RuntimeException("no such text");
        }
        registry.put(text.textId, text);
    }

    @Override
    public void deleteText(int textId) {
        registry.remove(textId);
    }
}
