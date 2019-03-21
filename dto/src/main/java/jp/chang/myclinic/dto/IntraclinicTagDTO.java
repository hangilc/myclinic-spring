package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

public class IntraclinicTagDTO {

    @Primary
    @AutoInc
    public int tagId;
    public String name;

    @Override
    public String toString() {
        return "IntraclinicTagDTO{" +
                "tagId=" + tagId +
                ", name='" + name + '\'' +
                '}';
    }
}
