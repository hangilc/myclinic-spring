package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.dto.TextDTO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class TextPersistenceMock {

    private Map<Integer, TextDTO> registry = new HashMap<>();

    private int serialId = 1;

    public int enterText(TextDTO text) {
        int textId = serialId++;
        text.textId = textId;
        registry.put(textId, text);
        return textId;
    }

    public TextDTO getText(int textId) {
        return registry.get(textId);
    }

    public void updateText(TextDTO text) {
        if (!registry.containsKey(text.textId)) {
            throw new RuntimeException("no such text");
        }
        registry.put(text.textId, text);
    }

    public void deleteText(int textId) {
        registry.remove(textId);
    }

    public List<TextDTO> listText(int visitId) {
        return registry.values().stream()
                .filter(t -> t.visitId == visitId)
                .sorted(Comparator.comparing(t -> t.textId))
                .collect(toList());
    }
}
