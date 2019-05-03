package jp.chang.myclinic.backenddb.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.util.DateTimeUtil;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class TesterBase {

    DbBackend dbBackend;
    static MockData mock = new MockData();
    static private ObjectMapper mapper = new ObjectMapper();
    private static final List<PracticeLogDTO> practiceLogList = new ArrayList<>();  // synchronized by this

    TesterBase(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
        dbBackend.setPracticeLogPublisher(TesterBase::publishPracticeLog);
    }

    private synchronized static void publishPracticeLog(String log){
        try {
            PracticeLogDTO practiceLog = mapper.readValue(log, PracticeLogDTO.class);
            synchronized(TesterBase.class) {
                practiceLogList.add(practiceLog);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static int getCurrentPracticeLogIndex(){
        return practiceLogList.size();
    }

    synchronized static List<PracticeLogDTO> getPracticeLogList(int startIndex, Predicate<PracticeLogDTO> filter){
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

    String toSqlDatetime(LocalDateTime time){
        return DateTimeUtil.toSqlDateTime(time);
    }

    LocalDateTime fromSqlDatetime(String sqlDatetime){
        return DateTimeUtil.parseSqlDateTime(sqlDatetime);
    }

    void confirm(boolean ok) {
        if (!ok) {
            throw new RuntimeException("Confirmation failed.");
        }
    }

    void confirm(boolean ok, String label, Runnable detail){
        if( !ok ){
            System.out.println(label);
            detail.run();
            throw new RuntimeException("Confirmation failed.");
        }
    }

    @Contract("null -> fail")
    void confirmNotNull(Object obj){
        confirm(obj != null);
    }

    void confirmSingleLog(int startIndex, Predicate<PracticeLogDTO> pred){
        List<PracticeLogDTO> logs = getPracticeLogList(startIndex, pred);
        confirm(logs.size() == 1);
    }

}
