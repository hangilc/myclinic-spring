package jp.chang.myclinic.logdto.practicelog;

import java.util.List;

public class PracticeLogList {

    public String serverId;
    public List<PracticeLog> logs;

    @Override
    public String toString() {
        return "PracticeLogList{" +
                "serverId='" + serverId + '\'' +
                ", logs=" + logs +
                '}';
    }
}
