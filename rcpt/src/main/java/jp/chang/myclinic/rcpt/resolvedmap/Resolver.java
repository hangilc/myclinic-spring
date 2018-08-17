package jp.chang.myclinic.rcpt.resolvedmap;

import java.time.LocalDate;

public interface Resolver {

    int resolve(String name, LocalDate at);

}
