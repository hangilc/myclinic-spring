package jp.chang.myclinic.mastermap;

import java.time.LocalDate;

public interface Resolver {

    int resolve(String name, LocalDate at);

}
