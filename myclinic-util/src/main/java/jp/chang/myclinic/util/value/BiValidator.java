package jp.chang.myclinic.util.value;

public interface BiValidator<T> {

    void validate(T leftValue, String leftName, T rightValue, String rightName, ErrorMessages em);

}
