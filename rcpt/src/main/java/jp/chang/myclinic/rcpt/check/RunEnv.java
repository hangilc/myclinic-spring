package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;

import java.util.List;
import java.util.function.Consumer;

public class RunEnv {

    public int year;
    public int month;
    public Service.ServerAPI api;
    public List<Integer> patientIds;
    public boolean fixit;
    public Consumer<Error> errorHandler;
    public boolean verbose;

}
