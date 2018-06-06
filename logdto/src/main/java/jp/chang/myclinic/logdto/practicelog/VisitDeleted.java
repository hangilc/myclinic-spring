package jp.chang.myclinic.logdto.practicelog;

public class VisitDeleted implements PracticeLogBody {

    public int visitId;

    public VisitDeleted(int visitId) {
        this.visitId = visitId;
    }
}
