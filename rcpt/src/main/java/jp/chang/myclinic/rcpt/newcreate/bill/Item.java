package jp.chang.myclinic.rcpt.newcreate.bill;

import java.util.Objects;

public interface Item {
    int getTanka();
    String getTekiyou();
    default boolean mergeable(Item arg){
        if( arg == null ){
            return false;
        }
        return Objects.equals(this, arg) && Objects.equals(getTekiyou(), arg.getTekiyou());
    }
}
