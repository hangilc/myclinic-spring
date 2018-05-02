package jp.chang.myclinic.rcpt.unit;

interface Extendable<T> {

    boolean isExtendableWith(T a);
    void extendWith(T a);

}
