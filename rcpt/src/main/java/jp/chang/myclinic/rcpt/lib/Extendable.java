package jp.chang.myclinic.rcpt.lib;

public interface Extendable<T> {

    boolean canExtend(T src);
    void extend(T src);

}
