package jp.chang.myclinic.practice.lib.conduct;

import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.practice.lib.Stuffer;
import jp.chang.myclinic.practice.lib.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface ConductKizaiInputInterface extends Stuffer<ConductKizaiDTO> {

    void setKizaicode(int kizaicode);
    void setName(String name);
    void setAmount(String amount);
    default void setAmount(double value){
        setAmount(ValueFormatter.formatDouble(value));
    }
    void setAmountUnit(String unit);

    int getKizaicode();
    String getAmount();

    default void setMaster(KizaiMasterDTO master){
        setKizaicode(master.kizaicode);
        setName(master.name);
        setAmountUnit(master.unit);
    }

    @Override
    default void stuffInto(ConductKizaiDTO kizai, Consumer<ConductKizaiDTO> okHandler,
                           Consumer<List<String>> errorHandler){
        List<String> err = new ArrayList<>();
        {
            int kizaicode = getKizaicode();
            if( kizaicode == 0 ){
                err.add("器材の種類が入力されていません。");
            } else {
                kizai.kizaicode = kizaicode;
            }
        }
        {
            try {
                kizai.amount = Double.parseDouble(getAmount());
            } catch(NumberFormatException ex){
                err.add("用量の入力が不適切です。");
            }
        }
        if( err.size() > 0 ){
            errorHandler.accept(err);
        } else {
            okHandler.accept(kizai);
        }
    }
}
