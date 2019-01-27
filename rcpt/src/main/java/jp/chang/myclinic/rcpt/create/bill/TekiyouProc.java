package jp.chang.myclinic.rcpt.create.bill;

import jp.chang.myclinic.rcpt.create.output.Output;

@FunctionalInterface
public interface TekiyouProc {
    TekiyouProc noOutput = (output, shuukei, tanka, count) -> {};
    void outputTekiyou(Output output, String shuukei, int tanka, int count);
}
