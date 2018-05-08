package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.Madoku;
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

public class IyakuhinMasterModifier {

    private IyakuhinMasterDTO value;

    public IyakuhinMasterModifier(IyakuhinMasterDTO value) {
        this.value = value;
    }

    public IyakuhinMasterModifier setZaikei(Zaikei zaikei){
        value.zaikei = zaikei.getCode();
        return this;
    }

    public IyakuhinMasterModifier setMadoku(Madoku madoku){
        value.madoku = madoku.getCode();
        return this;
    }

}
