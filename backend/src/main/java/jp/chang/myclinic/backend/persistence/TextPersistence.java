package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.TextDTO;

import java.util.List;

public interface TextPersistence {
    int enterText(TextDTO text);
    TextDTO getText(int textId);
    void updateText(TextDTO text);
    void deleteText(int textId);
    List<TextDTO> listText(int visitId);
}
