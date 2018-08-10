package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.rcpt.newcreate.output.Output;

@FunctionalInterface
public interface TekiyouProc {
    TekiyouProc noOutput = (output, shuukei, tanka, count) -> {};
    void outputTekiyou(Output output, String shuukei, int tanka, int count);
}
