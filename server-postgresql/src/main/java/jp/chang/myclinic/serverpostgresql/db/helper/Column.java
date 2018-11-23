package jp.chang.myclinic.serverpostgresql.db.helper;

public interface Column<T> {

    String getName(String prefix);
    default String getName(){
        return getName(null);
    }

    void stuffToData(T data);

}
