package jp.chang.myclinic.reception.javafx.edit_hoken;

import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.value.ErrorMessages;
import jp.chang.myclinic.util.value.Logic;
import jp.chang.myclinic.util.value.ObjectPropertyLogic;
import jp.chang.myclinic.util.value.StringPropertyLogic;
import jp.chang.myclinic.util.value.date.DateFormLogic;
import jp.chang.myclinic.util.value.date.ValidUptoFormLogic;

class ShahokokuhoLogic implements Logic<ShahokokuhoDTO> {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoLogic.class);
    private StringPropertyLogic hokenshaBangouSource = new StringPropertyLogic();
    private StringPropertyLogic hihokenshaKigouSource = new StringPropertyLogic();
    private StringPropertyLogic hihokenshaBangouSource = new StringPropertyLogic();
    private ObjectPropertyLogic<Integer> honninSource = new ObjectPropertyLogic<>();
    private DateFormLogic validFromSource = new DateFormLogic();
    private ValidUptoFormLogic validUptoSource = new ValidUptoFormLogic();
    private ObjectPropertyLogic<Integer> koureiSource = new ObjectPropertyLogic<>();

    private Logic<Integer> hokenshaBangouLogic;
    private Logic<String> hihokenshaKigouLogic;
    private Logic<String> hihokenshaBangouLogic;
    private Logic<Integer> honninLogic;
    private Logic<String> validFromLogic;
    private Logic<String> validUptoLogic;

    ShahokokuhoLogic() {
    }


    @Override
    public ShahokokuhoDTO getValue(String name, ErrorMessages em) {
        ShahokokuhoDTO value = new ShahokokuhoDTO();
        value.hokenshaBangou = hokenshaBangou.getValue(em);
        int ne = em.getNumberOfErrors();
        value.hihokenshaKigou = hihokenshaKigou.getValue(em);
        value.hihokenshaBangou = hihokenshaBangou.getValue(em);
        if( em.hasNoErrorSince(ne) ) {
            if( value.hihokenshaKigou.isEmpty() && value.hihokenshaBangou.isEmpty() ){
                em.add("被保険者記号と被保険者番号が両方とも空白です。");
            }
        }
        value.honnin = honninKazoku.getValue(em);
        return value;
    }


}
