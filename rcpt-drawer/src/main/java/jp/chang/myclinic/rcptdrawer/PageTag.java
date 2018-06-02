package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.Op;

import java.util.List;

class PageTag {

    List<Op> ops;
    int patientId;

    public PageTag(List<Op> ops, int patientId) {
        this.ops = ops;
        this.patientId = patientId;
    }

}
