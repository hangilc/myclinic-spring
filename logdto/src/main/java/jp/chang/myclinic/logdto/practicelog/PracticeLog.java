package jp.chang.myclinic.logdto.practicelog;

public class PracticeLog {

    public int serialId;
    public String kind;
    public String body;

    public PracticeLog(){}

    public PracticeLog(int serialId, String kind, String body) {
        this.serialId = serialId;
        this.kind = kind;
        this.body = body;
    }

    @Override
    public String toString() {
        return "PracticeLog{" +
                "serialId=" + serialId +
                ", kind='" + kind + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
