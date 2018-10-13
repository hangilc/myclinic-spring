package jp.chang.myclinic.util.logic;

public interface BiValidator<T> {

    void validate(T left, T right, String leftName, String rightName, ErrorMessages em);

}
