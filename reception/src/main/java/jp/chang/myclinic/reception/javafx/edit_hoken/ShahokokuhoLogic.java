package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.value.ErrorMessages;
import jp.chang.myclinic.util.value.Logic;
import jp.chang.myclinic.util.value.ObjectPropertyLogic;
import jp.chang.myclinic.util.value.StringPropertyLogic;
import jp.chang.myclinic.util.value.date.DateFormLogic;

import java.time.LocalDate;

import static jp.chang.myclinic.util.value.Converters.stringToInteger;
import static jp.chang.myclinic.util.value.Validators.*;
import static jp.chang.myclinic.util.value.Converters.*;

class ShahokokuhoLogic implements Logic<ShahokokuhoDTO> {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoLogic.class);
    private StringProperty hokenshaBangouSource = new SimpleStringProperty();
    private StringProperty hihokenshaKigouSource = new SimpleStringProperty();
    private StringProperty hihokenshaBangouSource = new SimpleStringProperty();
    private ObjectProperty<Integer> honninSource = new SimpleObjectProperty<>();
    private DateFormLogic validFromSource = new DateFormLogic();
    private DateFormLogic validUptoSource = new DateFormLogic();
    private ObjectProperty<Integer> koureiSource = new SimpleObjectProperty<>();

    private Logic<Integer> hokenshaBangouLogic;
    private Logic<String> hihokenshaKigouLogic;
    private Logic<String> hihokenshaBangouLogic;
    private Logic<Integer> honninLogic;
    private Logic<LocalDate> validFromLogic;
    private Logic<LocalDate> validUptoLogic;
    private Logic<Integer> koureiLogic;

    ShahokokuhoLogic() {
        this.hokenshaBangouLogic = new StringPropertyLogic(hokenshaBangouSource)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(isPositive())
                .validate(isValidHokenshaBangou());
        this.hihokenshaKigouLogic = new StringPropertyLogic(hihokenshaKigouSource)
                .convert(nullToEmpty());
        this.hihokenshaBangouLogic = new StringPropertyLogic(hihokenshaBangouSource)
                .convert(nullToEmpty());
        this.honninLogic = new ObjectPropertyLogic<Integer>(honninSource)
                .validate(isNotNull());
        this.validFromLogic = validFromSource.validate(isNotNull());
        this.validUptoLogic = validUptoSource.validate(isNotNull());
        this.koureiLogic = new ObjectPropertyLogic<Integer>(koureiSource)
                .validate(isNotNull());
    }


    @Override
    public ShahokokuhoDTO getValue(String name, ErrorMessages em) {
        ShahokokuhoDTO value = new ShahokokuhoDTO();
        int ne = em.getNumberOfErrors();
        value.hokenshaBangou = nullToZero((hokenshaBangouLogic.getValue("保険者番号", em));
        value.hihokenshaKigou = hihokenshaKigouLogic.getValue("被保険者記号", em);
        value.hihokenshaBangou = hihokenshaBangouLogic.getValue("被保険者番号", em);
        if (em.hasNoErrorSince(ne)) {
            if (value.hihokenshaKigou.isEmpty() && value.hihokenshaBangou.isEmpty()) {
                em.add("被保険者記号と被保険者番号が両方とも空白です。");
            }
        }
        //value.honnin = honninKazoku.getValue(em);
        return value;
    }


}
