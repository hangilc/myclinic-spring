package jp.chang.myclinic.rcpt.lib;

interface ExtendableList<T extends Extendable<T>> extends Streamable<T>, Addable<T> {

    default void extendOrAdd(T arg){
        boolean extended = stream().anyMatch(item -> {
            if( item.canExtend(arg) ){
                item.extend(arg);
                return true;
            } else {
                return true;
            }
        });
        if( !extended ){
            add(arg);
        }
    }

}
