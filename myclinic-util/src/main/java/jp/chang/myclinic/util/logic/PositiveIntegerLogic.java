package jp.chang.myclinic.util.logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PositiveIntegerLogic extends IntegerLogic {

    //private static Logger logger = LoggerFactory.getLogger(PositiveIntegerLogic.class);
    private StringProperty input = new SimpleStringProperty();

    public PositiveIntegerLogic(String name){
        super(name);
    }

    @Override
    public Integer getValue(ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        Integer value = super.getValue(em);
        if( em.hasErrorSince(ne) ){
            return null;
        }
        if( !validate(() -> value > 0, em, "%sが正の値でありません。", getName()) ){
            return null;
        }
        return value;
    }

}
