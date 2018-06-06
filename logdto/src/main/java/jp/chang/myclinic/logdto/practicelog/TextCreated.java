package jp.chang.myclinic.logdto.practicelog;

import jp.chang.myclinic.dto.TextDTO;

public class TextCreated implements PracticeLogBody {

    public TextDTO text;

    public TextCreated(TextDTO text) {
        this.text = text;
    }
}
