package jp.chang.myclinic.logdto.practicelog;

public class PracticeLog {

    public int serialId;
    public String kind;
    public String body;

    public PracticeLog(int serialId, String kind, String body) {
        this.serialId = serialId;
        this.kind = kind;
        this.body = body;
    }
}
