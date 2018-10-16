package jp.chang.myclinic.util.logic;

import java.util.List;

public interface CompositeLogic<T, P> {

    T getValue(List<CompositeError<P>> errors);

}
