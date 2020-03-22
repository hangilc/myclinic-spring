package jp.chang.myclinic.reception.remote;

public interface ComponentFinder {
    String getComponentFinderId();
    Object findComponent(String[] selectors);
}
