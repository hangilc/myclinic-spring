package jp.chang.myclinic.reception.javafx;

import javafx.stage.Stage;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.verify.ErrorMessages;
import jp.chang.myclinic.utilfx.dateinput.DateInput;

import java.time.LocalDate;
import java.util.function.Consumer;

public class EditHokenBaseStage extends Stage {

    DateInput validFromInput = new DateInput(Gengou.Recent);
    DateInput validUptoInput = new DateInput(Gengou.Recent);

    @FunctionalInterface
    interface ValidDateVerifier {
        String verify(LocalDate value, Consumer<String> handler);
    }

    EditHokenBaseStage(){
        validFromInput.setGengou(Gengou.Current);
        validUptoInput.setGengou(Gengou.Current);
        validUptoInput.setAllowNull(true);
    }

    void setValidUpto(String value){
        if( value == null || value.equals("0000-00-00") ){
            validUptoInput.clear();
        } else {
            validUptoInput.setValue(LocalDate.parse(value));
        }
    }

    void verifyValidFrom(ErrorMessages errs, ValidDateVerifier verifier, Consumer<String> handler){
        LocalDate validFrom = validFromInput.getValue(errList -> {
            errs.add("資格取得日が不適切です。（" + String.join("", errList) + "）");
        });
        if (validFrom != null) {
            errs.addIfError(verifier.verify(validFrom, handler));
        }
    }

    void verifyValidUpto(ErrorMessages errs, ValidDateVerifier verifier, Consumer<String> handler){
        LocalDate validUpto = validUptoInput.getValue(errList -> {
            errs.add("有効期限が不適切です。（" + String.join("", errList) + "）");
        });
        if (validUpto != null) {
            errs.addIfError(verifier.verify(validUpto, handler));
        }
    }
}
