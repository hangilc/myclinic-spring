package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.TextDTO;

public interface TextPersistence {
    int enterText(TextDTO text);
    TextDTO getText(int textId);
    void updateText(TextDTO text);
    void deleteText(int textId);
}
