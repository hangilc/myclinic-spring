package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

public class IntraclinicPostDTO {
    @Primary
    @AutoInc
    public Integer id;
    public String content;
    public String createdAt;

    @Override
    public String toString() {
        return "IntraclinicPostDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
