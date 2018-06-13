package jp.chang.myclinic.logdto.practicelog;

public class PracticeLogDTO {

    public int serialId;
    public String kind;
    public String body;

    public PracticeLogDTO(){}

    public PracticeLogDTO(int serialId, String kind, String body) {
        this.serialId = serialId;
        this.kind = kind;
        this.body = body;
    }

    @Override
    public String toString() {
        return "PracticeLogDTO{" +
                "serialId=" + serialId +
                ", kind='" + kind + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
