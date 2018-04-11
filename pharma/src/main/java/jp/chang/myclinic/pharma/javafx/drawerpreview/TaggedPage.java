package jp.chang.myclinic.pharma.javafx.drawerpreview;

import jp.chang.myclinic.drawer.Op;

import java.util.List;

public class TaggedPage<T> {

    private T tag;
    private List<Op> ops;

    public TaggedPage() {

    }

    public TaggedPage(T tag, List<Op> ops){
        this();
        this.tag = tag;
        this.ops = ops;
    }

    public T getTag() {
        return tag;
    }

    public void setTag(T tag) {
        this.tag = tag;
    }

    public List<Op> getOps() {
        return ops;
    }

    public void setOps(List<Op> ops) {
        this.ops = ops;
    }
}
