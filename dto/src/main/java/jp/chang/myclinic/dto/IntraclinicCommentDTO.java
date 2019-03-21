package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

public class IntraclinicCommentDTO {
    @Primary
    @AutoInc
    public int id;
    public String name;
    public String content;
    public int postId;
    public String createdAt;

    @Override
    public String toString() {
        return "IntraclinicCommentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", postId=" + postId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
