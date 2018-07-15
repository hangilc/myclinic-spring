package jp.chang.myclinic.rcpt.create.lib;

public interface Extendable<T> {

    boolean canExtend(T src);
    void extend(T src);

}
