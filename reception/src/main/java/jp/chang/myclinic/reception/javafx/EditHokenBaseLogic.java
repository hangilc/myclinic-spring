package jp.chang.myclinic.reception.javafx;

import jp.chang.myclinic.utilfx.dateinput.DateInputLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EditHokenBaseLogic {

    //private static Logger logger = LoggerFactory.getLogger(EditHokenBaseLogic.class);

    DateInputLogic validFrom = new DateInputLogic();
    DateInputLogic validUpto = new DateInputLogic();

    public EditHokenBaseLogic(){
        validUpto.setNullAllowed(true);
    }

    public DateInputLogic getValidFrom() {
        return validFrom;
    }

    public DateInputLogic getValidUpto() {
        return validUpto;
    }

    public String verifyValidFromAsStorageValue(Consumer<String> storageValueHandler){
        List<String> errs = new ArrayList<>();
        String storageValue =validFrom.getStorageValue(errs::addAll);
        if( storageValue != null && errs.size() == 0 ){
            if( storageValueHandler != null ){
                storageValueHandler.accept(storageValue);
                return null;
            }
        } else {
            return "資格取得日が不適切です。（" + String.join("", errs) + "）";
        }
    }

    public String verifyValidUptoAsStorageValue(Consumer<String> storageValueHandler){
        List<String> errs = new ArrayList<>();
        String storageValue =validFrom.getStorageValue(errs::addAll);
        if( storageValue != null && errs.size() == 0 ){
            if( storageValueHandler != null ){
                storageValueHandler.accept(storageValue);
                return null;
            }
        } else {
            return "有効期限が不適切です。（" + String.join("", errs) + "）";
        }
    }
}
