package jp.chang.myclinic.tracker.model;

import jp.chang.myclinic.dto.TextDTO;

public class Text {

    //private static Logger logger = LoggerFactory.getLogger(Text.class);
    private ModelRegistry registry;
    private int textId;
    private String content;

    public Text(ModelRegistry registry, TextDTO dto) {
        this.registry = registry;
        this.textId = dto.textId;
        this.content = dto.content;
    }

    public int getTextId() {
        return textId;
    }

    public String getContent() {
        return content;
    }
}
