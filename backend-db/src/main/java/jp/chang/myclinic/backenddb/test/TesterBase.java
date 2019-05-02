package jp.chang.myclinic.backenddb.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.mockdata.MockData;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

class TesterBase {

    DbBackend dbBackend;
    static MockData mock = new MockData();
    static private ObjectMapper mapper = new ObjectMapper();
    private List<PracticeLogDTO> practiceLogList = new ArrayList<>();  // synchronized by this

    TesterBase(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
        dbBackend.setPracticeLogPublisher(this::publishPracticeLog);
    }

    private void publishPracticeLog(String log){
        try {
            PracticeLogDTO practiceLog = mapper.readValue(log, PracticeLogDTO.class);
            synchronized(this) {
                practiceLogList.add(practiceLog);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized int getCurrentPracticeLogIndex(){
        return practiceLogList.size();
    }

    synchronized List<PracticeLogDTO> getPracticeLogList(int startIndex, Predicate<PracticeLogDTO> filter){
        List<PracticeLogDTO> result = new ArrayList<>();
        for(int i=startIndex;i<practiceLogList.size();i++){
            PracticeLogDTO log = practiceLogList.get(i);
            if( filter.test(log) ){
                result.add(log);
            }
        }
        return result;
    }

    public void test() {
        String clsName = getClass().getSimpleName();
        try {
            for (Method method : this.getClass().getMethods()) {
                if (method.isAnnotationPresent(DbTest.class)) {
                    System.out.printf("%s:%s\n", clsName, method.getName());
                    boolean invoked = false;
                    if (method.getParameterCount() == 0) {
                        method.invoke(this);
                        invoked = true;
                    }
                    if (!invoked) {
                        throw new RuntimeException("Cannot invoke method: " + clsName + "." + method.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void confirm(boolean ok) {
        if (!ok) {
            throw new RuntimeException("Confirmation failed.");
        }
    }

}
