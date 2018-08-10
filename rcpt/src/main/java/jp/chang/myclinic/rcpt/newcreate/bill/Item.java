package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.rcpt.newcreate.output.Output;

public interface Item {
    int getTanka();
    boolean mergeable(Item arg);
    void outputTekiyou(Output output);
}
