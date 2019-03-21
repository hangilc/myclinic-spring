package jp.chang.myclinic.logdto.practicelog;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

public class PracticeLogDTO {
    @Primary @AutoInc
    public int serialId;
    public String createdAt;
    public String kind;
    public String body;

    public PracticeLogDTO(){}

    public PracticeLogDTO(int serialId, String createdAt, String kind, String body) {
        this.serialId = serialId;
        this.createdAt = createdAt;
        this.kind = kind;
        this.body = body;
    }

    @Override
    public String toString() {
        return "PracticeLogDTO{" +
                "serialId=" + serialId +
                ", createdAt='" + createdAt + '\'' +
                ", kind='" + kind + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
