package jp.chang.myclinic.util.value;

import java.util.Objects;

public class IntLogic {

    private Logic<Integer> source;

    public IntLogic(Logic<Integer> source) {
        this.source = source;
    }

    public int getValue(String name, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        Integer value = source.getValue(name, em);
        if( em.hasErrorSince(ne) ){
            return 0;
        }
        return Objects.requireNonNullElse(value, 0);
    }

}
