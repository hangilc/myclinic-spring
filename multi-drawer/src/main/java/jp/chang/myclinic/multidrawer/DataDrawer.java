package jp.chang.myclinic.multidrawer;

import jp.chang.myclinic.drawer.Op;

import java.util.List;

public interface DataDrawer<T> {
    List<List<Op>> draw(T data);
}
