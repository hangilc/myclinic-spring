package jp.chang.myclinic.rcpt.lib;

public interface Mergeable<T extends Mergeable<T>> {
    boolean canMerge(T src);
    void merge(T src);
}
