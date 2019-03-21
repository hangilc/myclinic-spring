package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

import java.util.Objects;

/**
 * Created by hangil on 2017/06/07.
 */
public class TextDTO {
    @Primary
    @AutoInc
    public int textId;
    public int visitId;
    public String content;

    public TextDTO copy(){
        TextDTO textDTO = new TextDTO();
        textDTO.textId = textId;
        textDTO.visitId = visitId;
        textDTO.content = content;
        return textDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextDTO textDTO = (TextDTO) o;
        return textId == textDTO.textId &&
                visitId == textDTO.visitId &&
                Objects.equals(content, textDTO.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textId, visitId, content);
    }

    @Override
    public String toString() {
        return "TextDTO{" +
                "textId=" + textId +
                ", visitId=" + visitId +
                ", content='" + content + '\'' +
                '}';
    }
}
