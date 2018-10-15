package jp.chang.myclinic.util.logic;

public interface Chainer<T, U> {

    Logic<U> chain(Logic<T> src, String name, ErrorMessages em);

}
