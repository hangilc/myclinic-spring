package jp.chang.myclinic.logdto.practicelog;

public class PracticeLog {

    public String server;
    public int serialId;
    public String kind;
    public String body;

    public PracticeLog(String server, int serialId, String kind, String body) {
        this.server = server;
        this.serialId = serialId;
        this.kind = kind;
        this.body = body;
    }
}
