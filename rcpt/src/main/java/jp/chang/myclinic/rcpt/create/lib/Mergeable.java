package jp.chang.myclinic.rcpt.create.lib;

public interface Mergeable<T extends Mergeable<T>> {

    boolean canMerge(T src);
    void merge(T src);

}
