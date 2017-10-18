package jp.chang.myclinic.dto;

/**
 * Created by hangil on 2017/06/07.
 */
public class TextDTO {
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
}
