package jp.chang.myclinic.reception.javafx;

import jp.chang.myclinic.utilfx.dateinput.DateLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EditHokenBaseLogic {

    //private static Logger logger = LoggerFactory.getLogger(EditHokenBaseLogic.class);

    DateLogic validFrom = new DateLogic();
    DateLogic validUpto = new DateLogic();

    public EditHokenBaseLogic(){
        validUpto.setNullAllowed(true);
    }

    public DateLogic getValidFrom() {
        return validFrom;
    }

    public DateLogic getValidUpto() {
        return validUpto;
    }

    public String verifyValidFromAsStorageValue(Consumer<String> storageValueHandler){
        List<String> errs = new ArrayList<>();
        String storageValue =validFrom.getStorageValue(DateLogic.toStorageConverter, errs::add);
        if( storageValue != null && errs.size() == 0 ){
            if( storageValueHandler != null ){
                storageValueHandler.accept(storageValue);
            }
            return null;
        } else {
            return "資格取得日が不適切です。（" + String.join("", errs) + "）";
        }
    }

    public String verifyValidUptoAsStorageValue(Consumer<String> storageValueHandler){
        List<String> errs = new ArrayList<>();
        String storageValue =validFrom.getStorageValue(DateLogic.toStorageConverter, errs::add);
        if( storageValue != null && errs.size() == 0 ){
            if( storageValueHandler != null ){
                storageValueHandler.accept(storageValue);
            }
            return null;
        } else {
            return "有効期限が不適切です。（" + String.join("", errs) + "）";
        }
    }
}
